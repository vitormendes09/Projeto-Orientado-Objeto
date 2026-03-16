package com.barbearia.backend.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.barbearia.backend.core.dtos.agendamento.AgendamentoRequestDTO;
import com.barbearia.backend.core.dtos.agendamento.AgendamentoResponseDTO;
import com.barbearia.backend.core.dtos.agendamento.DisponibilidadeResponseDTO;
import com.barbearia.backend.core.ports.incoming.AgendamentoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    /**
     * RF04.2 – Cliente pode realizar agendamento
     */
    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(@Valid @RequestBody AgendamentoRequestDTO request) {
        AgendamentoResponseDTO response = agendamentoService.criar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Buscar agendamento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        AgendamentoResponseDTO response = agendamentoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * RF05.1 – Barbeiro pode visualizar todos os agendamentos
     */
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listarTodos() {
        List<AgendamentoResponseDTO> response = agendamentoService.listarTodos();
        return ResponseEntity.ok(response);
    }

    /**
     * RF05.1 – Filtrar por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorPeriodo(
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim) {
        List<AgendamentoResponseDTO> response = agendamentoService.listarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(response);
    }

    /**
     * RF05.1 – Filtrar por cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<AgendamentoResponseDTO> response = agendamentoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(response);
    }

    /**
     * RF04.1 – Cliente pode visualizar agenda disponível
     */
    @GetMapping("/disponibilidade")
    public ResponseEntity<DisponibilidadeResponseDTO> verificarDisponibilidade(
            @RequestParam LocalDate data,
            @RequestParam Long servicoId) {
        DisponibilidadeResponseDTO response = agendamentoService.verificarDisponibilidade(data, servicoId);
        return ResponseEntity.ok(response);
    }

    /**
     * Visualizar disponibilidade da semana
     */
    @GetMapping("/disponibilidade/semana")
    public ResponseEntity<List<DisponibilidadeResponseDTO>> verificarDisponibilidadeSemana(
            @RequestParam LocalDate dataInicio) {
        List<DisponibilidadeResponseDTO> response = agendamentoService.verificarDisponibilidadeSemana(dataInicio);
        return ResponseEntity.ok(response);
    }

    /**
     * RF05.2 – Cancelar agendamento
     * Tanto barbeiro quanto cliente podem cancelar (RN04)
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(
            @PathVariable Long id,
            @RequestParam String motivo) {
        AgendamentoResponseDTO response = agendamentoService.cancelar(id, motivo);
        return ResponseEntity.ok(response);
    }
}