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
     * 
     * Exemplo de uso:
     * POST /api/agendamentos?clienteId=1
     * {
     *   "dataHoraInicio": "2025-04-10T14:00:00",
     *   "servicosIds": [1, 2],
     *   "observacoes": "Corte e barba"
     * }
     */
    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(
            @RequestParam Long clienteId,  // ID do cliente na URL
            @Valid @RequestBody AgendamentoRequestDTO request) {
        
        // Define o clienteId no request
        request.setClienteId(clienteId);
        
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
     * Pode filtrar por barbeiro se necessário
     */
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listarTodos(
            @RequestParam(required = false) Long barbeiroId) {
        
        // Se tivesse um método para listar por barbeiro, usaria aqui
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
            @RequestParam(required = false) Long clienteId, // Opcional, para validação futura
            @RequestParam String motivo) {
        
        // TODO: Implementar validação de permissão baseada em clienteId
        AgendamentoResponseDTO response = agendamentoService.cancelar(id, motivo);
        return ResponseEntity.ok(response);
    }

    /**
     * NOVO: Listar agendamentos por barbeiro
     */
    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorBarbeiro(@PathVariable Long barbeiroId) {
        // Este método precisaria ser implementado no service
        List<AgendamentoResponseDTO> response = List.of(); // Placeholder
        return ResponseEntity.ok(response);
    }
}