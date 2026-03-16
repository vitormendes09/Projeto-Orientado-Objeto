package com.barbearia.backend.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.barbearia.backend.core.dtos.barbeiro.BarbeiroRequestDTO;
import com.barbearia.backend.core.dtos.barbeiro.BarbeiroResponseDTO;
import com.barbearia.backend.core.dtos.barbeiro.BarbeiroLoginDTO;
import com.barbearia.backend.core.ports.incoming.BarbeiroService;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/barbeiros")
@RequiredArgsConstructor
public class BarbeiroController {

    private final BarbeiroService barbeiroService;

    @PostMapping
    public ResponseEntity<BarbeiroResponseDTO> criar(@Valid @RequestBody BarbeiroRequestDTO request) {
        BarbeiroResponseDTO response = barbeiroService.criar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody BarbeiroLoginDTO loginDTO) {
        boolean autenticado = barbeiroService.autenticar(loginDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("autenticado", autenticado);
        
        if (autenticado) {
            BarbeiroResponseDTO barbeiro = barbeiroService.buscarPorEmail(loginDTO.getEmail());
            response.put("barbeiro", barbeiro);
            response.put("mensagem", "Login realizado com sucesso!");
            return ResponseEntity.ok(response);
        } else {
            response.put("mensagem", "Email ou senha inválidos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(@PathVariable Long id) {
        BarbeiroResponseDTO response = barbeiroService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorEmail(@PathVariable String email) {
        BarbeiroResponseDTO response = barbeiroService.buscarPorEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        List<BarbeiroResponseDTO> response = barbeiroService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody BarbeiroRequestDTO request) {
        BarbeiroResponseDTO response = barbeiroService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        barbeiroService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        barbeiroService.reativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        barbeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}