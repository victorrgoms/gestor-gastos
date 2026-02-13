package org.victor.service;

import org.springframework.stereotype.Service;
import org.victor.dto.CompraRequest;
import org.victor.dto.PessoaRequest;
import org.victor.model.Cartao;
import org.victor.model.Compra;
import org.victor.model.Pessoa;
import org.victor.repository.PessoaRepository;

import java.util.List;

@Service
public class PessoaService {
    private final PessoaRepository repositorio;

    public PessoaService(PessoaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Pessoa salvarPessoa(Pessoa pessoa) {
        return repositorio.save(pessoa);
    }

    public List<Pessoa> listarTodas() {
        return repositorio.findAll();
    }

    public void deletarPessoa(Long id) {
        repositorio.deleteById(id);
    }

    public Pessoa salvarPessoaViaDTO(PessoaRequest request) {

        Pessoa novaPessoa = new Pessoa(
                null, // ID é null pois é nova
                request.nome()
        );
        return repositorio.save(novaPessoa);
    }
}
