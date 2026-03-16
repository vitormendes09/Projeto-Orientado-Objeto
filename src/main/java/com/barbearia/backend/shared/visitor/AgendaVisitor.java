package com.barbearia.backend.shared.visitor;

import com.barbearia.backend.core.entities.Agenda;
import java.util.List;

/**
 * PADRÃO VISITOR para Agenda
 * 
 * Responsabilidade: Definir operações aplicáveis a objetos Agenda
 */
public interface AgendaVisitor<T> {
    T visit(Agenda agenda);
    T visitAll(List<Agenda> agendas);
}