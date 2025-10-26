package com.backend_module.service;

import com.backend_module.model.Beneficio;
import com.backend_module.repository.BeneficioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioRepository repository;

    public BeneficioService(BeneficioRepository repository) {
        this.repository = repository;
    }

    public List<Beneficio> listarTodos() {
        return repository.findAll();
    }

    public Beneficio buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Benefício não encontrado"));
    }

    @Transactional
    public Beneficio salvar(Beneficio beneficio) {
        return repository.save(beneficio);
    }

    @Transactional
    public Beneficio atualizar(Long id, Beneficio beneficioAtualizado) {
        Beneficio beneficio = buscarPorId(id);
        beneficio.setNome(beneficioAtualizado.getNome());
        beneficio.setDescricao(beneficioAtualizado.getDescricao());
        beneficio.setValor(beneficioAtualizado.getValor());
        beneficio.setAtivo(beneficioAtualizado.getAtivo());
        return repository.save(beneficio);
    }

    @Transactional
    public void deletar(Long id) {
        Beneficio beneficio = buscarPorId(id);
        repository.delete(beneficio);
    }
}
