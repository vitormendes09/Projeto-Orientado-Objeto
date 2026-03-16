package com.barbearia.backend.core.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @ManyToMany
    @JoinTable(name = "agendamento_servicos", joinColumns = @JoinColumn(name = "agendamento_id"), inverseJoinColumns = @JoinColumn(name = "servico_id"))
    private List<Servico> servicos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "motivo_cancelamento")
    private String motivoCancelamento;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (status == null) {
            status = StatusAgendamento.CONFIRMADO;
        }
    }

    /**
     * RN03 - Sistema impede agendamentos em horários já ocupados
     * Verifica se este agendamento conflita com outro
     */
    public boolean conflitaCom(Agendamento outro) {
        return !(this.dataHoraFim.isBefore(outro.dataHoraInicio) ||
                this.dataHoraInicio.isAfter(outro.dataHoraFim));
    }

    /**
     * RN04 - Cancelamentos
     */
    public void cancelar(String motivo) {
        this.status = StatusAgendamento.CANCELADO;
        this.dataCancelamento = LocalDateTime.now();
        this.motivoCancelamento = motivo;
    }

    /**
     * RN02.2 - Tempo total é soma dos tempos individuais dos serviços
     */
    public int calcularDuracaoTotal() {
        return servicos.stream()
                .mapToInt(Servico::getDuracaoMinutos)
                .sum();
    }

    /**
     * RN02.3 - Valor total é soma dos valores individuais dos serviços
     */
    public Double calcularValorTotal() {
        return servicos.stream()
                .mapToDouble(s -> s.getPreco().doubleValue())
                .sum();
    }
}