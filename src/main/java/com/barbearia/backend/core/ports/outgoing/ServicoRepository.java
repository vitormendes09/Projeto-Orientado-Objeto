package com.barbearia.backend.core.ports.outgoing;

import com.barbearia.backend.core.entities.Servico;
import java.util.List;
import java.util.Optional;

public interface ServicoRepository {
    Servico save(Servico servico);
    Optional<Servico> findById(Long id);
    Optional<Servico> findByNome(String nome);
    List<Servico> findAll();
    List<Servico> findAllAtivos();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);
}