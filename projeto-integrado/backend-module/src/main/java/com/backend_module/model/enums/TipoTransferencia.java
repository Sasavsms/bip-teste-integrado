package com.backend_module.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de transferência possíveis")
public enum TipoTransferencia {
    @Schema(description = "Transferência de saída (débito)")
    SAIDA,

    @Schema(description = "Transferência de entrada (crédito)")
    ENTRADA
}
