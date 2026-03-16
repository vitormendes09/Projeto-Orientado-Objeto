package com.barbearia.backend.shared.factory;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.dtos.agenda.AgendaRequestDTO;
import com.barbearia.backend.core.entities.Agenda;

/**
 * PADRÃO FACTORY para Agenda
 * 
 * Responsabilidade: Encapsular a criação de objetos Agenda
 */
@Component
public class AgendaFactory {

    /**
     * Criar nova agenda a partir do DTO
     */
    public Agenda criarAgenda(AgendaRequestDTO request) {
        return Agenda.builder()
                .diaSemana(request.getDiaSemana())
                .horaInicio(request.getHoraInicio())
                .horaFim(request.getHoraFim())
                .ativo(request.getAtivo() != null ? request.getAtivo() : true)
                .build();
    }

    /**
     * Atualizar agenda existente
     */
    public Agenda atualizarAgenda(Agenda agendaExistente, AgendaRequestDTO request) {
        agendaExistente.setDiaSemana(request.getDiaSemana());
        agendaExistente.setHoraInicio(request.getHoraInicio());
        agendaExistente.setHoraFim(request.getHoraFim());
        
        if (request.getAtivo() != null) {
            agendaExistente.setAtivo(request.getAtivo());
        }
        
        return agendaExistente;
    }

    /**
     * Criar agenda com horário padrão (útil para testes)
     */
    public Agenda criarAgendaPadrao(Integer diaSemana) {
        return Agenda.builder()
                .diaSemana(diaSemana)
                .horaInicio(java.time.LocalTime.of(9, 0))
                .horaFim(java.time.LocalTime.of(18, 0))
                .ativo(true)
                .build();
    }
}