package org.victor.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.victor.model.Compra;
import org.victor.dto.CompraRequest;
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
    public List<Compra> listarTodas() {
        return gerenciador.listarTodas();
    }

    @PostMapping
    public Compra criarCompra(@RequestBody @Valid CompraRequest request){
        return  gerenciador.salvarCompraViaDTO(request);
    }

    @DeleteMapping("/{id}")
    public void deletarCompra(@PathVariable Long id) {
        gerenciador.deletarCompra(id);
    }

    @PutMapping("/{id}")
    public Compra atualizarCompra(@PathVariable Long id, @RequestBody Compra compra) {
        compra.setId(id);
        return gerenciador.salvarCompra(compra);
    }
}
