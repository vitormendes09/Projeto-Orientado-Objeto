package com.barbearia.backend.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.backend.core.dtos.ClienteRequestDTO;
import com.barbearia.backend.core.dtos.ClienteResponseDTO;
import com.barbearia.backend.core.entities.Cliente;
import com.barbearia.backend.core.exception.BusinessException;
import com.barbearia.backend.core.exception.ResourceNotFoundException;
import com.barbearia.backend.core.ports.incoming.ClienteService;
import com.barbearia.backend.core.ports.outgoing.ClienteRepository;
import com.barbearia.backend.shared.factory.ClienteFactory;
import com.barbearia.backend.shared.prototype.ClientePrototype;
import com.barbearia.backend.shared.visitor.ClienteEstatisticaVisitor;
import com.barbearia.backend.shared.visitor.ClienteLogVisitor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CAMADA DE APLICAÇÃO (SERVIÇO)
 * 
 * Responsabilidade: Orquestrar as regras de negócio
 * 
 * SOLID APLICADO:
 * 
 * 1. SRP (Single Responsibility Principle)
 *    - Cada método tem uma responsabilidade única
 *    - Delega criação para Factory, clonagem para Prototype, etc.
 * 
 * 2. OCP (Open/Closed Principle)
 *    - Podemos adicionar novas funcionalidades sem modificar esta classe
 *    - Ex: novos visitors podem ser adicionados
 * 
 * 3. LSP (Liskov Substitution Principle)
 *    - Usamos interfaces (ClienteRepository, ClienteService)
 *    - Podemos trocar implementações sem quebrar o código
 * 
 * 4. ISP (Interface Segregation Principle)
 *    - Dependemos apenas dos métodos que usamos
 * 
 * 5. DIP (Dependency Inversion Principle)
 *    - Dependemos de abstrações (ports), não de implementações concretas
 *    - Injeção de dependência via construtor (Lombok @RequiredArgsConstructor)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ClienteFactory factory;
    private final ClientePrototype prototype;
    private final ClienteLogVisitor logVisitor;
    private final ClienteEstatisticaVisitor estatisticaVisitor;

    @Override
    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO request) {
        log.info("Criando novo cliente: {}", request.getNome());
        
        // Validação de negócio (SRP - regra de negócio)
        validarTelefoneUnico(request.getTelefone());
        
        // 🏭 Usando Factory para criar a entidade
        Cliente cliente = factory.criarCliente(request);
        
        // Persistência
        Cliente saved = repository.save(cliente);
        
        // 🚶 Usando Visitor para log (auditoria)
        logVisitor.visit(saved);
        
        return toResponseDTO(saved);
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        log.debug("Buscando cliente por ID: {}", id);
        
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
        
        return toResponseDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> listarTodos() {
        log.debug("Listando todos os clientes");
        
        List<Cliente> clientes = repository.findAll();
        
        // 🚶 Usando Visitor para estatísticas (opcional)
        estatisticaVisitor.visitAll(clientes);
        
        return clientes.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO request) {
        log.info("Atualizando cliente ID: {}", id);
        
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
        
        // 📋 Usando Prototype para backup (operação segura)
        Cliente backup = prototype.clonar(cliente);
        
        // Validação: se telefone mudou, verificar se já existe
        if (!cliente.getTelefone().equals(request.getTelefone())) {
            validarTelefoneUnico(request.getTelefone());
        }
        
        // Atualiza dados
        cliente.setNome(request.getNome().trim());
        cliente.setTelefone(request.getTelefone());
        
        Cliente updated = repository.save(cliente);
        
        // Comparação com backup (exemplo de uso do prototype)
        if (!backup.getNome().equals(updated.getNome())) {
            log.debug("Nome alterado de '{}' para '{}'", backup.getNome(), updated.getNome());
        }
        
        return toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando cliente ID: {}", id);
        
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + id);
        }
        
        repository.deleteById(id);
    }

    @Override
    public ClienteResponseDTO buscarPorTelefone(String telefone) {
        log.debug("Buscando cliente por telefone: {}", telefone);
        
        Cliente cliente = repository.findByTelefone(telefone)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com telefone: " + telefone));
        
        return toResponseDTO(cliente);
    }

    /**
     * Regra de negócio: telefone deve ser único
     * SRP: Método privado para validar regra específica
     */
    private void validarTelefoneUnico(String telefone) {
        if (repository.existsByTelefone(telefone)) {
            throw new BusinessException("Já existe um cliente cadastrado com este telefone: " + telefone);
        }
    }

    /**
     * Converte entidade para DTO
     * SRP: Isola a lógica de mapeamento (poderia usar MapStruct)
     */
    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getTelefone(),
            cliente.getDataCriacao(),
            cliente.getUltimoAgendamento()
        );
    }
}