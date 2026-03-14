package com.barbearia.backend.shared.prototype;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barbearia.backend.core.entities.Cliente;

/**
 * PADRÃO PROTOTYPE
 * 
 * Responsabilidade: Permitir a clonagem de objetos Cliente.
 * 
 * SOLID:
 * - SRP: Responsável apenas por criar cópias
 * - OCP: Novos tipos de clonagem podem ser adicionados
 * 
 * Quando usar:
 * - Quando a criação de um objeto é mais cara que cloná-lo
 * - Quando queremos evitar chamadas repetidas ao banco
 * - Quando precisamos de cópias independentes (evitar referências compartilhadas)
 * 
 * Caso de uso no projeto:
 * - Ao editar um cliente, podemos clonar o original para comparar mudanças
 * - Para criar um novo agendamento, podemos clonar dados do cliente anterior
 */
@Component
public class ClientePrototype {

    /**
     * Clona um cliente existente
     * @param original Cliente a ser clonado
     * @return Novo cliente com os mesmos dados, mas sem ID e datas
     */
    public Cliente clonar(Cliente original) {
        if (original == null) {
            return null;
        }

        return Cliente.builder()
                .nome(original.getNome())
                .telefone(original.getTelefone())
                .build();
        // Note: ID, dataCriacao e ultimoAgendamento NÃO são copiados
        // Isso garante que o clone é uma nova entidade independente
    }

    /**
     * Clona um cliente mas permite modificar o telefone
     * @param original Cliente original
     * @param novoTelefone Telefone para o clone
     * @return Cliente clonado com telefone atualizado
     */
    public Cliente clonarComNovoTelefone(Cliente original, String novoTelefone) {
        Cliente clone = clonar(original);
        clone.setTelefone(novoTelefone);
        return clone;
    }

    /**
     * Cria uma cópia profunda (deep copy) de uma lista de clientes
     * @param clientes Lista original
     * @return Lista com clones independentes
     */
    public List<Cliente> clonarLista(List<Cliente> clientes) {
        return clientes.stream()
                .map(this::clonar)
                .collect(Collectors.toList());
    }
}