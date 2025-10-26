package com.backend_module.repository;

import com.backend_module.model.Transferencia;
import com.backend_module.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByContaOrigemOrContaDestino(Conta origem, Conta destino);
}
