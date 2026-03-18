package com.barbearia.backend.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.barbearia.backend.core.dtos.agenda.AgendaRequestDTO;
import com.barbearia.backend.core.dtos.agenda.AgendaResponseDTO;
import com.barbearia.backend.core.ports.incoming.AgendaService;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    /**
     * CORREÇÃO: Adicionado barbeiroId como parâmetro
     */
    @PostMapping
    public ResponseEntity<AgendaResponseDTO> criar(
            @RequestParam Long barbeiroId,
            @Valid @RequestBody AgendaRequestDTO request) {
        AgendaResponseDTO response = agendaService.criar(barbeiroId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * NOVO: Listar agendas por barbeiro
     */
    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<AgendaResponseDTO>> listarPorBarbeiro(@PathVariable Long barbeiroId) {
        List<AgendaResponseDTO> response = agendaService.listarPorBarbeiro(barbeiroId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> buscarPorId(@PathVariable Long id) {
        AgendaResponseDTO response = agendaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AgendaResponseDTO>> listarTodos() {
        List<AgendaResponseDTO> response = agendaService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dia/{diaSemana}")
    public ResponseEntity<List<AgendaResponseDTO>> listarPorDiaSemana(@PathVariable Integer diaSemana) {
        List<AgendaResponseDTO> response = agendaService.listarPorDiaSemana(diaSemana);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<AgendaResponseDTO>> listarAtivos() {
        List<AgendaResponseDTO> response = agendaService.listarAtivos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendaRequestDTO request) {
        AgendaResponseDTO response = agendaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        agendaService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        agendaService.reativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validar")
    public ResponseEntity<Boolean> validarHorario(
            @RequestParam Integer diaSemana,
            @RequestParam String horaInicio,
            @RequestParam Integer duracaoMinutos) {
        
        boolean valido = agendaService.isHorarioValido(
            diaSemana, 
            java.time.LocalTime.parse(horaInicio), 
            duracaoMinutos
        );
        
        return ResponseEntity.ok(valido);
    }
}