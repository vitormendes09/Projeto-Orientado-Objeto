package com.barbearia.backend.core.ports.outgoing;


import java.util.List;
import java.util.Optional;

import com.barbearia.backend.core.entities.Cliente;

/**
 * PORTA DE SAÍDA (OUTGOING PORT)
 * 
 * Princípio: Dependência Inversa (DIP do SOLID)
 * - O core não depende de infraestrutura
 * - A infraestrutura implementa esta interface
 * 
 * Responsabilidade: Definir contrato para persistência de Clientes
 */

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(Long id);
    Optional<Cliente> findByTelefone(String telefone);
    List<Cliente> findAll();
    void deleteById(Long id);
    boolean existsByTelefone(String telefone);
    boolean existsById(Long id);
}