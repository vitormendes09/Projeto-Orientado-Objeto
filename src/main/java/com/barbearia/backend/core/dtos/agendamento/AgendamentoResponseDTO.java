package com.barbearia.backend.core.dtos.agendamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNome;
    private String clienteTelefone;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private List<ServicoAgendadoDTO> servicos;
    private String status;
    private Double valorTotal;
    private String observacoes;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataCancelamento;
    private String motivoCancelamento;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicoAgendadoDTO {
        private Long id;
        private String nome;
        private Double preco;
        private Integer duracaoMinutos;
    }
}