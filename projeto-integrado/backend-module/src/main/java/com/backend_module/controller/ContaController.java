package com.backend_module.controller;

import com.backend_module.model.Conta;
import com.backend_module.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/contas")
@Tag(name = "Contas", description = "API para gerenciamento de contas bancárias")
public class ContaController {

    private final ContaService service;

    public ContaController(ContaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todas as contas", description = "Retorna todas as contas cadastradas no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contas encontradas com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Conta.class)))
    })
    @GetMapping
    public List<Conta> listarTodas() {
        return service.listarTodas();
    }
}