package com.barbearia.backend.core.ports.incoming;

import com.barbearia.backend.core.dtos.agenda.AgendaRequestDTO;
import com.barbearia.backend.core.dtos.agenda.AgendaResponseDTO;

import java.time.LocalTime;
import java.util.List;

public interface AgendaService {
    
    /**
     * RF01.1 – Cadastrar horário de funcionamento para um dia da semana
     */
    AgendaResponseDTO criar(Long barbeiroId, AgendaRequestDTO request);
    
    /**
     * Buscar configuração de agenda por ID
     */
    AgendaResponseDTO buscarPorId(Long id);
    
    /**
     * Listar todas as configurações de agenda
     */
    List<AgendaResponseDTO> listarTodos();
    
    /**
     * Listar configurações por barbeiro
     */
    List<AgendaResponseDTO> listarPorBarbeiro(Long barbeiroId); // NOVO
    
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
     * Validar se um horário está dentro do expediente
     */
    boolean isHorarioValido(Integer diaSemana, LocalTime horaInicio, int duracaoMinutos);
}