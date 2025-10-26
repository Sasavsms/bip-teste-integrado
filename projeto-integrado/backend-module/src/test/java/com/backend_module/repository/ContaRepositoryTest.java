package com.backend_module.repository;

import com.backend_module.model.Conta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ContaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContaRepository contaRepository;

    @Test
    void findByIdForUpdate_QuandoContaExiste_DeveRetornarConta() {
        // Arrange
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("1000.00"));
        entityManager.persist(conta);
        entityManager.flush();

        // Act
        Optional<Conta> resultado = contaRepository.findByIdForUpdate(conta.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(conta.getId(), resultado.get().getId());
        assertEquals(new BigDecimal("1000.00"), resultado.get().getSaldo());
    }

    @Test
    void findByIdForUpdate_QuandoContaNaoExiste_DeveRetornarVazio() {
        // Act
        Optional<Conta> resultado = contaRepository.findByIdForUpdate(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void findById_QuandoContaExiste_DeveRetornarConta() {
        // Arrange
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("1000.00"));
        entityManager.persist(conta);
        entityManager.flush();

        // Act
        Optional<Conta> resultado = contaRepository.findById(conta.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(conta.getId(), resultado.get().getId());
        assertEquals(new BigDecimal("1000.00"), resultado.get().getSaldo());
    }

    @Test
    void save_QuandoContaNova_DevePersistirConta() {
        // Arrange
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("1000.00"));

        // Act
        Conta contaSalva = contaRepository.save(conta);

        // Assert
        assertNotNull(contaSalva.getId());
        assertEquals(new BigDecimal("1000.00"), contaSalva.getSaldo());

        // Verify it's in the database
        Conta contaRecuperada = entityManager.find(Conta.class, contaSalva.getId());
        assertNotNull(contaRecuperada);
        assertEquals(new BigDecimal("1000.00"), contaRecuperada.getSaldo());
    }

    @Test
    void save_QuandoContaExistente_DeveAtualizarConta() {
        // Arrange
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("1000.00"));
        entityManager.persist(conta);
        entityManager.flush();

        // Update the account
        conta.setSaldo(new BigDecimal("1500.00"));

        // Act
        Conta contaAtualizada = contaRepository.save(conta);

        // Assert
        assertEquals(conta.getId(), contaAtualizada.getId());
        assertEquals(new BigDecimal("1500.00"), contaAtualizada.getSaldo());

        // Verify it's updated in the database
        Conta contaRecuperada = entityManager.find(Conta.class, conta.getId());
        assertEquals(new BigDecimal("1500.00"), contaRecuperada.getSaldo());
    }
}