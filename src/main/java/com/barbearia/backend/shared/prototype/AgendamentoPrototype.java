package com.barbearia.backend.shared.prototype;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Agendamento;
import com.barbearia.backend.core.entities.StatusAgendamento;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class AgendamentoPrototype {

    /**
     * Clona um agendamento existente (útil para backup antes de cancelar)
     */
    public Agendamento clonar(Agendamento original) {
        if (original == null) {
            return null;
        }

        Agendamento clone = Agendamento.builder()
                .cliente(original.getCliente())
                .servicos(new ArrayList<>(original.getServicos())) // Nova lista
                .dataHoraInicio(original.getDataHoraInicio())
                .dataHoraFim(original.getDataHoraFim())
                .status(original.getStatus())
                .valorTotal(original.getValorTotal())
                .observacoes(original.getObservacoes())
                .build();

        return clone;
    }

    /**
     * Clona um agendamento para reagendamento (mantém cliente e serviços)
     */
    public Agendamento clonarParaReagendar(Agendamento original, LocalDateTime novoHorario) {
        Agendamento clone = clonar(original);
        clone.setDataHoraInicio(novoHorario);
        clone.setDataHoraFim(novoHorario.plusMinutes(
                original.calcularDuracaoTotal()));
        clone.setStatus(StatusAgendamento.CONFIRMADO);
        return clone;
    }
}