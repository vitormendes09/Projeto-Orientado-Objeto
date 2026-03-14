package com.barbearia.backend.shared.visitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Servico;
import java.util.List;

/**
 * Visitor para gerar logs estruturados de serviços
 * Útil para auditoria e debug
 */
@Slf4j
@Component
public class ServicoLogVisitor implements ServicoVisitor<String> {

    @Override
    public String visit(Servico service) {
        String logMessage = String.format(
            "Service{id=%d, nome='%s', preço=R$%.2f, duração=%dmin, ativo=%s, agendamentos=%d}",
            service.getId(),
            service.getNome(),
            service.getPreco(),
            service.getDuracaoMinutos(),
            service.getAtivo() ? "sim" : "não",
            service.getTotalAgendamentos()
        );
        
        log.debug(logMessage);
        return logMessage;
    }

    @Override
    public String visitAll(List<Servico> services) {
        StringBuilder sb = new StringBuilder("📋 RELATÓRIO DE SERVIÇOS:\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("Total de serviços: ").append(services.size()).append("\n");
        
        long ativos = services.stream().filter(Servico::getAtivo).count();
        sb.append("Ativos: ").append(ativos).append("\n");
        sb.append("Inativos: ").append(services.size() - ativos).append("\n\n");
        
        services.forEach(s -> {
            sb.append("• ").append(visit(s)).append("\n");
        });
        
        log.info(sb.toString());
        return sb.toString();
    }
}