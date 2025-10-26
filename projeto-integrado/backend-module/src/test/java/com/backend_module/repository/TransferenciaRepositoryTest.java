package com.backend_module.repository;

import com.backend_module.model.Conta;
import com.backend_module.model.Transferencia;
import com.backend_module.model.enums.TipoTransferencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class TransferenciaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    private Conta contaOrigem;
    private Conta contaDestino;
    private Conta contaTerceira;
    private Transferencia transferencia1;
    private Transferencia transferencia2;
    private Transferencia transferencia3;

    @BeforeEach
    void setUp() {
        // Create test accounts
        contaOrigem = new Conta();
        contaOrigem.setSaldo(new BigDecimal("1000.00"));
        entityManager.persist(contaOrigem);

        contaDestino = new Conta();
        contaDestino.setSaldo(new BigDecimal("500.00"));
        entityManager.persist(contaDestino);

        contaTerceira = new Conta();
        contaTerceira.setSaldo(new BigDecimal("2000.00"));
        entityManager.persist(contaTerceira);

        // Create test transfers
        transferencia1 = new Transferencia(contaOrigem, contaDestino, new BigDecimal("100.00"), TipoTransferencia.SAIDA);
        entityManager.persist(transferencia1);

        transferencia2 = new Transferencia(contaDestino, contaOrigem, new BigDecimal("50.00"), TipoTransferencia.ENTRADA);
        entityManager.persist(transferencia2);

        transferencia3 = new Transferencia(contaTerceira, contaDestino, new BigDecimal("200.00"), TipoTransferencia.SAIDA);
        entityManager.persist(transferencia3);

        entityManager.flush();
    }

    @Test
    void findByContaOrigemOrContaDestino_QuandoContaOrigemOuDestino_DeveRetornarTransferencias() {
        // Act
        List<Transferencia> transferencias = transferenciaRepository.findByContaOrigemOrContaDestino(contaOrigem, contaOrigem);

        // Assert
        assertEquals(2, transferencias.size());
        assertTrue(transferencias.contains(transferencia1));
        assertTrue(transferencias.contains(transferencia2));
        assertFalse(transferencias.contains(transferencia3));
    }

    @Test
    void findByContaOrigemOrContaDestino_QuandoContaDestinoEmAmbas_DeveRetornarTransferencias() {
        // Act
        List<Transferencia> transferencias = transferenciaRepository.findByContaOrigemOrContaDestino(contaDestino, contaDestino);

        // Assert
        assertEquals(3, transferencias.size());
        assertTrue(transferencias.contains(transferencia1));
        assertTrue(transferencias.contains(transferencia2));
        assertTrue(transferencias.contains(transferencia3));
    }

    @Test
    void findByContaOrigemOrContaDestino_QuandoContaTerceira_DeveRetornarTransferencias() {
        // Act
        List<Transferencia> transferencias = transferenciaRepository.findByContaOrigemOrContaDestino(contaTerceira, contaTerceira);

        // Assert
        assertEquals(1, transferencias.size());
        assertTrue(transferencias.contains(transferencia3));
    }

    @Test
    void save_QuandoTransferenciaNova_DevePersistirTransferencia() {
        // Arrange
        Transferencia novaTransferencia = new Transferencia(contaOrigem, contaTerceira, new BigDecimal("300.00"), TipoTransferencia.SAIDA);

        // Act
        Transferencia transferenciaSalva = transferenciaRepository.save(novaTransferencia);

        // Assert
        assertNotNull(transferenciaSalva.getId());
        assertEquals(contaOrigem, transferenciaSalva.getContaOrigem());
        assertEquals(contaTerceira, transferenciaSalva.getContaDestino());
        assertEquals(new BigDecimal("300.00"), transferenciaSalva.getValor());
        assertEquals(TipoTransferencia.SAIDA, transferenciaSalva.getTipo());

        // Verify it's in the database
        Transferencia transferenciaRecuperada = entityManager.find(Transferencia.class, transferenciaSalva.getId());
        assertNotNull(transferenciaRecuperada);
        assertEquals(contaOrigem, transferenciaRecuperada.getContaOrigem());
        assertEquals(contaTerceira, transferenciaRecuperada.getContaDestino());
        assertEquals(new BigDecimal("300.00"), transferenciaRecuperada.getValor());
        assertEquals(TipoTransferencia.SAIDA, transferenciaRecuperada.getTipo());
    }
}