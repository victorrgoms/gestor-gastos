package org.victor.service;

import org.springframework.stereotype.Service;
import org.victor.dto.CartaoRequest;
import org.victor.dto.CompraRequest;
import org.victor.model.Cartao;
import org.victor.model.Compra;
import org.victor.model.Pessoa;
import org.victor.repository.CartaoRepository;
import org.victor.repository.PessoaRepository;

import java.util.List;

@Service
public class CartaoService {
    private final CartaoRepository cartaoRepositorio;
    private final PessoaRepository pessoaRepositorio;

    public CartaoService(CartaoRepository cartaoRepositorio, PessoaRepository pessoaRepositorio) {
        this.cartaoRepositorio = cartaoRepositorio;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    public Cartao salvarCartao(Cartao cartao) {
        return cartaoRepositorio.save(cartao);
    }

    public List<Cartao> listarTodas() {
        return cartaoRepositorio.findAll();
    }

    public void deletarCartao(Long id) {
        cartaoRepositorio.deleteById(id);
    }

    public Cartao salvarCartaoViaDTO(CartaoRequest request) {

        Pessoa dono = new Pessoa();
        dono.setId(request.donoId());

        Cartao novoCartao = new Cartao(
                null, // ID é null pois é nova
                request.apelido(),
                request.limite(),
                dono,
                request.diaVencimento()
        );

        // 3. Salvamos
        return cartaoRepositorio.save(novoCartao);
    }
}
