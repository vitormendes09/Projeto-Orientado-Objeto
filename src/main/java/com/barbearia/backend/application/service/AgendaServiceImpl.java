package com.barbearia.backend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.backend.core.dtos.agenda.AgendaRequestDTO;
import com.barbearia.backend.core.dtos.agenda.AgendaResponseDTO;
import com.barbearia.backend.core.entities.Agenda;
import com.barbearia.backend.core.entities.Barbeiro;
import com.barbearia.backend.core.exception.BusinessException;
import com.barbearia.backend.core.exception.ResourceNotFoundException;
import com.barbearia.backend.core.ports.incoming.AgendaService;
import com.barbearia.backend.core.ports.outgoing.AgendaRepository;
import com.barbearia.backend.core.ports.outgoing.AgendamentoRepository;
import com.barbearia.backend.core.ports.outgoing.BarbeiroRepository;
import com.barbearia.backend.shared.factory.AgendaFactory;
import com.barbearia.backend.shared.prototype.AgendaPrototype;
import com.barbearia.backend.shared.visitor.AgendaLogVisitor;
import com.barbearia.backend.shared.visitor.AgendaEstatisticaVisitor;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository repository;
    private final AgendamentoRepository agendamentoRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final AgendaFactory factory;
    private final AgendaPrototype prototype;
    private final AgendaLogVisitor logVisitor;
    private final AgendaEstatisticaVisitor estatisticaVisitor;

    private static final Map<Integer, String> DIAS_SEMANA = Map.of(
            1, "Segunda-feira",
            2, "Terça-feira",
            3, "Quarta-feira",
            4, "Quinta-feira",
            5, "Sexta-feira",
            6, "Sábado",
            7, "Domingo");

    @Override
    @Transactional
    public AgendaResponseDTO criar(Long barbeiroId, AgendaRequestDTO request) {
        log.info("Criando agenda para barbeiro ID: {}", barbeiroId);

        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado"));

        validarConflitoHorario(barbeiroId, request.getDiaSemana(),
                request.getHoraInicio(), request.getHoraFim(), null);

        Agenda agenda = factory.criarAgenda(request);
        agenda.setBarbeiro(barbeiro);

        Agenda saved = repository.save(agenda);

        logVisitor.visit(saved);

        return toResponseDTO(saved);
    }

    @Override
    public List<AgendaResponseDTO> listarPorBarbeiro(Long barbeiroId) {
        log.debug("Listando agendas do barbeiro ID: {}", barbeiroId);

        List<Agenda> agendas = repository.findByBarbeiroId(barbeiroId);

        return agendas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AgendaResponseDTO buscarPorId(Long id) {
        log.debug("Buscando configuração de agenda por ID: {}", id);

        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuração de agenda não encontrada com ID: " + id));

        return toResponseDTO(agenda);
    }

    @Override
    public List<AgendaResponseDTO> listarTodos() {
        log.debug("Listando todas as configurações de agenda");

        List<Agenda> agendas = repository.findAll();

        var estatisticas = estatisticaVisitor.visitAll(agendas);
        log.info("Estatísticas de agenda: {}", estatisticas);

        return agendas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgendaResponseDTO> listarPorDiaSemana(Integer diaSemana) {
        log.debug("Listando configurações para o dia {}", getNomeDiaSemana(diaSemana));

        if (diaSemana < 1 || diaSemana > 7) {
            throw new BusinessException("Dia da semana inválido. Use 1 (segunda) a 7 (domingo)");
        }

        List<Agenda> agendas = repository.findByDiaSemana(diaSemana);

        return agendas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgendaResponseDTO> listarAtivos() {
        log.debug("Listando configurações de agenda ativas");

        List<Agenda> agendas = repository.findAll().stream()
                .filter(Agenda::getAtivo)
                .collect(Collectors.toList());

        return agendas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AgendaResponseDTO atualizar(Long id, AgendaRequestDTO request) {
        log.info("Atualizando configuração de agenda ID: {}", id);

        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuração de agenda não encontrada com ID: " + id));

        Agenda backup = prototype.clonar(agenda);

        // CORREÇÃO: Removido o parâmetro barbeiroId da chamada
        validarConflitoHorario(agenda.getBarbeiro().getId(), request.getDiaSemana(), 
                              request.getHoraInicio(), request.getHoraFim(), id);

        Agenda updated = factory.atualizarAgenda(agenda, request);
        updated = repository.save(updated);

        if (!backup.getHoraInicio().equals(updated.getHoraInicio()) ||
                !backup.getHoraFim().equals(updated.getHoraFim())) {
            log.debug("Horário alterado de {} - {} para {} - {}",
                    backup.getHoraInicio(), backup.getHoraFim(),
                    updated.getHoraInicio(), updated.getHoraFim());
        }

        log.info("Configuração de agenda atualizada com sucesso!");
        return toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        log.info("Desativando configuração de agenda ID: {}", id);

        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuração de agenda não encontrada com ID: " + id));

        agenda.setAtivo(false);
        repository.save(agenda);

        log.info("Configuração de agenda desativada com sucesso");
    }

    @Override
    @Transactional
    public void reativar(Long id) {
        log.info("Reativando configuração de agenda ID: {}", id);

        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuração de agenda não encontrada com ID: " + id));

        agenda.setAtivo(true);
        repository.save(agenda);

        log.info("Configuração de agenda reativada com sucesso");
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.warn("Deletando configuração de agenda ID: {} (HARD DELETE)", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Configuração de agenda não encontrada com ID: " + id);
        }

        Agenda agenda = repository.findById(id).get();
        boolean temAgendamentos = verificarSeHaAgendamentos(agenda.getDiaSemana());

        if (temAgendamentos) {
            throw new BusinessException(
                    "Não é possível deletar esta configuração pois existem agendamentos para este dia. " +
                            "Considere desativar em vez de deletar.");
        }

        repository.deleteById(id);
        log.info("Configuração de agenda deletada permanentemente");
    }

    @Override
    public boolean isHorarioValido(Integer diaSemana, LocalTime horaInicio, int duracaoMinutos) {
        LocalTime horaFim = horaInicio.plusMinutes(duracaoMinutos);

        List<Agenda> agendas = repository.findByDiaSemana(diaSemana);

        if (agendas.isEmpty()) {
            return false;
        }

        return agendas.stream()
                .filter(Agenda::getAtivo)
                .anyMatch(agenda -> agenda.isHorarioDisponivel(horaInicio, duracaoMinutos));
    }

    /**
     * CORREÇÃO: Método validarConflitoHorario atualizado para incluir barbeiroId
     */
    private void validarConflitoHorario(Long barbeiroId, Integer diaSemana, 
                                        LocalTime horaInicio, LocalTime horaFim, Long ignorarId) {
        List<Agenda> agendasDoDia = repository.findByBarbeiroIdAndDiaSemana(barbeiroId, diaSemana);

        for (Agenda agenda : agendasDoDia) {
            if (ignorarId != null && agenda.getId().equals(ignorarId)) {
                continue;
            }

            boolean conflito = !(horaFim.isBefore(agenda.getHoraInicio()) ||
                    horaInicio.isAfter(agenda.getHoraFim()));

            if (conflito) {
                throw new BusinessException(String.format(
                        "Já existe uma configuração para %s no horário %s - %s",
                        getNomeDiaSemana(diaSemana), agenda.getHoraInicio(), agenda.getHoraFim()));
            }
        }
    }

    private boolean verificarSeHaAgendamentos(Integer diaSemana) {
        return false;
    }

    private AgendaResponseDTO toResponseDTO(Agenda agenda) {
        return new AgendaResponseDTO(
                agenda.getId(),
                agenda.getDiaSemana(),
                getNomeDiaSemana(agenda.getDiaSemana()),
                agenda.getHoraInicio(),
                agenda.getHoraFim(),
                agenda.getAtivo(),
                agenda.getDataCriacao());
    }

    private String getNomeDiaSemana(Integer dia) {
        return DIAS_SEMANA.getOrDefault(dia, "Dia inválido");
    }
}