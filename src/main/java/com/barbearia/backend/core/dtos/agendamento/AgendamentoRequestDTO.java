package com.barbearia.backend.core.dtos.agendamento;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoRequestDTO {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "A data do agendamento deve ser futura")
    private LocalDateTime dataHoraInicio;

    @NotEmpty(message = "Selecione pelo menos um serviço")
    private List<Long> servicosIds;

    @Size(max = 255, message = "Observações muito longas")
    private String observacoes;
}