package com.backend_module.controller;

import com.backend_module.model.Beneficio;
import com.backend_module.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/beneficios")
@Tag(name = "Benefícios", description = "API para gerenciamento de benefícios")
public class BeneficioController {

    private final BeneficioService service;

    public BeneficioController(BeneficioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os benefícios", description = "Retorna uma lista com todos os benefícios cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Benefícios encontrados com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Beneficio.class)))
    })
    @GetMapping
    public ResponseEntity<List<Beneficio>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar benefício por ID", description = "Retorna um benefício específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Benefício encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Beneficio.class))),
        @ApiResponse(responseCode = "404", description = "Benefício não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Beneficio> buscarPorId(
            @Parameter(description = "ID do benefício a ser buscado", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Criar novo benefício", description = "Cria um novo benefício com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Benefício criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Beneficio.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Beneficio> criar(
            @Parameter(description = "Dados do benefício a ser criado", required = true) @RequestBody Beneficio beneficio) {
        return ResponseEntity.ok(service.salvar(beneficio));
    }

    @Operation(summary = "Atualizar benefício", description = "Atualiza um benefício existente com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Benefício atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Beneficio.class))),
        @ApiResponse(responseCode = "404", description = "Benefício não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Beneficio> atualizar(
            @Parameter(description = "ID do benefício a ser atualizado", required = true) @PathVariable Long id, 
            @Parameter(description = "Novos dados do benefício", required = true) @RequestBody Beneficio beneficio) {
        return ResponseEntity.ok(service.atualizar(id, beneficio));
    }

    @Operation(summary = "Deletar benefício", description = "Remove um benefício existente pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Benefício removido com sucesso", content = @Content),
        @ApiResponse(responseCode = "404", description = "Benefício não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do benefício a ser removido", required = true) @PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
