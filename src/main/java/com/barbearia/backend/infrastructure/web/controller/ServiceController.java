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

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * RF02.1 – Barbeiro pode cadastrar serviços
     * CORREÇÃO: Adicionado @RequestParam barbeiroId
     */
    @PostMapping
    public ResponseEntity<ServiceResponseDTO> criar(
            @RequestParam Long barbeiroId,  // <-- ADICIONADO
            @Valid @RequestBody ServiceRequestDTO request) {
        ServiceResponseDTO response = serviceService.criar(barbeiroId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * NOVO: Listar serviços por barbeiro
     */
    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<ServiceResponseDTO>> listarPorBarbeiro(@PathVariable Long barbeiroId) {
        List<ServiceResponseDTO> response = serviceService.listarPorBarbeiro(barbeiroId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponseDTO> buscarPorId(@PathVariable Long id) {
        ServiceResponseDTO response = serviceService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<ServiceResponseDTO> buscarPorNome(@PathVariable String nome) {
        ServiceResponseDTO response = serviceService.buscarPorNome(nome);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<ServiceResponseDTO>> listarDisponiveis() {
        List<ServiceResponseDTO> response = serviceService.listarAtivos();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponseDTO>> listarTodos() {
        List<ServiceResponseDTO> response = serviceService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponseDTO> atualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ServiceRequestDTO request) {
        ServiceResponseDTO response = serviceService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        serviceService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        serviceService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        serviceService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}