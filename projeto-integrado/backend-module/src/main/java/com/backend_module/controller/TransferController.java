package com.backend_module.controller;

import com.backend_module.model.dto.TransferenciaDTO;
import com.backend_module.model.Transferencia;
import com.backend_module.service.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/transferencias")
@Tag(name = "Transferências", description = "API para gerenciamento de transferências entre contas")
public class TransferController {

    private final TransferenciaService service;

    public TransferController(TransferenciaService service) {
        this.service = service;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        String mensagem = ex.getMessage();

        if (mensagem.contains("não encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagem);
        }
    }

    @Operation(summary = "Realizar transferência", description = "Transfere um valor de uma conta para outra")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso", 
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo insuficiente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> transferir(
            @Parameter(description = "Dados da transferência", required = true) 
            @RequestBody TransferenciaDTO transferenciaDTO) {
        return ResponseEntity.ok(service.transferir(
            transferenciaDTO.getOrigem(), 
            transferenciaDTO.getDestino(), 
            transferenciaDTO.getValor()
        ));
    }

    @Operation(summary = "Listar transferências por conta", description = "Retorna todas as transferências relacionadas a uma conta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transferências encontradas com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transferencia.class))),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    @GetMapping("/{contaId}")
    public List<Transferencia> listarPorConta(
            @Parameter(description = "ID da conta para listar transferências", required = true) @PathVariable Long contaId) {
        return service.listarPorConta(contaId);
    }
}
