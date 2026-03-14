package com.barbearia.backend.shared.factory;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.dtos.servicos.ServiceRequestDTO;
import com.barbearia.backend.core.entities.Servico;  // Import corrigido

@Component
public class ServicoFactory {

    public Servico criarServico(ServiceRequestDTO request) {
        return Servico.builder()
                .nome(capitalizarNome(request.getNome().trim()))
                .descricao(request.getDescricao() != null ? request.getDescricao().trim() : null)
                .preco(request.getPreco())
                .duracaoMinutos(request.getDuracaoMinutos())
                .ativo(request.getAtivo() != null ? request.getAtivo() : true)
                .totalAgendamentos(0)
                .build();
    }

    public Servico atualizarServico(Servico servicoExistente, ServiceRequestDTO request) {
        servicoExistente.setNome(capitalizarNome(request.getNome().trim()));
        servicoExistente.setDescricao(request.getDescricao() != null ? request.getDescricao().trim() : null);
        servicoExistente.setPreco(request.getPreco());
        servicoExistente.setDuracaoMinutos(request.getDuracaoMinutos());
        
        if (request.getAtivo() != null) {
            servicoExistente.setAtivo(request.getAtivo());
        }
        
        return servicoExistente;
    }

    private String capitalizarNome(String nome) {
        if (nome == null || nome.isEmpty()) return nome;
        
        String[] palavras = nome.toLowerCase().split(" ");
        StringBuilder capitalizado = new StringBuilder();
        
        for (String palavra : palavras) {
            if (palavra.length() > 0) {
                capitalizado.append(Character.toUpperCase(palavra.charAt(0)))
                           .append(palavra.substring(1))
                           .append(" ");
            }
        }
        
        return capitalizado.toString().trim();
    }
}