package com.barbearia.backend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.backend.core.dtos.servicos.ServiceRequestDTO;
import com.barbearia.backend.core.dtos.servicos.ServiceResponseDTO;
import com.barbearia.backend.core.exception.BusinessException;
import com.barbearia.backend.core.exception.ResourceNotFoundException;
import com.barbearia.backend.core.ports.incoming.ServiceService;
import com.barbearia.backend.core.ports.outgoing.ServicoRepository;
import com.barbearia.backend.shared.factory.ServicoFactory;
import com.barbearia.backend.shared.prototype.ServicoPrototype;
import com.barbearia.backend.shared.visitor.ServicoEstatisticaVisitor;
import com.barbearia.backend.shared.visitor.ServicoLogVisitor;
import com.barbearia.backend.core.entities.Servico;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicoServiceImpl implements ServiceService {

    private final ServicoRepository repository;  
    private final ServicoFactory factory;  
    private final ServicoPrototype prototype;  
    private final ServicoLogVisitor logVisitor;  
    private final ServicoEstatisticaVisitor estatisticaVisitor;  

    @Override
    @Transactional
    public ServiceResponseDTO criar(ServiceRequestDTO request) {
        log.info("Criando novo serviço: {}", request.getNome());
        
        validarNomeUnico(request.getNome(), null);
        
        //  Agora retorna Servico (nossa entidade)
        Servico servico = factory.criarServico(request);
        
        Servico saved = repository.save(servico);
        
        logVisitor.visit(saved);
        
        log.info(" Serviço criado com sucesso! ID: {}", saved.getId());
        return toResponseDTO(saved);
    }

    @Override
    public ServiceResponseDTO buscarPorId(Long id) {
        log.debug(" Buscando serviço por ID: {}", id);
        
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));
        
        return toResponseDTO(servico);
    }

    @Override
    public ServiceResponseDTO buscarPorNome(String nome) {
        log.debug(" Buscando serviço por nome: {}", nome);
        
        Servico servico = repository.findByNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com nome: " + nome));
        
        return toResponseDTO(servico);
    }

    @Override
    public List<ServiceResponseDTO> listarTodos() {
        log.debug(" Listando todos os serviços (incluindo inativos)");
        
        List<Servico> servicos = repository.findAll();
        
        var estatisticas = estatisticaVisitor.visitAll(servicos);
        log.info("Estatísticas: {}", estatisticas);
        
        return servicos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponseDTO> listarAtivos() {
        log.debug("Listando apenas serviços ativos (para clientes)");
        
        List<Servico> servicos = repository.findAllAtivos();
        
        return servicos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ServiceResponseDTO atualizar(Long id, ServiceRequestDTO request) {
        log.info(" Atualizando serviço ID: {}", id);
        
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));
        
        Servico backup = prototype.clonar(servico);
        
        if (!servico.getNome().equalsIgnoreCase(request.getNome().trim())) {
            validarNomeUnico(request.getNome(), id);
        }
        
        Servico updated = factory.atualizarServico(servico, request);
        
        updated = repository.save(updated);
        
        if (!backup.getPreco().equals(updated.getPreco())) {
            log.debug(" Preço alterado de R$ {} para R$ {}", 
                backup.getPreco(), updated.getPreco());
        }
        
        log.info("Serviço atualizado com sucesso!");
        return toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.warn(" Deletando serviço ID: {} (HARD DELETE)", id);
        
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado com ID: " + id);
        }
        
        log.warn("Verificar se existem agendamentos futuros antes de deletar!");
        
        repository.deleteById(id);
        log.info(" Serviço deletado permanentemente");
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        log.info(" Desativando serviço ID: {} (soft delete)", id);
        
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));
        
        servico.desativar();
        repository.save(servico);
        
        log.info("Serviço desativado com sucesso");
    }

    @Override
    @Transactional
    public void reativar(Long id) {
        log.info("Reativando serviço ID: {}", id);
        
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));
        
        servico.reativar();
        repository.save(servico);
        
        log.info("Serviço reativado com sucesso");
    }

    private void validarNomeUnico(String nome, Long idIgnorar) {
        boolean existe;
        
        if (idIgnorar == null) {
            existe = repository.existsByNome(nome.trim());
        } else {
            existe = repository.existsByNomeAndIdNot(nome.trim(), idIgnorar);
        }
        
        if (existe) {
            throw new BusinessException("Já existe um serviço cadastrado com este nome: " + nome);
        }
    }

    private ServiceResponseDTO toResponseDTO(Servico servico) {
        return new ServiceResponseDTO(
            servico.getId(),
            servico.getNome(),
            servico.getDescricao(),
            servico.getPreco(),
            servico.getDuracaoMinutos(),
            servico.getAtivo(),
            servico.getDataCriacao(),
            servico.getDataAtualizacao(),
            servico.getTotalAgendamentos()
        );
    }
}