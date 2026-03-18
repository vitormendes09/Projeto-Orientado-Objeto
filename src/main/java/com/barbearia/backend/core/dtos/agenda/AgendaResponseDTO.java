package com.barbearia.backend.core.dtos.agenda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaResponseDTO {
    private Long id;
    private Integer diaSemana;
    private String nomeDiaSemana; // Para facilitar a leitura
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
}