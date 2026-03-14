package com.barbearia.backend.core.dtos.servicos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDTO {

    @NotBlank(message = "Nome do serviço é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição muito longa (máximo 500 caracteres)")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    @DecimalMax(value = "9999.99", message = "Preço muito alto")
    private BigDecimal preco;

    @NotNull(message = "Duração é obrigatória")
    @Min(value = 5, message = "Duração mínima é 5 minutos")
    @Max(value = 480, message = "Duração máxima é 8 horas (480 minutos)")
    private Integer duracaoMinutos;

    private Boolean ativo = true;  // Opcional, default true
}