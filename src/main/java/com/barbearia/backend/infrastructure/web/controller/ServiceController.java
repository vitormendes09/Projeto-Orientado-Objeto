package com.barbearia.backend.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.barbearia.backend.core.dtos.servicos.ServiceRequestDTO;
import com.barbearia.backend.core.dtos.servicos.ServiceResponseDTO;
import com.barbearia.backend.core.ports.incoming.ServiceService;

import java.util.List;

/**
 * CAMADA DE APRESENTAÇÃO (CONTROLLER)
 * 
 * Responsabilidade: Receber requisições HTTP e delegar para o serviço
 * Endpoints: /api/servicos (em português para facilitar entendimento)
 */
@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * RF02.1 – Barbeiro pode cadastrar serviços
     */
    @PostMapping
    public ResponseEntity<ServiceResponseDTO> criar(@Valid @RequestBody ServiceRequestDTO request) {
        ServiceResponseDTO response = serviceService.criar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Buscar serviço por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponseDTO> buscarPorId(@PathVariable Long id) {
        ServiceResponseDTO response = serviceService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Buscar serviço por nome
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<ServiceResponseDTO> buscarPorNome(@PathVariable String nome) {
        ServiceResponseDTO response = serviceService.buscarPorNome(nome);
        return ResponseEntity.ok(response);
    }

    /**
     * RF02.3 – Sistema deve exibir serviços disponíveis para clientes
     * Listar apenas serviços ativos (para clientes)
     */
    @GetMapping("/disponiveis")
    public ResponseEntity<List<ServiceResponseDTO>> listarDisponiveis() {
        List<ServiceResponseDTO> response = serviceService.listarAtivos();
        return ResponseEntity.ok(response);
    }

    /**
     * Listar todos os serviços (para o barbeiro, incluindo inativos)
     */
    @GetMapping
    public ResponseEntity<List<ServiceResponseDTO>> listarTodos() {
        List<ServiceResponseDTO> response = serviceService.listarTodos();
        return ResponseEntity.ok(response);
    }

    /**
     * RF02.2 – Barbeiro pode editar serviços
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponseDTO> atualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ServiceRequestDTO request) {
        ServiceResponseDTO response = serviceService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * RF02.2 – Barbeiro pode excluir serviços (hard delete)
     * CUIDADO: Isso remove permanentemente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        serviceService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Soft delete - apenas desativa o serviço (recomendado)
     * RN05 - Serviços inativos não aparecem para clientes
     */
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        serviceService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reativar um serviço desativado
     */
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        serviceService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}