package com.barbearia.backend.core.ports.incoming;


import java.util.List;

import com.barbearia.backend.core.dtos.clintes.ClienteRequestDTO;
import com.barbearia.backend.core.dtos.clintes.ClienteResponseDTO;

/**
 * PORTA DE ENTRADA (INCOMING PORT)
 * 
 * Responsabilidade: Definir as operações de negócio disponíveis
 * 
 * SOLID:
 * - ISP: Interface específica para cliente
 * - DIP: Camadas superiores dependem desta abstração
 */
public interface ClienteService {
    ClienteResponseDTO criar(ClienteRequestDTO request);
    ClienteResponseDTO buscarPorId(Long id);
    List<ClienteResponseDTO> listarTodos();
    ClienteResponseDTO atualizar(Long id, ClienteRequestDTO request);
    void deletar(Long id);
    ClienteResponseDTO buscarPorTelefone(String telefone);
}