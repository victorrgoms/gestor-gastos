package org.victor.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.victor.dto.CartaoRequest;
import org.victor.dto.ResumoCartaoDTO;
import org.victor.model.Cartao;
import org.victor.service.CartaoService;
import java.util.List;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
    private final CartaoService gerenciador;

    public CartaoController(CartaoService gerenciador) {
        this.gerenciador = gerenciador;
    }

    @GetMapping
    public List<Cartao> listar(@RequestHeader("X-Usuario-Id") String usuarioId) {
        return gerenciador.listarPorUsuario(usuarioId);
    }

    @GetMapping("/resumo")
    public List<ResumoCartaoDTO> resumo(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestHeader("X-Usuario-Id") String usuarioId
    ) {
        return gerenciador.gerarResumoCartoes(mes, ano, usuarioId);
    }

    @PostMapping
    public Cartao criar(@RequestBody @Valid CartaoRequest request, @RequestHeader("X-Usuario-Id") String usuarioId) {
        return gerenciador.salvarCartaoViaDTO(request, usuarioId);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletarCartao(id);
    }

    @PutMapping("/{id}")
    public Cartao atualizar(@PathVariable Long id, @RequestBody @Valid CartaoRequest request) {
        return gerenciador.atualizar(id, request);
    }
}