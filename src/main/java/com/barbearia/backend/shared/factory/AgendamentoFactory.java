package com.barbearia.backend.shared.factory;

import org.springframework.stereotype.Component;
import com.barbearia.backend.core.entities.Agendamento;
import com.barbearia.backend.core.entities.Cliente;
import com.barbearia.backend.core.entities.Servico;
import com.barbearia.backend.core.entities.StatusAgendamento;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AgendamentoFactory {

    public Agendamento criarAgendamento(Cliente cliente, List<Servico> servicos,
                                        LocalDateTime dataHoraInicio, 
                                        LocalDateTime dataHoraFim,
                                        String observacoes) {
        
        Double valorTotal = servicos.stream()
                .mapToDouble(s -> s.getPreco().doubleValue())
                .sum();
        
        return Agendamento.builder()
                .cliente(cliente)
                .servicos(servicos)
                .dataHoraInicio(dataHoraInicio)
                .dataHoraFim(dataHoraFim)
                .status(StatusAgendamento.CONFIRMADO)
                .valorTotal(valorTotal)
                .observacoes(observacoes)
                .build();
    }

    public Agendamento criarAgendamentoSimples(Cliente cliente, Servico servico,
                                               LocalDateTime dataHoraInicio) {
        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(servico.getDuracaoMinutos());
        
        return criarAgendamento(cliente, List.of(servico), dataHoraInicio, 
                dataHoraFim, null);
    }
}