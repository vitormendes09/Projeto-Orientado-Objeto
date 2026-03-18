package com.barbearia.backend.shared.prototype;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Barbeiro;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BarbeiroPrototype {

    public Barbeiro clonar(Barbeiro original) {
        if (original == null) return null;

        return Barbeiro.builder()
                .nome(original.getNome())
                .email(original.getEmail())
                .telefone(original.getTelefone())
                .senha(original.getSenha())
                .biografia(original.getBiografia())
                .fotoUrl(original.getFotoUrl())
                .ativo(original.getAtivo())
                .build();
    }

    public List<Barbeiro> clonarLista(List<Barbeiro> barbeiros) {
        return barbeiros.stream()
                .map(this::clonar)
                .collect(Collectors.toList());
    }
}