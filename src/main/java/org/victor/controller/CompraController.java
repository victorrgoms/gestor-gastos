package org.victor.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.victor.dto.CompraRequest;
import org.victor.dto.ResumoDTO;
import org.victor.model.Compra;
import org.victor.service.CompraService;
import java.util.List;

@RestController
@RequestMapping("/compras")
public class CompraController {
    private final CompraService gerenciador;

    public CompraController(CompraService gerenciador) {
        this.gerenciador = gerenciador;
    }

    @GetMapping
    public List<Compra> listar(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestHeader("X-Usuario-Id") String usuarioId
    ) {
        return gerenciador.listarPorMes(mes, ano, usuarioId);
    }

    @GetMapping("/resumo")
    public List<ResumoDTO> resumo(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestHeader("X-Usuario-Id") String usuarioId
    ) {
        return gerenciador.gerarResumo(mes, ano, usuarioId);
    }

    @PostMapping
    public List<Compra> criar(@RequestBody @Valid CompraRequest request, @RequestHeader("X-Usuario-Id") String usuarioId) {
        return gerenciador.salvarCompraViaDTO(request, usuarioId);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletarCompra(id);
    }

    @PutMapping("/{id}")
    public Compra atualizar(@PathVariable Long id, @RequestBody @Valid CompraRequest request) {
        return gerenciador.atualizarCompra(id, request);
    }

    @DeleteMapping("/todas")
    public ResponseEntity<Void> deletarTodas(@RequestHeader("X-Usuario-Id") String usuarioId) {
        gerenciador.deletarTodasAsCompras(usuarioId);
        return ResponseEntity.noContent().build();
    }
}