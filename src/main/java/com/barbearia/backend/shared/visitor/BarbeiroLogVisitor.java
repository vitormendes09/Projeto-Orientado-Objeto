package com.barbearia.backend.shared.visitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Barbeiro;
import java.util.List;

@Slf4j
@Component
public class BarbeiroLogVisitor implements BarbeiroVisitor<String> {

    @Override
    public String visit(Barbeiro barbeiro) {
        String logMessage = String.format(
            "Barbeiro{id=%d, nome='%s', email='%s', telefone='%s', ativo=%s}",
            barbeiro.getId(),
            barbeiro.getNome(),
            barbeiro.getEmail(),
            barbeiro.getTelefone(),
            barbeiro.getAtivo() ? "" : ""
        );
        
        log.debug(logMessage);
        return logMessage;
    }

    @Override
    public String visitAll(List<Barbeiro> barbeiros) {
        StringBuilder sb = new StringBuilder("RELATÓRIO DE BARBEIROS:\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("Total: ").append(barbeiros.size()).append("\n");
        
        long ativos = barbeiros.stream().filter(Barbeiro::getAtivo).count();
        sb.append("Ativos: ").append(ativos).append("\n");
        sb.append("Inativos: ").append(barbeiros.size() - ativos).append("\n\n");
        
        barbeiros.forEach(b -> sb.append("• ").append(visit(b)).append("\n"));
        
        log.info(sb.toString());
        return sb.toString();
    }
}