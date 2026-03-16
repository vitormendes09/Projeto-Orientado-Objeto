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

/**
 * CAMADA DE APRESENTAÇÃO (CONTROLLER) para Agenda
 * 
 * RF01 – Barbeiro define horário de funcionamento
 * Endpoints: /api/agendas
 */
@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    /**
     * RF01.1 – Cadastrar horário de funcionamento
     * 
     * Exemplo de request:
     * {
     *   "diaSemana": 2,
     *   "horaInicio": "09:00",
     *   "horaFim": "18:00",
     *   "ativo": true
     * }
     */
    @PostMapping
    public ResponseEntity<AgendaResponseDTO> criar(@Valid @RequestBody AgendaRequestDTO request) {
        AgendaResponseDTO response = agendaService.criar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Buscar configuração por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> buscarPorId(@PathVariable Long id) {
        AgendaResponseDTO response = agendaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Listar todas as configurações
     */
    @GetMapping
    public ResponseEntity<List<AgendaResponseDTO>> listarTodos() {
        List<AgendaResponseDTO> response = agendaService.listarTodos();
        return ResponseEntity.ok(response);
    }

    /**
     * Listar configurações por dia da semana
     * Exemplo: /api/agendas/dia/2 (para terça-feira)
     */
    @GetMapping("/dia/{diaSemana}")
    public ResponseEntity<List<AgendaResponseDTO>> listarPorDiaSemana(@PathVariable Integer diaSemana) {
        List<AgendaResponseDTO> response = agendaService.listarPorDiaSemana(diaSemana);
        return ResponseEntity.ok(response);
    }

    /**
     * Listar apenas configurações ativas
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<AgendaResponseDTO>> listarAtivos() {
        List<AgendaResponseDTO> response = agendaService.listarAtivos();
        return ResponseEntity.ok(response);
    }

    /**
     * RF01.2 – Atualizar horário de funcionamento
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendaRequestDTO request) {
        AgendaResponseDTO response = agendaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Desativar configuração (soft delete)
     */
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        agendaService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reativar configuração
     */
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        agendaService.reativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletar configuração (hard delete - apenas se não houver agendamentos)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint auxiliar para verificar se um horário está disponível
     * Útil para o frontend validar antes de tentar agendar
     */
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