package com.barbearia.backend.shared.visitor;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Servico;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

/**
 * Visitor para calcular estatísticas de serviços
 * Útil para o barbeiro entender quais serviços estão vendendo mais
 */
@Component
public class ServicoEstatisticaVisitor implements ServicoVisitor<Map<String, Object>> {

    @Override
    public Map<String, Object> visit(Servico service) {
        return Map.of(
            "id", service.getId(),
            "nome", service.getNome(),
            "preco", service.getPreco(),
            "duracao", service.getDuracaoMinutos(),
            "agendamentos", service.getTotalAgendamentos(),
            "faturamento", service.getPreco().multiply(BigDecimal.valueOf(service.getTotalAgendamentos()))
        );
    }

    @Override
    public Map<String, Object> visitAll(List<Servico> services) {
        Map<String, Object> estatisticas = new HashMap<>();
        
        // Estatísticas básicas
        estatisticas.put("totalServicos", services.size());
        
        // Serviços ativos vs inativos
        long ativos = services.stream().filter(Servico::getAtivo).count();
        estatisticas.put("servicosAtivos", ativos);
        estatisticas.put("servicosInativos", services.size() - ativos);
        
        // Preços
        Optional<BigDecimal> precoMedio = services.stream()
                .map(Servico::getPreco)
                .reduce(BigDecimal::add)
                .map(total -> total.divide(BigDecimal.valueOf(services.size()), BigDecimal.ROUND_HALF_EVEN));
        
        estatisticas.put("precoMedio", precoMedio.orElse(BigDecimal.ZERO));
        
        // Serviço mais caro
        Optional<Servico> maisCaro = services.stream()
                .max(Comparator.comparing(Servico::getPreco));
        maisCaro.ifPresent(s -> estatisticas.put("servicoMaisCaro", s.getNome()));
        
        // Serviço mais popular (mais agendamentos)
        Optional<Servico> maisPopular = services.stream()
                .max(Comparator.comparing(Servico::getTotalAgendamentos));
        maisPopular.ifPresent(s -> {
            estatisticas.put("servicoMaisPopular", s.getNome());
            estatisticas.put("totalAgendamentosMaisPopular", s.getTotalAgendamentos());
        });
        
        // Duração média
        double duracaoMedia = services.stream()
                .mapToInt(Servico::getDuracaoMinutos)
                .average()
                .orElse(0.0);
        estatisticas.put("duracaoMediaMinutos", duracaoMedia);
        
        return estatisticas;
    }
}