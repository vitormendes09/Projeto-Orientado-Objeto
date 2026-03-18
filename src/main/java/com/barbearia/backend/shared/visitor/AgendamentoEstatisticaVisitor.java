package com.barbearia.backend.shared.visitor;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Agendamento;
import com.barbearia.backend.core.entities.StatusAgendamento;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class AgendamentoEstatisticaVisitor implements AgendamentoVisitor<Map<String, Object>> {

    @Override
    public Map<String, Object> visit(Agendamento agendamento) {
        return Map.of(
            "id", agendamento.getId(),
            "cliente", agendamento.getCliente().getNome(),
            "data", agendamento.getDataHoraInicio(),
            "valor", agendamento.getValorTotal(),
            "status", agendamento.getStatus()
        );
    }

    @Override
    public Map<String, Object> visitAll(List<Agendamento> agendamentos) {
        Map<String, Object> estatisticas = new HashMap<>();
        
        estatisticas.put("total", agendamentos.size());
        
        // Agendamentos por status
        long confirmados = agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONFIRMADO).count();
        long cancelados = agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CANCELADO).count();
        long concluidos = agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO).count();
        
        estatisticas.put("confirmados", confirmados);
        estatisticas.put("cancelados", cancelados);
        estatisticas.put("concluidos", concluidos);
        
        // Faturamento total (apenas confirmados e concluídos)
        Double faturamento = agendamentos.stream()
                .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO)
                .mapToDouble(Agendamento::getValorTotal)
                .sum();
        estatisticas.put("faturamentoTotal", faturamento);
        
        // Média de serviços por agendamento
        double mediaServicos = agendamentos.stream()
                .mapToInt(a -> a.getServicos().size())
                .average()
                .orElse(0.0);
        estatisticas.put("mediaServicosPorAgendamento", mediaServicos);
        
        return estatisticas;
    }
}