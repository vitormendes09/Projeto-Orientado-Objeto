package com.barbearia.backend.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.barbearia.backend.core.dtos.ClienteRequestDTO;
import com.barbearia.backend.core.dtos.ClienteResponseDTO;
import com.barbearia.backend.core.ports.incoming.ClienteService;

import java.util.List;

/**
 * CAMADA DE APRESENTAÇÃO (CONTROLLER)
 * 
 * Responsabilidade: Receber requisições HTTP e delegar para o serviço
 * 
 * SOLID:
 * - SRP: Apenas lida com HTTP, não com regras de negócio
 * - DIP: Depende da abstração ClienteService, não da implementação
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO response = clienteService.criar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO response = clienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> response = clienteService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/telefone/{telefone}")
    public ResponseEntity<ClienteResponseDTO> buscarPorTelefone(@PathVariable String telefone) {
        ClienteResponseDTO response = clienteService.buscarPorTelefone(telefone);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO response = clienteService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}