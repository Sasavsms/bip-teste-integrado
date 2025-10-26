package com.backend_module.service;

import com.backend_module.model.Conta;
import com.backend_module.model.Transferencia;
import com.backend_module.model.enums.TipoTransferencia;
import com.backend_module.repository.ContaRepository;
import com.backend_module.repository.TransferenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferenciaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @InjectMocks
    private TransferenciaService transferenciaService;

    private Conta contaOrigem;
    private Conta contaDestino;

    @BeforeEach
    void setUp() {
        contaOrigem = new Conta();
        contaOrigem.setId(1L);
        contaOrigem.setSaldo(new BigDecimal("1000.00"));

        contaDestino = new Conta();
        contaDestino.setId(2L);
        contaDestino.setSaldo(new BigDecimal("500.00"));
    }

    @Test
    void transferir_QuandoValorValido_DeveRealizarTransferencia() {
        // Arrange
        when(contaRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(contaDestino));

        // Act
        String resultado = transferenciaService.transferir(1L, 2L, 200.0);

        // Assert
        assertEquals("Transferência de 200,00 realizada com sucesso!", resultado);
        assertEquals(new BigDecimal("800.00"), contaOrigem.getSaldo());
        assertEquals(new BigDecimal("700.00"), contaDestino.getSaldo());
        verify(contaRepository).save(contaOrigem);
        verify(contaRepository).save(contaDestino);
        verify(transferenciaRepository, times(2)).save(any(Transferencia.class));
    }

    @Test
    void transferir_QuandoSaldoInsuficiente_DeveLancarExcecao() {
        // Arrange
        when(contaRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(contaDestino));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transferenciaService.transferir(1L, 2L, 1500.0);
        });
        assertEquals("Saldo insuficiente", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void transferir_QuandoValorNegativo_DeveLancarExcecao() {
        // Arrange
        when(contaRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(contaDestino));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transferenciaService.transferir(1L, 2L, -100.0);
        });
        assertEquals("Valor inválido", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void transferir_QuandoContaOrigemNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(contaRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transferenciaService.transferir(1L, 2L, 100.0);
        });
        assertEquals("Conta de origem não encontrada", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void transferir_QuandoContaDestinoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(contaRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdForUpdate(2L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transferenciaService.transferir(1L, 2L, 100.0);
        });
        assertEquals("Conta de destino não encontrada", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void listarPorConta_QuandoContaExiste_DeveRetornarTransferencias() {
        // Arrange
        Transferencia transferencia1 = new Transferencia(contaOrigem, contaDestino, new BigDecimal("100.00"), TipoTransferencia.SAIDA);
        Transferencia transferencia2 = new Transferencia(contaDestino, contaOrigem, new BigDecimal("50.00"), TipoTransferencia.ENTRADA);
        List<Transferencia> transferencias = Arrays.asList(transferencia1, transferencia2);

        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(transferenciaRepository.findByContaOrigemOrContaDestino(contaOrigem, contaOrigem)).thenReturn(transferencias);

        // Act
        List<Transferencia> resultado = transferenciaService.listarPorConta(1L);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(transferencias, resultado);
    }

    @Test
    void listarPorConta_QuandoContaNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(contaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transferenciaService.listarPorConta(1L);
        });
        assertEquals("Conta não encontrada", exception.getMessage());
    }
}