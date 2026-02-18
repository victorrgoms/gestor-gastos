package org.victor.service;

import org.springframework.stereotype.Service;
import org.victor.dto.PessoaRequest;
import org.victor.model.Pessoa;
import org.victor.repository.PessoaRepository;
import java.util.List;

@Service
public class PessoaService {
    private final PessoaRepository repositorio;

    public PessoaService(PessoaRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Agora recebe o ID do usuário para filtrar
    public List<Pessoa> listarPorUsuario(String usuarioId) {
        return repositorio.findByUsuarioId(usuarioId);
    }

    public Pessoa salvarPessoa(Pessoa pessoa) {
        return repositorio.save(pessoa);
    }

    public void deletarPessoa(Long id) {
        repositorio.deleteById(id);
    }

    public Pessoa salvarPessoaViaDTO(PessoaRequest request, String usuarioId) {
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome(request.nome());
        novaPessoa.setUsuarioId(usuarioId); // <--- Vincula ao usuário

        return repositorio.save(novaPessoa);
    }
}