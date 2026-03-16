package com.barbearia.backend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.backend.core.dtos.barbeiro.BarbeiroRequestDTO;
import com.barbearia.backend.core.dtos.barbeiro.BarbeiroResponseDTO;
import com.barbearia.backend.core.dtos.barbeiro.BarbeiroLoginDTO;
import com.barbearia.backend.core.entities.Barbeiro;
import com.barbearia.backend.core.exception.BusinessException;
import com.barbearia.backend.core.exception.ResourceNotFoundException;
import com.barbearia.backend.core.ports.incoming.BarbeiroService;
import com.barbearia.backend.core.ports.outgoing.BarbeiroRepository;
import com.barbearia.backend.shared.factory.BarbeiroFactory;
import com.barbearia.backend.shared.prototype.BarbeiroPrototype;
import com.barbearia.backend.shared.visitor.BarbeiroLogVisitor;
import com.barbearia.backend.shared.visitor.BarbeiroEstatisticaVisitor;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BarbeiroServiceImpl implements BarbeiroService {

    private final BarbeiroRepository repository;
    private final BarbeiroFactory factory;
    private final BarbeiroPrototype prototype;
    private final BarbeiroLogVisitor logVisitor;
    private final BarbeiroEstatisticaVisitor estatisticaVisitor;

    @Override
    @Transactional
    public BarbeiroResponseDTO criar(BarbeiroRequestDTO request) {
        log.info(" Criando novo barbeiro: {}", request.getNome());
        
        validarEmailUnico(request.getEmail(), null);
        validarTelefoneUnico(request.getTelefone(), null);
        
        Barbeiro barbeiro = factory.criarBarbeiro(request);
        
        Barbeiro saved = repository.save(barbeiro);
        
        logVisitor.visit(saved);
        
        return toResponseDTO(saved);
    }

    @Override
    public BarbeiroResponseDTO buscarPorId(Long id) {
        log.debug("Buscando barbeiro por ID: {}", id);
        
        Barbeiro barbeiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id));
        
        return toResponseDTO(barbeiro);
    }

    @Override
    public BarbeiroResponseDTO buscarPorEmail(String email) {
        log.debug("Buscando barbeiro por email: {}", email);
        
        Barbeiro barbeiro = repository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com email: " + email));
        
        return toResponseDTO(barbeiro);
    }

    @Override
    public List<BarbeiroResponseDTO> listarTodos() {
        log.debug("Listando todos os barbeiros");
        
        List<Barbeiro> barbeiros = repository.findAll();
        
        estatisticaVisitor.visitAll(barbeiros);
        
        return barbeiros.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BarbeiroResponseDTO atualizar(Long id, BarbeiroRequestDTO request) {
        log.info("Atualizando barbeiro ID: {}", id);
        
        Barbeiro barbeiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id));
        
        Barbeiro backup = prototype.clonar(barbeiro);
        
        if (!barbeiro.getEmail().equalsIgnoreCase(request.getEmail())) {
            validarEmailUnico(request.getEmail(), id);
        }
        
        if (!barbeiro.getTelefone().equals(request.getTelefone())) {
            validarTelefoneUnico(request.getTelefone(), id);
        }
        
        Barbeiro updated = factory.atualizarBarbeiro(barbeiro, request);
        
        updated = repository.save(updated);
        
        log.info("Barbeiro atualizado com sucesso!");
        
        return toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        log.info("Desativando barbeiro ID: {}", id);
        
        Barbeiro barbeiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id));
        
        barbeiro.desativar();
        repository.save(barbeiro);
        
        log.info("Barbeiro desativado com sucesso");
    }

    @Override
    @Transactional
    public void reativar(Long id) {
        log.info("Reativando barbeiro ID: {}", id);
        
        Barbeiro barbeiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id));
        
        barbeiro.reativar();
        repository.save(barbeiro);
        
        log.info("Barbeiro reativado com sucesso");
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.warn("Deletando barbeiro ID: {} (HARD DELETE)", id);
        
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id);
        }
        
        // Aqui você pode adicionar validações: 
        // - Existem agendamentos futuros?
        // - Existem serviços ativos?
        
        repository.deleteById(id);
        log.info("Barbeiro deletado permanentemente");
    }

    @Override
    public boolean autenticar(BarbeiroLoginDTO loginDTO) {
        log.debug("Tentativa de autenticação para email: {}", loginDTO.getEmail());
        
        Barbeiro barbeiro = repository.findByEmail(loginDTO.getEmail().toLowerCase())
                .orElse(null);
        
        if (barbeiro == null || !barbeiro.getAtivo()) {
            return false;
        }
        
        // Em produção: usar passwordEncoder.matches(loginDTO.getSenha(), barbeiro.getSenha())
        boolean autenticado = barbeiro.getSenha().equals(loginDTO.getSenha());
        
        if (autenticado) {
            log.info("Barbeiro autenticado com sucesso: {}", barbeiro.getNome());
        } else {
            log.warn("Falha na autenticação para email: {}", loginDTO.getEmail());
        }
        
        return autenticado;
    }

    private void validarEmailUnico(String email, Long idIgnorar) {
        boolean existe;
        
        if (idIgnorar == null) {
            existe = repository.existsByEmail(email.toLowerCase());
        } else {
            // Precisamos de um método customizado no repository
            existe = repository.findAll().stream()
                    .anyMatch(b -> b.getEmail().equalsIgnoreCase(email) && !b.getId().equals(idIgnorar));
        }
        
        if (existe) {
            throw new BusinessException("Já existe um barbeiro cadastrado com este email: " + email);
        }
    }

    private void validarTelefoneUnico(String telefone, Long idIgnorar) {
        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
        boolean existe;
        
        if (idIgnorar == null) {
            existe = repository.existsByTelefone(telefoneLimpo);
        } else {
            existe = repository.findAll().stream()
                    .anyMatch(b -> b.getTelefone().equals(telefoneLimpo) && !b.getId().equals(idIgnorar));
        }
        
        if (existe) {
            throw new BusinessException("Já existe um barbeiro cadastrado com este telefone: " + telefone);
        }
    }

    private BarbeiroResponseDTO toResponseDTO(Barbeiro barbeiro) {
        return new BarbeiroResponseDTO(
            barbeiro.getId(),
            barbeiro.getNome(),
            barbeiro.getEmail(),
            barbeiro.getTelefone(),
            barbeiro.getBiografia(),
            barbeiro.getFotoUrl(),
            barbeiro.getDataCriacao(),
            barbeiro.getDataAtualizacao(),
            barbeiro.getAtivo()
        );
    }
}