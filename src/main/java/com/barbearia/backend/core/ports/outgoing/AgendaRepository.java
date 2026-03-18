package com.barbearia.backend.core.ports.outgoing;

import com.barbearia.backend.core.entities.Agenda;
import java.util.List;
import java.util.Optional;

public interface AgendaRepository {
    Agenda save(Agenda agenda);
    Optional<Agenda> findById(Long id);
    List<Agenda> findAll();
    List<Agenda> findByDiaSemana(Integer diaSemana);
    List<Agenda> findByBarbeiroId(Long barbeiroId);
    List<Agenda> findByBarbeiroIdAndDiaSemana(Long barbeiroId, Integer diaSemana); 
    void deleteById(Long id);
    boolean existsById(Long id);
}