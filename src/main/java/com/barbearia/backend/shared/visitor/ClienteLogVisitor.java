package com.barbearia.backend.shared.visitor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.barbearia.backend.core.entities.Cliente;

import java.util.List;

/**
 *  PADRÃO VISITOR (IMPLEMENTAÇÃO CONCRETA)
 * 
 * Responsabilidade: Gerar logs estruturados de clientes.
 * Útil para auditoria e debug.
 */

@Slf4j
@Component
public class ClienteLogVisitor implements ClienteVisitor<String> {

    @Override
    public String visit(Cliente cliente) {
        String logMessage = String.format(
            "Cliente{id=%d, nome='%s', telefone='%s', criado=%s}",
            cliente.getId(),
            cliente.getNome(),
            cliente.getTelefone(),
            cliente.getDataCriacao()
        );
        
        log.debug(logMessage);
        return logMessage;
    }

    @Override
    public String visitAll(List<Cliente> clientes) {
        StringBuilder sb = new StringBuilder("Relatório de Clientes:\n");
        sb.append("Total: ").append(clientes.size()).append("\n");
        
        clientes.forEach(c -> {
            sb.append("- ").append(visit(c)).append("\n");
        });
        
        log.info(sb.toString());
        return sb.toString();
    }
}