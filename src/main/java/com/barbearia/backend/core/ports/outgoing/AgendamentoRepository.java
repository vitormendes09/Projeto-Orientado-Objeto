package com.barbearia.backend.core.ports.outgoing;

import com.barbearia.backend.core.entities.Agendamento;
import com.barbearia.backend.core.entities.StatusAgendamento;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository {
    Agendamento save(Agendamento agendamento);
    Optional<Agendamento> findById(Long id);
    List<Agendamento> findAll();
    List<Agendamento> findByClienteId(Long clienteId);
    List<Agendamento> findByPeriodo(LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByStatus(StatusAgendamento status);
    List<Agendamento> findConflitantes(LocalDateTime inicio, LocalDateTime fim);
    boolean existsConflitante(LocalDateTime inicio, LocalDateTime fim, Long ignorarId);
    void deleteById(Long id);
}