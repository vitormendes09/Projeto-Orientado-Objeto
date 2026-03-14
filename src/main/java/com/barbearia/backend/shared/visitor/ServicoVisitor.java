package com.barbearia.backend.shared.visitor;


import java.util.List;

import com.barbearia.backend.core.entities.Servico;

/**
 * PADRÃO VISITOR (INTERFACE)
 * 
 * Responsabilidade: Definir operações que podem ser aplicadas a Services.
 * 
 * Quando usar:
 * - Para gerar relatórios de serviços
 * - Para exportar dados de serviços
 * - Para calcular estatísticas
 */
public interface ServicoVisitor<T> {
    
    /**
     * Visita um serviço individual
     */
    T visit(Servico service);
    
    /**
     * Visita uma coleção de serviços
     */
    T visitAll(List<Servico> services);
}