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
    
    Optional<Servico> findByNome(String nome);
    
    @Query("SELECT s FROM Servico s WHERE s.ativo = true")
    List<Servico> findAllAtivos();
    
    boolean existsByNome(String nome);
    
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Servico s WHERE s.nome = :nome AND s.id != :id")
    boolean existsByNomeAndIdNot(@Param("nome") String nome, @Param("id") Long id);
}