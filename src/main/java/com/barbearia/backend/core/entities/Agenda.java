package com.barbearia.backend.core.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    /**
     * RN01 - Barbeiro define horário de funcionamento
     * RN01.1 - Sistema só permite agendamentos dentro deste horário
     */
    public boolean isHorarioDisponivel(LocalTime horario, int duracaoMinutos) {
        LocalTime horarioFim = horario.plusMinutes(duracaoMinutos);
        return !horario.isBefore(horaInicio) && !horarioFim.isAfter(horaFim);
    }
}