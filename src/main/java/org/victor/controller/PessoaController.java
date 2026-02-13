package org.victor.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.victor.dto.CompraRequest;
import org.victor.dto.PessoaRequest;
import org.victor.model.Compra;
import org.victor.model.Pessoa;
import org.victor.service.CompraService;
import org.victor.service.PessoaService;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
    private final PessoaService gerenciador;

    public PessoaController(PessoaService gerenciador) {
        this.gerenciador = gerenciador;
    }

    @GetMapping
    public List<Pessoa> listarTodas() {
        return gerenciador.listarTodas();
    }

    @PostMapping
    public Pessoa criarPessoa(@RequestBody @Valid PessoaRequest request){
        return  gerenciador.salvarPessoaViaDTO(request);
    }

    @DeleteMapping("/{id}")
    public void deletarPessoa(@PathVariable Long id) {
        gerenciador.deletarPessoa(id);
    }

    @PutMapping("/{id}")
    public Pessoa atualizarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        pessoa.setId(id);
        return gerenciador.salvarPessoa(pessoa);
    }
}
