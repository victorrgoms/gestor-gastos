package org.victor.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.victor.dto.CartaoRequest;
import org.victor.dto.PessoaRequest;
import org.victor.dto.ResumoCartaoDTO;
import org.victor.model.Cartao;
import org.victor.model.Pessoa;
import org.victor.service.CartaoService;
import org.victor.service.PessoaService;

import java.util.List;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
    private final CartaoService gerenciador;

    public CartaoController(CartaoService gerenciador) {
        this.gerenciador = gerenciador;
    }

    @GetMapping
    public List<Cartao> listarTodas() {
        return gerenciador.listarTodas();
    }

    @GetMapping("/resumo")
    public List<ResumoCartaoDTO> resumoCartoes(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano
    ) {
        return gerenciador.gerarResumoCartoes(mes, ano);
    }

    @PostMapping
    public Cartao criarCartao(@RequestBody @Valid CartaoRequest request){
        return  gerenciador.salvarCartaoViaDTO(request);
    }

    @DeleteMapping("/{id}")
    public void deletarCartao(@PathVariable Long id) {
        gerenciador.deletarCartao(id);
    }

    @PutMapping("/{id}")
    public Cartao atualizarCartao(@PathVariable Long id, @RequestBody @Valid CartaoRequest request) {
        return gerenciador.atualizar(id, request);
    }
}
