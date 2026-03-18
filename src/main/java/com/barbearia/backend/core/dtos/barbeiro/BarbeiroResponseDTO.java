package com.barbearia.backend.core.dtos.barbeiro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarbeiroResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String biografia;
    private String fotoUrl;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Boolean ativo;
}