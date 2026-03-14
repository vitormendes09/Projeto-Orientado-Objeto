package com.barbearia.backend.shared.visitor;

import java.util.List;

import com.barbearia.backend.core.entities.Cliente;

/**
 * PADRÃO VISITOR (INTERFACE)
 * 
 * Responsabilidade: Definir operações que podem ser aplicadas a Clientes.
 * 
 * SOLID:
 * - OCP: Novas operações podem ser adicionadas sem modificar as classes visitadas
 * - SRP: Cada visitante tem uma responsabilidade específica
 * 
 * Quando usar:
 * - Quando precisamos executar operações não relacionadas em objetos
 * - Quando as operações mudam com frequência
 * - Para separar algoritmos dos objetos onde operam
 * 
 * Exemplos de uso no sistema:
 * - Gerar relatórios
 * - Exportar dados
 * - Validar regras complexas
 * - Calcular métricas (ex: quantidade de cortes por mês)
 */

public interface ClienteVisitor<T> {
    
    /**
     * Visita um cliente individual
     * @param cliente Cliente sendo visitado
     * @return Resultado da operação (genérico para flexibilidade)
     */
    T visit(Cliente cliente);
    
    /**
     * Visita uma coleção de clientes
     * @param clientes Lista de clientes
     * @return Resultado agregado
     */
    T visitAll(List<Cliente> clientes);
}