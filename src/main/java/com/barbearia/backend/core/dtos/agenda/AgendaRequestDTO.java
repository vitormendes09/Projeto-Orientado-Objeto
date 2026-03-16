package com.barbearia.backend.core.dtos.agenda;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaRequestDTO {

    @NotNull(message = "Dia da semana é obrigatório")
    @Min(value = 1, message = "Dia da semana deve ser entre 1 (segunda) e 7 (domingo)")
    @Max(value = 7, message = "Dia da semana deve ser entre 1 (segunda) e 7 (domingo)")
    private Integer diaSemana;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    @AssertTrue(message = "Hora de fim deve ser após hora de início")
    private boolean isHorarioValido() {
        if (horaInicio == null || horaFim == null) return true; // Deixa o @NotNull validar
        return horaFim.isAfter(horaInicio);
    }

    private Boolean ativo = true;
}