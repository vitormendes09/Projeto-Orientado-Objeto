package com.barbearia.backend.core.ports.outgoing;

import com.barbearia.backend.core.entities.Barbeiro;
import java.util.List;
import java.util.Optional;

public interface BarbeiroRepository {
    Barbeiro save(Barbeiro barbeiro);
    Optional<Barbeiro> findById(Long id);
    Optional<Barbeiro> findByEmail(String email);
    Optional<Barbeiro> findByTelefone(String telefone);
    List<Barbeiro> findAll();
    List<Barbeiro> findAllAtivos();
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
    boolean existsById(Long id);
}