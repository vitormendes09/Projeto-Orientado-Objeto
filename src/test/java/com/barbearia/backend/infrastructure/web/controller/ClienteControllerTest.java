package com.barbearia.backend.infrastructure.web.controller;

import com.barbearia.backend.core.dtos.clintes.ClienteRequestDTO;
import com.barbearia.backend.core.dtos.clintes.ClienteResponseDTO;
import com.barbearia.backend.core.ports.incoming.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import iff.edu.br.barbearia.BarbeariaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@ContextConfiguration(classes = BarbeariaApplication.class)  // <-- IMPORTANTE: Adicione esta linha
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNome("João Teste");
        request.setTelefone("22999999999");

        ClienteResponseDTO response = new ClienteResponseDTO(
            1L, 
            "João Teste", 
            "22999999999", 
            LocalDateTime.now(), 
            null
        );

        when(clienteService.criar(any(ClienteRequestDTO.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Teste"))
                .andExpect(jsonPath("$.telefone").value("22999999999"));
    }

    @Test
    void deveRetornarErroAoCriarClienteSemNome() throws Exception {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setTelefone("22999999999");

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}