package com.barbearia.backend.shared.prototype;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Servico;
import com.barbearia.backend.core.entities.Servico;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PADRÃO PROTOTYPE
 * 
 * Responsabilidade: Permitir a clonagem de objetos Service.
 * 
 * Casos de uso:
 * - Fazer backup antes de atualizações
 * - Criar serviços similares (ex: "Corte" e "Corte Infantil")
 * - Comparar mudanças
 */
@Component
public class ServicoPrototype {

    /**
     * Clona um serviço existente
     * @param original Serviço a ser clonado
     * @return Novo serviço com os mesmos dados, mas sem ID e com nome marcado como cópia
     */
    public Servico clonar(Servico original) {
        if (original == null) {
            return null;
        }

        return Servico.builder()
                .nome(original.getNome() + " (cópia)")
                .descricao(original.getDescricao())
                .preco(original.getPreco())
                .duracaoMinutos(original.getDuracaoMinutos())
                .ativo(true)  // Cópia sempre começa ativa
                .totalAgendamentos(0)  // Zera o contador
                .build();
        // ID, dataCriacao, dataAtualizacao NÃO são copiados
    }

    /**
     * Clona um serviço com novo nome (útil para variações)
     */
    public Servico clonarComNovoNome(Servico original, String novoNome) {
        Servico clone = clonar(original);
        clone.setNome(novoNome);
        return clone;
    }

    /**
     * Clona um serviço com novo preço (útil para promoções)
     */
    public Servico clonarComNovoPreco(Servico original, java.math.BigDecimal novoPreco) {
        Servico clone = clonar(original);
        clone.setPreco(novoPreco);
        return clone;
    }

    /**
     * Cria uma cópia profunda de uma lista de serviços
     */
    public List<Servico> clonarLista(List<Servico> services) {
        return services.stream()
                .map(this::clonar)
                .collect(Collectors.toList());
    }
}