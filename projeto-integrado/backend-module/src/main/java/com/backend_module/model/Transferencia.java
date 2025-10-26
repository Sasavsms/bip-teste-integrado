package com.backend_module.model;

import com.backend_module.model.enums.TipoTransferencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencia")
@Schema(description = "Entidade que representa uma transferência entre contas")
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da transferência", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id", nullable = false)
    @Schema(description = "Conta de origem da transferência", required = true)
    private Conta contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id", nullable = false)
    @Schema(description = "Conta de destino da transferência", required = true)
    private Conta contaDestino;

    @Column(nullable = false)
    @Schema(description = "Valor monetário da transferência", example = "100.00", required = true)
    private BigDecimal valor;

    @Column(nullable = false)
    @Schema(description = "Data e hora da transferência", example = "2023-10-25T14:30:00", required = true)
    private LocalDateTime dataHora;

    // Tipo da transferência (ENTRADA ou SAIDA)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo da transferência (ENTRADA ou SAIDA)", example = "SAIDA", required = true)
    private TipoTransferencia tipo;

    public Transferencia() {}

    public Transferencia(Conta origem, Conta destino, BigDecimal valor, TipoTransferencia tipo) {
        this.contaOrigem = origem;
        this.contaDestino = destino;
        this.valor = valor;
        this.tipo = tipo;
        this.dataHora = LocalDateTime.now();
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Conta getContaOrigem() { return contaOrigem; }
    public void setContaOrigem(Conta contaOrigem) { this.contaOrigem = contaOrigem; }

    public Conta getContaDestino() { return contaDestino; }
    public void setContaDestino(Conta contaDestino) { this.contaDestino = contaDestino; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public TipoTransferencia getTipo() { return tipo; }
    public void setTipo(TipoTransferencia tipo) { this.tipo = tipo; }
}
