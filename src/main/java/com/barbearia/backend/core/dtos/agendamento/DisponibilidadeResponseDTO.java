package com.barbearia.backend.core.dtos.agendamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadeResponseDTO {
    private LocalDate data;
    private List<HorarioDisponivelDTO> horarios;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HorarioDisponivelDTO {
        private String hora;
        private boolean disponivel;
        private Integer minutosDisponiveis;
    }
}