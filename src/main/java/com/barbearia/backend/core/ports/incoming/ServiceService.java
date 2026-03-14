package com.barbearia.backend.core.ports.incoming;


import java.util.List;
import com.barbearia.backend.core.dtos.servicos.ServiceRequestDTO;
import com.barbearia.backend.core.dtos.servicos.ServiceResponseDTO;

/**
 * PORTA DE ENTRADA (INCOMING PORT)
 * 
 * Responsabilidade: Definir as operações de negócio disponíveis para serviços
 */
public interface ServiceService {
    ServiceResponseDTO criar(ServiceRequestDTO request);
    ServiceResponseDTO buscarPorId(Long id);
    List<ServiceResponseDTO> listarTodos();
    List<ServiceResponseDTO> listarAtivos();
    ServiceResponseDTO atualizar(Long id, ServiceRequestDTO request);
    void deletar(Long id);  // Hard delete
    void desativar(Long id); // Soft delete
    void reativar(Long id);  // Reativar serviço
    ServiceResponseDTO buscarPorNome(String nome);
}