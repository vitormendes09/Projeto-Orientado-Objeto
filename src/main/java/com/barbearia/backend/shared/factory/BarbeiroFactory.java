package com.barbearia.backend.shared.factory;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.dtos.barbeiro.BarbeiroRequestDTO;
import com.barbearia.backend.core.entities.Barbeiro;

@Component
public class BarbeiroFactory {

    public Barbeiro criarBarbeiro(BarbeiroRequestDTO request) {
        return Barbeiro.builder()
                .nome(request.getNome().trim())
                .email(request.getEmail().toLowerCase().trim())
                .telefone(limparTelefone(request.getTelefone()))
                .senha(request.getSenha()) // Em produção: encoder.encode(request.getSenha())
                .biografia(request.getBiografia())
                .fotoUrl(request.getFotoUrl())
                .ativo(true)
                .build();
    }

    public Barbeiro atualizarBarbeiro(Barbeiro barbeiroExistente, BarbeiroRequestDTO request) {
        barbeiroExistente.setNome(request.getNome().trim());
        barbeiroExistente.setEmail(request.getEmail().toLowerCase().trim());
        barbeiroExistente.setTelefone(limparTelefone(request.getTelefone()));
        barbeiroExistente.setBiografia(request.getBiografia());
        barbeiroExistente.setFotoUrl(request.getFotoUrl());
        
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            barbeiroExistente.setSenha(request.getSenha()); // Em produção: encoder.encode()
        }
        
        return barbeiroExistente;
    }

    private String limparTelefone(String telefone) {
        return telefone.replaceAll("[^0-9]", "");
    }
}