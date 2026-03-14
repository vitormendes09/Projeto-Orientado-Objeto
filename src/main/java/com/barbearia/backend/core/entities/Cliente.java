package com.barbearia.backend.core.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String telefone;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "ultimo_agendamento")
    private LocalDateTime ultimoAgendamento;

    /**
     * Lifecycle callback do JPA para setar data de criação antes de persistir
     * Princípio: Não misturar regras de negócio com infraestrutura, mas isso é exceção
     */
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    /**
     * Método para atualizar último agendamento (SRP - coeso)
     */
    public void registrarAgendamento() {
        this.ultimoAgendamento = LocalDateTime.now();
    }
}