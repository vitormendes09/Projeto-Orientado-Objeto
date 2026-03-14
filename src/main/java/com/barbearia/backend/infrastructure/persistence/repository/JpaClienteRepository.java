package  com.barbearia.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbearia.backend.core.entities.Cliente;
import com.barbearia.backend.core.ports.outgoing.ClienteRepository;

import java.util.Optional;

/**
 * CAMADA DE INFRAESTRUTURA
 * 
 * Implementação concreta do repositório usando Spring Data JPA
 * 
 * SOLID:
 * - DIP: Implementa a interface definida no core
 * - ISP: Depende apenas dos métodos que precisa (JpaRepository genérico)
 */
@Repository
public interface JpaClienteRepository extends JpaRepository<Cliente, Long>, ClienteRepository {
    
    // Métodos customizados com derived queries
    Optional<Cliente> findByTelefone(String telefone);
    
    boolean existsByTelefone(String telefone);
    
    // O JpaRepository já fornece save, findById, findAll, deleteById
}