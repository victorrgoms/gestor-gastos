package org.victor.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.victor.dto.PessoaRequest;
import org.victor.model.Pessoa;
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
    public List<Pessoa> listar(@RequestHeader("X-Usuario-Id") String usuarioId) {
        return gerenciador.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public Pessoa criar(@RequestBody @Valid PessoaRequest request, @RequestHeader("X-Usuario-Id") String usuarioId) {
        return gerenciador.salvarPessoaViaDTO(request, usuarioId);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletarPessoa(id);
    }

    @PutMapping("/{id}")
    public Pessoa atualizar(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        pessoa.setId(id);
        return gerenciador.salvarPessoa(pessoa);
    }
}