package com.barbearia.backend.core.ports.incoming;

import com.barbearia.backend.core.dtos.barbeiro.BarbeiroRequestDTO;
import com.barbearia.backend.core.dtos.barbeiro.BarbeiroResponseDTO;
import com.barbearia.backend.core.dtos.barbeiro.BarbeiroLoginDTO;
import java.util.List;

public interface BarbeiroService {
    BarbeiroResponseDTO criar(BarbeiroRequestDTO request);
    BarbeiroResponseDTO buscarPorId(Long id);
    BarbeiroResponseDTO buscarPorEmail(String email);
    List<BarbeiroResponseDTO> listarTodos();
    BarbeiroResponseDTO atualizar(Long id, BarbeiroRequestDTO request);
    void desativar(Long id);
    void reativar(Long id);
    void deletar(Long id);
    boolean autenticar(BarbeiroLoginDTO loginDTO);
}