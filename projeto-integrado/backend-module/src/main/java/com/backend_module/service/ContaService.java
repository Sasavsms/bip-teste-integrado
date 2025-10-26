package com.backend_module.service;

import com.backend_module.model.Conta;
import com.backend_module.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public List<Conta> listarTodas() {
        return contaRepository.findAll();
    }
}