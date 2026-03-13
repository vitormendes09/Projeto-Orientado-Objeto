package com.barbearia.backend.shared.factory;


import org.springframework.stereotype.Component;

import com.barbearia.backend.core.dtos.ClienteRequestDTO;
import com.barbearia.backend.core.entities.Cliente;

/**
 * PADRÃO FACTORY
 * 
 * Responsabilidade: Encapsular a criação de objetos Cliente.
 * 
 * SOLID:
 * - SRP: Única responsabilidade é criar Clientes
 * - OCP: Podemos estender com novos tipos de criação sem modificar
 * - DIP: Depende de abstrações (DTO), não de implementações concretas
 * 
 * Benefícios:
 * - Centraliza a lógica de criação
 * - Desacopla o cliente do código de criação
 * - Facilita testes (podemos mockar a factory)
 */

@Component
public class ClienteFactory {

    /**
     * Cria um novo Cliente a partir de um DTO de requisição
     * @param request DTO com dados do cliente
     * @return Entidade Cliente pronta para persistência
     */
    public Cliente criarCliente(ClienteRequestDTO request) {
        return Cliente.builder()
                .nome(request.getNome().trim())
                .telefone(limparTelefone(request.getTelefone()))
                .build();
    }

    /**
     * Cria um cliente apenas com telefone (útil para buscas rápidas)
     * @param telefone Telefone do cliente
     * @return Cliente parcialmente preenchido
     */
    public Cliente criarClienteComTelefone(String telefone) {
        return Cliente.builder()
                .telefone(limparTelefone(telefone))
                .build();
    }

    /**
     * Método privado para padronizar formato do telefone
     * SRP: Cada método tem uma responsabilidade única
     */
    private String limparTelefone(String telefone) {
        return telefone.replaceAll("[^0-9]", "");
    }
}