package com.barbearia.backend.shared.prototype;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Agenda;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PADRÃO PROTOTYPE para Agenda
 * 
 * Responsabilidade: Permitir clonagem de objetos Agenda
 * Útil para backup antes de atualizações
 */
@Component
public class AgendaPrototype {

    /**
     * Clonar uma agenda existente
     */
    public Agenda clonar(Agenda original) {
        if (original == null) {
            return null;
        }

        return Agenda.builder()
                .diaSemana(original.getDiaSemana())
                .horaInicio(original.getHoraInicio())
                .horaFim(original.getHoraFim())
                .ativo(original.getAtivo())
                .build();
        // ID e dataCriacao NÃO são copiados
    }

    /**
     * Clonar lista de agendas
     */
    public List<Agenda> clonarLista(List<Agenda> agendas) {
        return agendas.stream()
                .map(this::clonar)
                .collect(Collectors.toList());
    }
}