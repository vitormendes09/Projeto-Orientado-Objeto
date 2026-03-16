package com.barbearia.backend.core.ports.incoming;

import com.barbearia.backend.core.dtos.agendamento.AgendamentoRequestDTO;
import com.barbearia.backend.core.dtos.agendamento.AgendamentoResponseDTO;
import com.barbearia.backend.core.dtos.agendamento.DisponibilidadeResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface AgendamentoService {
    AgendamentoResponseDTO criar(AgendamentoRequestDTO request);
    AgendamentoResponseDTO buscarPorId(Long id);
    List<AgendamentoResponseDTO> listarPorCliente(Long clienteId);
    List<AgendamentoResponseDTO> listarTodos();
    List<AgendamentoResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim);
    AgendamentoResponseDTO cancelar(Long id, String motivo);
    DisponibilidadeResponseDTO verificarDisponibilidade(LocalDate data, Long servicoId);
    List<DisponibilidadeResponseDTO> verificarDisponibilidadeSemana(LocalDate dataInicio);
}