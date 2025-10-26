package com.backend_module.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "conta")
@Schema(description = "Entidade que representa uma conta bancária no sistema")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da conta", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Saldo disponível na conta", example = "1000.00", required = true)
    private BigDecimal saldo;

    @Version
    @Schema(description = "Versão para controle de concorrência", hidden = true)
    private Long version;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
