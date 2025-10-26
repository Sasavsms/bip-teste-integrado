package com.backend_module.controller;

import com.backend_module.model.dto.TransferenciaDTO;
import com.backend_module.model.Conta;
import com.backend_module.model.Transferencia;
import com.backend_module.model.enums.TipoTransferencia;
import com.backend_module.service.TransferenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TransferController transferController;

    @Mock
    private TransferenciaService transferenciaService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Conta contaOrigem;
    private Conta contaDestino;
    private List<Transferencia> transferencias;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(transferController)
                .build();

        contaOrigem = new Conta();
        contaOrigem.setId(1L);
        contaOrigem.setSaldo(new BigDecimal("1000.00"));

        contaDestino = new Conta();
        contaDestino.setId(2L);
        contaDestino.setSaldo(new BigDecimal("500.00"));

        // Create test transferencias using the constructor
        Transferencia transferencia1 = new Transferencia(contaOrigem, contaDestino, new BigDecimal("100.00"), TipoTransferencia.SAIDA);
        Transferencia transferencia2 = new Transferencia(contaDestino, contaOrigem, new BigDecimal("50.00"), TipoTransferencia.ENTRADA);

        transferencias = Arrays.asList(transferencia1, transferencia2);
    }

    @Test
    void transferir_QuandoParametrosValidos_DeveRetornarSucesso() throws Exception {
        // Arrange
        TransferenciaDTO dto = new TransferenciaDTO(1L, 2L, 200.0);
        when(transferenciaService.transferir(1L, 2L, 200.0))
                .thenReturn("Transferência de 200.00 realizada com sucesso!");

        // Act & Assert
        mockMvc.perform(post("/api/transferencias")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Transferência de 200.00 realizada com sucesso!"));
    }

    @Test
    void transferir_QuandoSaldoInsuficiente_DeveRetornarErro400() throws Exception {
        // Arrange
        TransferenciaDTO dto = new TransferenciaDTO(1L, 2L, 1500.0);
        when(transferenciaService.transferir(1L, 2L, 1500.0))
                .thenThrow(new IllegalArgumentException("Saldo insuficiente"));

        // Act & Assert
        mockMvc.perform(post("/api/transferencias")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transferir_QuandoContaNaoEncontrada_DeveRetornarErro404() throws Exception {
        // Arrange
        TransferenciaDTO dto = new TransferenciaDTO(999L, 2L, 200.0);
        when(transferenciaService.transferir(999L, 2L, 200.0))
                .thenThrow(new IllegalArgumentException("Conta de origem não encontrada"));

        // Act & Assert
        mockMvc.perform(post("/api/transferencias")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPorConta_QuandoContaExiste_DeveRetornarTransferencias() throws Exception {
        // Arrange
        when(transferenciaService.listarPorConta(1L)).thenReturn(transferencias);

        // Act & Assert
        mockMvc.perform(get("/api/transferencias/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void listarPorConta_QuandoContaNaoExiste_DeveRetornarErro404() throws Exception {
        // Arrange
        when(transferenciaService.listarPorConta(999L))
                .thenThrow(new IllegalArgumentException("Conta não encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api/transferencias/999"))
                .andExpect(status().isNotFound());
    }
}
