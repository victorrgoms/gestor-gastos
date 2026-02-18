package org.victor.service;

import org.springframework.stereotype.Service;
import org.victor.dto.CartaoRequest;
import org.victor.dto.ResumoCartaoDTO;
import org.victor.model.Cartao;
import org.victor.model.Pessoa;
import org.victor.repository.CartaoRepository;
import org.victor.repository.CompraRepository;
import org.victor.repository.PessoaRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartaoService {
    private final CartaoRepository cartaoRepositorio;
    private final PessoaRepository pessoaRepositorio;
    private final CompraRepository compraRepository;

    public CartaoService(CartaoRepository cartaoRepositorio, PessoaRepository pessoaRepositorio, CompraRepository compraRepository) {
        this.cartaoRepositorio = cartaoRepositorio;
        this.pessoaRepositorio = pessoaRepositorio;
        this.compraRepository = compraRepository;
    }

    public List<Cartao> listarPorUsuario(String usuarioId) {
        return cartaoRepositorio.findByUsuarioId(usuarioId);
    }

    public void deletarCartao(Long id) {
        cartaoRepositorio.deleteById(id);
    }

    public Cartao atualizar(Long id, CartaoRequest request) {
        Cartao cartao = cartaoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (request.donoId() != null) {
            Pessoa dono = pessoaRepositorio.findById(request.donoId())
                    .orElseThrow(() -> new RuntimeException("Dono não encontrado"));
            cartao.setDono(dono);
        }

        cartao.setApelido(request.apelido());
        cartao.setLimite(request.limite());
        cartao.setDiaVencimento(request.diaVencimento());

        return cartaoRepositorio.save(cartao);
    }

    public Cartao salvarCartaoViaDTO(CartaoRequest request, String usuarioId) {
        Pessoa dono = new Pessoa();
        dono.setId(request.donoId());

        Cartao novoCartao = new Cartao();
        novoCartao.setApelido(request.apelido());
        novoCartao.setLimite(request.limite());
        novoCartao.setDiaVencimento(request.diaVencimento());
        novoCartao.setDono(dono);
        novoCartao.setUsuarioId(usuarioId); // <--- Vincula ao usuário

        return cartaoRepositorio.save(novoCartao);
    }

    public List<ResumoCartaoDTO> gerarResumoCartoes(Integer mes, Integer ano, String usuarioId) {
        // Busca apenas cartões do usuário logado
        List<Cartao> cartoes = cartaoRepositorio.findByUsuarioId(usuarioId);
        List<ResumoCartaoDTO> resumo = new ArrayList<>();

        for (Cartao c : cartoes) {
            // Soma gastos passando o usuarioId para garantir segurança
            BigDecimal gastoMes = compraRepository.somarGastosPorCartao(c.getId(), mes, ano, usuarioId);

            if (gastoMes == null) gastoMes = BigDecimal.ZERO;
            BigDecimal disponivel = c.getLimite().subtract(gastoMes);

            double porcentagem = 0.0;
            if (c.getLimite().compareTo(BigDecimal.ZERO) > 0) {
                porcentagem = gastoMes.divide(c.getLimite(), 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
            }

            resumo.add(new ResumoCartaoDTO(
                    c.getApelido(),
                    c.getLimite(),
                    gastoMes,
                    disponivel,
                    porcentagem
            ));
        }
        return resumo;
    }
}