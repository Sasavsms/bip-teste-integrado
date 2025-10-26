package com.backend_module.model.dto;


public class TransferenciaDTO {
    private Long origem;
    private Long destino;
    private Double valor;

    public TransferenciaDTO() {
    }

    public TransferenciaDTO(Long origem, Long destino, Double valor) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
    }

    public Long getOrigem() {
        return origem;
    }

    public void setOrigem(Long origem) {
        this.origem = origem;
    }

    public Long getDestino() {
        return destino;
    }

    public void setDestino(Long destino) {
        this.destino = destino;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}