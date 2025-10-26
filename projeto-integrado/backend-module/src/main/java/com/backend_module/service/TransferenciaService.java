package com.backend_module.service;

import com.backend_module.model.*;
import com.backend_module.model.enums.TipoTransferencia;
import com.backend_module.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferenciaService {

    private final ContaRepository contaRepository;
    private final TransferenciaRepository transferenciaRepository;

    public TransferenciaService(ContaRepository contaRepository, TransferenciaRepository transferenciaRepository) {
        this.contaRepository = contaRepository;
        this.transferenciaRepository = transferenciaRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public String transferir(Long origemId, Long destinoId, Double valorDouble) {
        BigDecimal valor = BigDecimal.valueOf(valorDouble);

        Conta origem = contaRepository.findByIdForUpdate(origemId)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada"));
        Conta destino = contaRepository.findByIdForUpdate(destinoId)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada"));

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor inválido");
        }

        if (origem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        contaRepository.save(origem);
        contaRepository.save(destino);

        transferenciaRepository.save(new Transferencia(origem, destino, valor, TipoTransferencia.SAIDA));
        transferenciaRepository.save(new Transferencia(destino, origem, valor, TipoTransferencia.ENTRADA));

        return String.format("Transferência de %.2f realizada com sucesso!", valor);
    }

    public List<Transferencia> listarPorConta(Long contaId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        return transferenciaRepository.findByContaOrigemOrContaDestino(conta, conta);
    }
}
