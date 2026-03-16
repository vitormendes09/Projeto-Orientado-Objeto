package com.barbearia.backend.shared.visitor;

import com.barbearia.backend.core.entities.Agendamento;
import java.util.List;

public interface AgendamentoVisitor<T> {
    T visit(Agendamento agendamento);
    T visitAll(List<Agendamento> agendamentos);
}