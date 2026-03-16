package com.barbearia.backend.shared.visitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Agenda;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AgendaLogVisitor implements AgendaVisitor<String> {

    private static final Map<Integer, String> DIAS_SEMANA = Map.of(
        1, "Segunda", 2, "Terça", 3, "Quarta", 4, "Quinta",
        5, "Sexta", 6, "Sábado", 7, "Domingo"
    );

    @Override
    public String visit(Agenda agenda) {
        String dia = DIAS_SEMANA.getOrDefault(agenda.getDiaSemana(), "Dia " + agenda.getDiaSemana());
        
        String logMessage = String.format(
            "Agenda{id=%d, dia=%s (%d), horário=%s-%s, ativo=%s}",
            agenda.getId(),
            dia,
            agenda.getDiaSemana(),
            agenda.getHoraInicio(),
            agenda.getHoraFim(),
            agenda.getAtivo() ? "✅" : "❌"
        );
        
        log.debug(logMessage);
        return logMessage;
    }

    @Override
    public String visitAll(List<Agenda> agendas) {
        StringBuilder sb = new StringBuilder("📅 RELATÓRIO DE AGENDA:\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("Total de configurações: ").append(agendas.size()).append("\n");
        
        long ativas = agendas.stream().filter(Agenda::getAtivo).count();
        sb.append("Ativas: ").append(ativas).append("\n");
        sb.append("Inativas: ").append(agendas.size() - ativas).append("\n\n");
        
        // Agrupar por dia da semana
        Map<Integer, List<Agenda>> porDia = agendas.stream()
                .collect(java.util.stream.Collectors.groupingBy(Agenda::getDiaSemana));
        
        for (int dia = 1; dia <= 7; dia++) {
            List<Agenda> doDia = porDia.getOrDefault(dia, List.of());
            String nomeDia = DIAS_SEMANA.getOrDefault(dia, "");
            
            sb.append(nomeDia).append(" (").append(dia).append("): ");
            
            if (doDia.isEmpty()) {
                sb.append("Sem configuração\n");
            } else {
                sb.append(doDia.size()).append(" configuração(ões)\n");
                doDia.forEach(a -> sb.append("  • ").append(visit(a)).append("\n"));
            }
        }
        
        log.info(sb.toString());
        return sb.toString();
    }
}