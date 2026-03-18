package com.barbearia.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.barbearia.backend.core.entities.Agendamento;
import com.barbearia.backend.core.entities.StatusAgendamento;
import com.barbearia.backend.core.ports.outgoing.AgendamentoRepository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaAgendamentoRepository extends JpaRepository<Agendamento, Long>, AgendamentoRepository {

       List<Agendamento> findByBarbeiroId(Long barbeiroId);

       List<Agendamento> findByClienteId(Long clienteId);

       @Query("SELECT a FROM Agendamento a WHERE a.dataHoraInicio >= :inicio AND a.dataHoraFim <= :fim")
       List<Agendamento> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

       List<Agendamento> findByStatus(StatusAgendamento status);

       @Query("SELECT a FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId " +
                     "AND a.dataHoraInicio >= :inicio AND a.dataHoraFim <= :fim")
       List<Agendamento> findByBarbeiroIdAndPeriodo(@Param("barbeiroId") Long barbeiroId,
                     @Param("inicio") LocalDateTime inicio,
                     @Param("fim") LocalDateTime fim);
                     

       @Query("SELECT a FROM Agendamento a WHERE " +
                     "a.status != 'CANCELADO' AND " +
                     "((a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio))")
       List<Agendamento> findConflitantes(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

       @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Agendamento a WHERE " +
                     "a.status != 'CANCELADO' AND " +
                     "((a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)) AND " +
                     "(:ignorarId IS NULL OR a.id != :ignorarId)")
       boolean existsConflitante(@Param("inicio") LocalDateTime inicio,
                     @Param("fim") LocalDateTime fim,
                     @Param("ignorarId") Long ignorarId);

       @Query("SELECT a FROM Agendamento a WHERE " +
                     "DATE(a.dataHoraInicio) = DATE(:data) AND " +
                     "a.status != 'CANCELADO' " +
                     "ORDER BY a.dataHoraInicio")
       List<Agendamento> findByData(@Param("data") LocalDateTime data);
}