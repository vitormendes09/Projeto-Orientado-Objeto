package com.barbearia.backend.core.ports.incoming;

import java.util.List;
import com.barbearia.backend.core.dtos.servicos.ServiceRequestDTO;
import com.barbearia.backend.core.dtos.servicos.ServiceResponseDTO;

public interface ServiceService {
    ServiceResponseDTO criar(Long barbeiroId, ServiceRequestDTO request);
    
    ServiceResponseDTO buscarPorId(Long id);
    List<ServiceResponseDTO> listarPorBarbeiro(Long barbeiroId);
    
    List<ServiceResponseDTO> listarTodos();
    List<ServiceResponseDTO> listarAtivos();
    ServiceResponseDTO atualizar(Long id, ServiceRequestDTO request);
    void deletar(Long id);
    void desativar(Long id);
    void reativar(Long id);
    ServiceResponseDTO buscarPorNome(String nome);
}