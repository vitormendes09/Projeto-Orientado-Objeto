package com.barbearia.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.barbearia.backend.core.entities.Barbeiro;
import com.barbearia.backend.core.ports.outgoing.BarbeiroRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaBarbeiroRepository extends JpaRepository<Barbeiro, Long>, BarbeiroRepository {
    
    Optional<Barbeiro> findByEmail(String email);
    
    Optional<Barbeiro> findByTelefone(String telefone);
    
    @Query("SELECT b FROM Barbeiro b WHERE b.ativo = true")
    List<Barbeiro> findAllAtivos();
    
    boolean existsByEmail(String email);
    
    boolean existsByTelefone(String telefone);
}