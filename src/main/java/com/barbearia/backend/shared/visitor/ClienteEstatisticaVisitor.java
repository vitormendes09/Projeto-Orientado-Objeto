package com.barbearia.backend.shared.visitor;


import org.springframework.stereotype.Component;

import com.barbearia.backend.core.entities.Cliente;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 🚶 Visitor para calcular estatísticas de clientes
 * Útil para o barbeiro ver métricas mensais
 */
@Component
public class ClienteEstatisticaVisitor implements ClienteVisitor<Map<String, Object>> {

    @Override
    public Map<String, Object> visit(Cliente cliente) {
        return Map.of(
            "id", cliente.getId(),
            "nome", cliente.getNome(),
            "telefone", cliente.getTelefone(),
            "ultimoAgendamento", cliente.getUltimoAgendamento()
        );
    }

    @Override
    public Map<String, Object> visitAll(List<Cliente> clientes) {
        return Map.of(
            "totalClientes", clientes.size(),
            "agendamentosHoje", contarAgendamentosHoje(clientes),
            "clientesAtivos", contarClientesAtivosUltimoMes(clientes)
        );
    }

    private long contarAgendamentosHoje(List<Cliente> clientes) {
        LocalDate hoje = LocalDate.now();
        return clientes.stream()
                .filter(c -> c.getUltimoAgendamento() != null)
                .filter(c -> c.getUltimoAgendamento().toLocalDate().equals(hoje))
                .count();
    }

    private long contarClientesAtivosUltimoMes(List<Cliente> clientes) {
        LocalDate mesPassado = LocalDate.now().minusMonths(1);
        return clientes.stream()
                .filter(c -> c.getUltimoAgendamento() != null)
                .filter(c -> c.getUltimoAgendamento().toLocalDate().isAfter(mesPassado))
                .count();
    }
}