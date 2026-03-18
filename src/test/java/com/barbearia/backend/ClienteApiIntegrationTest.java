package com.barbearia.backend;

import com.barbearia.backend.core.dtos.clintes.ClienteRequestDTO;
import com.barbearia.backend.core.dtos.clintes.ClienteResponseDTO;
import com.barbearia.backend.infrastructure.persistence.repository.JpaClienteRepository;

import iff.edu.br.barbearia.BarbeariaApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BarbeariaApplication.class)
@ActiveProfiles("test")
class ClienteApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JpaClienteRepository clienteRepository;

    @BeforeEach
    void limparBanco() {
        clienteRepository.deleteAll();
    }

    @Test
    void deveCriarEBuscarCliente() {
        // Criar cliente via API
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNome("Maria Integração");
        request.setTelefone("21988887777");

        ResponseEntity<ClienteResponseDTO> responsePost = restTemplate
            .postForEntity("/api/clientes", request, ClienteResponseDTO.class);

        assertThat(responsePost.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responsePost.getBody()).isNotNull();
        assertThat(responsePost.getBody().getId()).isNotNull();

        Long id = responsePost.getBody().getId();

        // Buscar cliente via API
        ResponseEntity<ClienteResponseDTO> responseGet = restTemplate
            .getForEntity("/api/clientes/" + id, ClienteResponseDTO.class);

        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseGet.getBody().getNome()).isEqualTo("Maria Integração");
        assertThat(responseGet.getBody().getTelefone()).isEqualTo("21988887777");
    }

    @Test
    void deveRetornar404AoBuscarClienteInexistente() {
        ResponseEntity<ClienteResponseDTO> response = restTemplate
            .getForEntity("/api/clientes/99999", ClienteResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}