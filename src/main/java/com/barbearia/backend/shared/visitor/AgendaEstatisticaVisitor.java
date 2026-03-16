package com.barbearia.backend.shared.visitor;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Agenda;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class AgendaEstatisticaVisitor implements AgendaVisitor<Map<String, Object>> {

    @Override
    public Map<String, Object> visit(Agenda agenda) {
        return Map.of(
            "id", agenda.getId(),
            "diaSemana", agenda.getDiaSemana(),
            "horaInicio", agenda.getHoraInicio().toString(),
            "horaFim", agenda.getHoraFim().toString(),
            "ativo", agenda.getAtivo()
        );
    }

    @Override
    public Map<String, Object> visitAll(List<Agenda> agendas) {
        Map<String, Object> estatisticas = new HashMap<>();
        
        estatisticas.put("totalConfiguracoes", agendas.size());
        
        long ativas = agendas.stream().filter(Agenda::getAtivo).count();
        estatisticas.put("configuracoesAtivas", ativas);
        estatisticas.put("configuracoesInativas", agendas.size() - ativas);
        
        // Dias da semana cobertos
        long diasCobertos = agendas.stream()
                .map(Agenda::getDiaSemana)
                .distinct()
                .count();
        estatisticas.put("diasCobertos", diasCobertos);
        estatisticas.put("diasSemCobertura", 7 - diasCobertos);
        
        // Média de horas por dia
        double mediaHoras = agendas.stream()
                .filter(Agenda::getAtivo)
                .mapToDouble(a -> java.time.Duration.between(a.getHoraInicio(), a.getHoraFim()).toHours())
                .average()
                .orElse(0.0);
        estatisticas.put("mediaHorasPorDia", Math.round(mediaHoras * 10) / 10.0);
        
        return estatisticas;
    }
}
