package com.barbearia.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.barbearia.backend.core.entities.Servico;
import com.barbearia.backend.core.ports.outgoing.ServicoRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaServicoRepository extends JpaRepository<Servico, Long>, ServicoRepository {

    // Query methods por nome (Spring Data gera automaticamente)
    List<Servico> findByBarbeiroId(Long barbeiroId);
    Optional<Servico> findByBarbeiroIdAndNome(Long barbeiroId, String nome);

    // CORREÇÃO: Query explícita para findAllAtivos
    @Query("SELECT s FROM Servico s WHERE s.ativo = true")
    List<Servico> findAllAtivos();

    // Query com JPQL para filtrar por barbeiro e ativos
    @Query("SELECT s FROM Servico s WHERE s.barbeiro.id = :barbeiroId AND s.ativo = true")
    List<Servico> findAllAtivosPorBarbeiro(@Param("barbeiroId") Long barbeiroId);

    // Query para verificar existência por nome ignorando ID
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Servico s " +
           "WHERE s.barbeiro.id = :barbeiroId AND s.nome = :nome AND s.id != :id")
    boolean existsByBarbeiroIdAndNomeAndIdNot(@Param("barbeiroId") Long barbeiroId,
                                              @Param("nome") String nome,
                                              @Param("id") Long id);

    // Mantém os métodos existentes se precisar
    Optional<Servico> findByNome(String nome);
    
    boolean existsByNome(String nome);
    
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Servico s " +
           "WHERE s.nome = :nome AND s.id != :id")
    boolean existsByNomeAndIdNot(@Param("nome") String nome, @Param("id") Long id);
}