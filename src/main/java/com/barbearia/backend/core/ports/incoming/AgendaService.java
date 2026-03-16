package com.barbearia.backend.core.ports.incoming;

import com.barbearia.backend.core.dtos.agenda.AgendaRequestDTO;
import com.barbearia.backend.core.dtos.agenda.AgendaResponseDTO;

import java.time.LocalTime;
import java.util.List;

/**
 * PORTA DE ENTRADA (INCOMING PORT) para Agenda
 * 
 * Responsabilidade: Definir as operações de negócio disponíveis para gerenciamento de agenda
 * 
 * RF01 – Barbeiro define horário de funcionamento
 * RN01 – Sistema só permite agendamentos dentro deste horário
 */
public interface AgendaService {
    
    /**
     * RF01.1 – Cadastrar horário de funcionamento para um dia da semana
     */
    AgendaResponseDTO criar(AgendaRequestDTO request);
    
    /**
     * Buscar configuração de agenda por ID
     */
    AgendaResponseDTO buscarPorId(Long id);
    
    /**
     * Listar todas as configurações de agenda
     */
    List<AgendaResponseDTO> listarTodos();
    
    /**
     * Listar configurações por dia da semana
     */
    List<AgendaResponseDTO> listarPorDiaSemana(Integer diaSemana);
    
    /**
     * Listar apenas configurações ativas
     */
    List<AgendaResponseDTO> listarAtivos();
    
    /**
     * RF01.2 – Atualizar horário de funcionamento
     */
    AgendaResponseDTO atualizar(Long id, AgendaRequestDTO request);
    
    /**
     * Ativar/Desativar uma configuração de agenda (soft delete)
     */
    void desativar(Long id);
    
    /**
     * Reativar uma configuração de agenda
     */
    void reativar(Long id);
    
    /**
     * Remover configuração (hard delete - apenas se não houver agendamentos)
     */
    void deletar(Long id);
    
    /**
     * Validar se um horário está dentro do expediente (usado por AgendamentoService)
     */
    boolean isHorarioValido(Integer diaSemana, LocalTime horaInicio, int duracaoMinutos);
}