package org.victor.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.victor.dto.CartaoRequest;
import org.victor.dto.CompraRequest;
import org.victor.dto.ResumoCartaoDTO;
import org.victor.model.Cartao;
import org.victor.model.Compra;
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

    public Cartao salvarCartao(Cartao cartao) {
        return cartaoRepositorio.save(cartao);
    }

    public List<Cartao> listarTodas() {
        return cartaoRepositorio.findAll();
    }

    public void deletarCartao(Long id) {
        cartaoRepositorio.deleteById(id);
    }

    @Transactional
    public Cartao atualizar(Long id, CartaoRequest request) {
        Cartao cartao = cartaoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (request.donoId() != null) {
            Pessoa donoReal = pessoaRepositorio.findById(request.donoId())
                    .orElseThrow(() -> new RuntimeException("Dono não encontrado"));
            cartao.setDono(donoReal); // Vincula o objeto completo buscado do banco
        }

        cartao.setApelido(request.apelido());
        cartao.setLimite(request.limite());
        cartao.setDiaVencimento(request.diaVencimento());

        return cartaoRepositorio.save(cartao);
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

    public List<ResumoCartaoDTO> gerarResumoCartoes(Integer mes, Integer ano) {
        List<Cartao> cartoes = cartaoRepositorio.findAll();
        List<ResumoCartaoDTO> resumo = new ArrayList<>();

        for (Cartao c : cartoes) {
            // Soma os gastos deste cartão neste mês
            BigDecimal gastoMes = compraRepository.somarGastosPorCartao(c.getId(), mes, ano);

            // Se vier null (sem compras), vira Zero
            if (gastoMes == null) gastoMes = BigDecimal.ZERO;

            BigDecimal disponivel = c.getLimite().subtract(gastoMes);

            // Calcula porcentagem para a barra de progresso (proteção contra divisão por zero)
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
