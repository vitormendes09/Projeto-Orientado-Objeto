package com.barbearia.backend.shared.visitor;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Barbeiro;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class BarbeiroEstatisticaVisitor implements BarbeiroVisitor<Map<String, Object>> {

    @Override
    public Map<String, Object> visit(Barbeiro barbeiro) {
        return Map.of(
            "id", barbeiro.getId(),
            "nome", barbeiro.getNome(),
            "email", barbeiro.getEmail(),
            "ativo", barbeiro.getAtivo()
        );
    }

    @Override
    public Map<String, Object> visitAll(List<Barbeiro> barbeiros) {
        Map<String, Object> estatisticas = new HashMap<>();
        
        estatisticas.put("totalBarbeiros", barbeiros.size());
        
        long ativos = barbeiros.stream().filter(Barbeiro::getAtivo).count();
        estatisticas.put("barbeirosAtivos", ativos);
        estatisticas.put("barbeirosInativos", barbeiros.size() - ativos);
        
        return estatisticas;
    }
}