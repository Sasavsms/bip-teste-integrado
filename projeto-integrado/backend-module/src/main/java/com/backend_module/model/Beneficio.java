package com.backend_module.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "beneficio")
@Schema(description = "Entidade que representa um benefício no sistema")
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do benefício", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nome do benefício", example = "Vale Alimentação", required = true)
    private String nome;

    @Column(length = 255)
    @Schema(description = "Descrição detalhada do benefício", example = "Benefício para alimentação dos funcionários")
    private String descricao;

    @Column(nullable = false, precision = 15, scale = 2)
    @Schema(description = "Valor monetário do benefício", example = "500.00", required = true)
    private BigDecimal valor;

    @Column(nullable = false)
    @Schema(description = "Indica se o benefício está ativo", example = "true", defaultValue = "true")
    private Boolean ativo = true;

    @Version
    @Schema(description = "Versão para controle de concorrência", hidden = true)
    private Long version;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
