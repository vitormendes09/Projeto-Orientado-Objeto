package com.barbearia.backend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.backend.core.dtos.agenda.AgendaResponseDTO;
import com.barbearia.backend.core.dtos.agendamento.AgendamentoRequestDTO;
import com.barbearia.backend.core.dtos.agendamento.AgendamentoResponseDTO;
import com.barbearia.backend.core.dtos.agendamento.DisponibilidadeResponseDTO;
import com.barbearia.backend.core.entities.*;
import com.barbearia.backend.core.exception.BusinessException;
import com.barbearia.backend.core.exception.ResourceNotFoundException;
import com.barbearia.backend.core.ports.incoming.AgendaService;
import com.barbearia.backend.core.ports.incoming.AgendamentoService;
import com.barbearia.backend.core.ports.outgoing.*;
import com.barbearia.backend.shared.factory.AgendamentoFactory;
import com.barbearia.backend.shared.prototype.AgendamentoPrototype;
import com.barbearia.backend.shared.visitor.AgendamentoLogVisitor;
import com.barbearia.backend.shared.visitor.AgendamentoEstatisticaVisitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;
    private final AgendaRepository agendaRepository;
    private final AgendaService agendaService;
    private final AgendamentoFactory factory;
    private final AgendamentoPrototype prototype;
    private final AgendamentoLogVisitor logVisitor;
    private final AgendamentoEstatisticaVisitor estatisticaVisitor;

    @Override
    @Transactional
    public AgendamentoResponseDTO criar(AgendamentoRequestDTO request) {
        log.info("Criando novo agendamento para cliente ID: {}", request.getClienteId());

        // 1. Buscar cliente
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // 2. Buscar serviços
        List<Servico> servicos = new ArrayList<>();
        for (Long servicoId : request.getServicosIds()) {
            Servico servico = servicoRepository.findById(servicoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + servicoId));
            servicos.add(servico);
        }

        if (servicos.isEmpty()) {
            throw new BusinessException("Nenhum serviço válido encontrado");
        }

        // 3. Validar serviços ativos
        boolean hasInativo = servicos.stream().anyMatch(s -> !s.getAtivo());
        if (hasInativo) {
            throw new BusinessException("Não é possível agendar serviços inativos");
        }

        // 4. Pegar o barbeiro do primeiro serviço (todos devem ser do mesmo barbeiro)
        Barbeiro barbeiro = servicos.get(0).getBarbeiro();
        boolean mesmoBarbeiro = servicos.stream()
                .allMatch(s -> s.getBarbeiro().getId().equals(barbeiro.getId()));

        if (!mesmoBarbeiro) {
            throw new BusinessException("Todos os serviços devem ser do mesmo barbeiro");
        }

        // 5. Calcular duração total
        int duracaoTotal = servicos.stream()
                .mapToInt(Servico::getDuracaoMinutos)
                .sum();

        LocalDateTime dataHoraFim = request.getDataHoraInicio().plusMinutes(duracaoTotal);

        // 6. Validar dentro do horário de funcionamento
        validarHorarioFuncionamento(request.getDataHoraInicio(), dataHoraFim);

        // 7. Validar conflitos
        validarConflitos(request.getDataHoraInicio(), dataHoraFim, null);

        // 8. Criar agendamento usando Factory
        Agendamento agendamento = factory.criarAgendamento(cliente, servicos,
                request.getDataHoraInicio(), dataHoraFim, request.getObservacoes());

        // 9. IMPORTANTE: Associar o barbeiro
        agendamento.setBarbeiro(barbeiro);

        // 10. Registrar agendamento no cliente
        cliente.registrarAgendamento();

        // 11. Incrementar contador nos serviços
        servicos.forEach(Servico::registrarAgendamento);

        // 12. Salvar
        Agendamento saved = agendamentoRepository.save(agendamento);

        // 13. Log via Visitor
        logVisitor.visit(saved);

        log.info("Agendamento criado com sucesso! ID: {}, Horário: {} - {}",
                saved.getId(), saved.getDataHoraInicio(), saved.getDataHoraFim());

        return toResponseDTO(saved);
    }

    @Override
    public AgendamentoResponseDTO buscarPorId(Long id) {
        log.debug("Buscando agendamento por ID: {}", id);

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        return toResponseDTO(agendamento);
    }

    @Override
    public List<AgendamentoResponseDTO> listarPorCliente(Long clienteId) {
        log.debug("Listando agendamentos do cliente ID: {}", clienteId);

        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        List<Agendamento> agendamentos = agendamentoRepository.findByClienteId(clienteId);

        return agendamentos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgendamentoResponseDTO> listarTodos() {
        log.debug("Listando todos os agendamentos");

        List<Agendamento> agendamentos = agendamentoRepository.findAll();

        // Estatísticas via Visitor
        var estatisticas = estatisticaVisitor.visitAll(agendamentos);
        log.info("Estatísticas de agendamentos: {}", estatisticas);

        return agendamentos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgendamentoResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        log.debug("Listando agendamentos do período {} a {}", dataInicio, dataFim);

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.plusDays(1).atStartOfDay();

        List<Agendamento> agendamentos = agendamentoRepository.findByPeriodo(inicio, fim);

        return agendamentos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AgendamentoResponseDTO cancelar(Long id, String motivo) {
        log.info("Cancelando agendamento ID: {}", id);

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        // Fazer backup antes de cancelar (Prototype)
        Agendamento backup = prototype.clonar(agendamento);

        // Validar se pode cancelar (RN04)
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Agendamento já está cancelado");
        }

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new BusinessException("Não é possível cancelar um agendamento concluído");
        }

        // Cancelar
        agendamento.cancelar(motivo);

        Agendamento canceled = agendamentoRepository.save(agendamento);

        log.info("Agendamento {} cancelado. Motivo: {}", id, motivo);

        return toResponseDTO(canceled);
    }

    @Override
    public DisponibilidadeResponseDTO verificarDisponibilidade(LocalDate data, Long servicoId) {
        log.debug("Verificando disponibilidade para data: {}, serviço: {}", data, servicoId);

        // Buscar serviço para saber duração
        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        if (!servico.getAtivo()) {
            throw new BusinessException("Serviço inativo não pode ser agendado");
        }

        // Buscar agenda do dia
        int diaSemana = data.getDayOfWeek().getValue(); // 1=Segunda, 7=Domingo
        List<Agenda> agendas = agendaRepository.findByDiaSemana(diaSemana);

        if (agendas.isEmpty()) {
            // Dia sem expediente
            return new DisponibilidadeResponseDTO(data, new ArrayList<>());
        }

        // Buscar agendamentos existentes
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.plusDays(1).atStartOfDay();
        List<Agendamento> agendamentosDia = agendamentoRepository.findByPeriodo(inicioDia, fimDia);

        // Gerar slots de horário (a cada 30 minutos)
        List<DisponibilidadeResponseDTO.HorarioDisponivelDTO> horarios = new ArrayList<>();

        for (Agenda agenda : agendas) {
            LocalTime horarioAtual = agenda.getHoraInicio();

            while (horarioAtual.plusMinutes(servico.getDuracaoMinutos()).isBefore(agenda.getHoraFim()) ||
                    horarioAtual.plusMinutes(servico.getDuracaoMinutos()).equals(agenda.getHoraFim())) {

                LocalDateTime inicioSlot = data.atTime(horarioAtual);
                LocalDateTime fimSlot = inicioSlot.plusMinutes(servico.getDuracaoMinutos());

                // Verificar se está disponível
                boolean disponivel = true;

                for (Agendamento a : agendamentosDia) {
                    if (a.getStatus() != StatusAgendamento.CANCELADO &&
                            a.conflitaCom(Agendamento.builder()
                                    .dataHoraInicio(inicioSlot)
                                    .dataHoraFim(fimSlot)
                                    .build())) {
                        disponivel = false;
                        break;
                    }
                }

                if (disponivel) {
                    horarios.add(new DisponibilidadeResponseDTO.HorarioDisponivelDTO(
                            horarioAtual.toString(), true, servico.getDuracaoMinutos()));
                } else {
                    horarios.add(new DisponibilidadeResponseDTO.HorarioDisponivelDTO(
                            horarioAtual.toString(), false, servico.getDuracaoMinutos()));
                }

                horarioAtual = horarioAtual.plusMinutes(30); // Slot de 30 min
            }
        }

        return new DisponibilidadeResponseDTO(data, horarios);
    }

    @Override
    public List<DisponibilidadeResponseDTO> verificarDisponibilidadeSemana(LocalDate dataInicio) {
        log.debug("Verificando disponibilidade para semana a partir de: {}", dataInicio);

        List<DisponibilidadeResponseDTO> resultados = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate data = dataInicio.plusDays(i);
            // Para simplificar, verificamos disponibilidade para o primeiro serviço
            // Em produção, isso seria mais sofisticado
            resultados.add(verificarDisponibilidade(data, 1L));
        }

        return resultados;
    }

    /**
     * RN01 - Validar se horário está dentro do expediente
     */
    private void validarHorarioFuncionamento(LocalDateTime inicio, LocalDateTime fim) {
        int diaSemana = inicio.getDayOfWeek().getValue(); // 1=Segunda, 7=Domingo
        LocalTime horaInicio = inicio.toLocalTime();
        int duracaoMinutos = (int) java.time.Duration.between(inicio, fim).toMinutes();

        boolean horarioValido = agendaService.isHorarioValido(diaSemana, horaInicio, duracaoMinutos);

        if (!horarioValido) {
            // Buscar agendas para mostrar horários disponíveis
            List<AgendaResponseDTO> agendas = agendaService.listarPorDiaSemana(diaSemana);

            if (agendas.isEmpty()) {
                throw new BusinessException("Barbearia não abre neste dia da semana");
            } else {
                String horariosDisponiveis = agendas.stream()
                        .filter(AgendaResponseDTO::getAtivo)
                        .map(a -> String.format("%s - %s", a.getHoraInicio(), a.getHoraFim()))
                        .collect(java.util.stream.Collectors.joining(", "));

                throw new BusinessException(String.format(
                        "Horário fora do expediente. Horários disponíveis para %s: %s",
                        agendas.get(0).getNomeDiaSemana(),
                        horariosDisponiveis));
            }
        }
    }

    /**
     * RN03 - Validar conflitos de horário
     */
    private void validarConflitos(LocalDateTime inicio, LocalDateTime fim, Long ignorarId) {
        boolean temConflito = agendamentoRepository.existsConflitante(inicio, fim, ignorarId);

        if (temConflito) {
            throw new BusinessException("Já existe um agendamento neste horário");
        }
    }

    private AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        List<AgendamentoResponseDTO.ServicoAgendadoDTO> servicosDTO = agendamento.getServicos()
                .stream()
                .map(s -> new AgendamentoResponseDTO.ServicoAgendadoDTO(
                        s.getId(), s.getNome(), s.getPreco().doubleValue(), s.getDuracaoMinutos()))
                .collect(Collectors.toList());

        return new AgendamentoResponseDTO(
                agendamento.getId(),
                agendamento.getCliente().getId(),
                agendamento.getCliente().getNome(),
                agendamento.getCliente().getTelefone(),
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFim(),
                servicosDTO,
                agendamento.getStatus().toString(),
                agendamento.getValorTotal(),
                agendamento.getObservacoes(),
                agendamento.getDataCriacao(),
                agendamento.getDataCancelamento(),
                agendamento.getMotivoCancelamento());
    }
}