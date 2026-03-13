package com.barbearia.backend.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String telefone;
    private LocalDateTime dataCriacao;
    private LocalDateTime ultimoAgendamento;
}