package com.barbearia.backend.shared.visitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Agendamento;
import com.barbearia.backend.core.entities.StatusAgendamento;
import java.util.List;

@Slf4j
@Component
public class AgendamentoLogVisitor implements AgendamentoVisitor<String> {

    @Override
    public String visit(Agendamento agendamento) {
        String logMessage = String.format(
            "Agendamento{id=%d, cliente='%s', data=%s, serviços=%d, status=%s, valor=R$%.2f}",
            agendamento.getId(),
            agendamento.getCliente().getNome(),
            agendamento.getDataHoraInicio(),
            agendamento.getServicos().size(),
            agendamento.getStatus(),
            agendamento.getValorTotal()
        );
        
        log.debug(logMessage);
        return logMessage;
    }

    @Override
    public String visitAll(List<Agendamento> agendamentos) {
        StringBuilder sb = new StringBuilder("RELATÓRIO DE AGENDAMENTOS:\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("Total: ").append(agendamentos.size()).append("\n");
        
        long confirmados = agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONFIRMADO).count();
        long cancelados = agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CANCELADO).count();
        long concluidos = agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO).count();
        
        sb.append("Confirmados: ").append(confirmados).append("\n");
        sb.append("Cancelados: ").append(cancelados).append("\n");
        sb.append("Concluídos: ").append(concluidos).append("\n\n");
        
        agendamentos.forEach(a -> sb.append("• ").append(visit(a)).append("\n"));
        
        log.info(sb.toString());
        return sb.toString();
    }
}