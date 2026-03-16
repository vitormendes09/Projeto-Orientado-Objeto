package com.barbearia.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.barbearia.backend.core.entities.Agenda;
import com.barbearia.backend.core.ports.outgoing.AgendaRepository;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface JpaAgendaRepository extends JpaRepository<Agenda, Long>, AgendaRepository {
    
    List<Agenda> findByDiaSemana(Integer diaSemana);
    
    @Query("SELECT a FROM Agenda a WHERE a.ativo = true ORDER BY a.diaSemana, a.horaInicio")
    List<Agenda> findAllAtivas();
}