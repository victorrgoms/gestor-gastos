package org.victor.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.victor.dto.CompraRequest;
import org.victor.dto.ResumoDTO;
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
public class CompraService {
    private final CompraRepository repositorio;
    private final PessoaRepository pessoaRepository;
    private final CartaoRepository cartaoRepository;

    public CompraService(CompraRepository repositorio, PessoaRepository pessoaRepository, CartaoRepository cartaoRepository) {
        this.repositorio = repositorio;
        this.pessoaRepository = pessoaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    public void deletarCompra(Long id) {
        repositorio.deleteById(id);
    }

    public List<Compra> listarPorMes(Integer mes, Integer ano, String usuarioId) {
        // Agora filtra também pelo ID do usuário
        return repositorio.findByMesFaturaAndAnoFaturaAndUsuarioId(mes, ano, usuarioId);
    }

    @Transactional
    public List<Compra> salvarCompraViaDTO(CompraRequest request, String usuarioId) {
        Pessoa comprador = new Pessoa();
        comprador.setId(request.compradorId());

        Cartao cartao = cartaoRepository.findById(request.cartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        Pessoa parceiro = null;
        if(request.parceiroId() != null && request.parceiroId() > 0) {
            parceiro = new Pessoa();
            parceiro.setId(request.parceiroId());
        }

        int qtdParcelas = request.totalParcelas() != null ? request.totalParcelas() : 1;
        BigDecimal valorParcela = request.valor().divide(BigDecimal.valueOf(qtdParcelas), 2, BigDecimal.ROUND_HALF_UP);

        List<Compra> comprasGeradas = new ArrayList<>();

        for (int i = 0; i < qtdParcelas; i++) {
            int mesCalculado = request.mesFatura() + i;
            int anoCalculado = request.anoFatura();

            while (mesCalculado > 12) {
                mesCalculado -= 12;
                anoCalculado++;
            }

            // Validação de limite segura por usuário
            BigDecimal totalGastoNoMes = repositorio.somarGastosPorCartao(cartao.getId(), mesCalculado, anoCalculado, usuarioId);
            if (totalGastoNoMes == null) totalGastoNoMes = BigDecimal.ZERO;

            if (totalGastoNoMes.add(valorParcela).compareTo(cartao.getLimite()) > 0) {
                throw new IllegalArgumentException("Limite insuficiente em " + mesCalculado + "/" + anoCalculado);
            }

            Compra c = new Compra();
            c.setDescricao(request.descricao());
            c.setValor(valorParcela);
            c.setData(request.data().plusMonths(i));
            c.setMesFatura(mesCalculado);
            c.setAnoFatura(anoCalculado);
            c.setCartao(cartao);
            c.setComprador(comprador);
            c.setParceiro(parceiro);
            c.setParcelaAtual(i + 1);
            c.setTotalParcelas(qtdParcelas);
            c.setUsuarioId(usuarioId); // <--- Vincula ao usuário

            comprasGeradas.add(repositorio.save(c));
        }
        return comprasGeradas;
    }

    public List<ResumoDTO> gerarResumo(Integer mes, Integer ano, String usuarioId) {
        // Busca pessoas DO USUÁRIO
        List<Pessoa> pessoas = pessoaRepository.findByUsuarioId(usuarioId);
        List<ResumoDTO> resumo = new ArrayList<>();

        for (Pessoa p : pessoas) {
            BigDecimal total = somarTotalPorPessoa(p, mes, ano, usuarioId);
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                resumo.add(new ResumoDTO(p.getNome(), total));
            }
        }
        return resumo;
    }

    public Compra atualizarCompra(Long id, CompraRequest request) {
        Compra c = repositorio.findById(id).orElseThrow();
        c.setDescricao(request.descricao());
        c.setValor(request.valor());
        c.setData(request.data());
        c.setMesFatura(request.mesFatura());
        c.setAnoFatura(request.anoFatura());
        return repositorio.save(c);
    }

    private BigDecimal somarTotalPorPessoa(Pessoa pessoaAlvo, Integer mes, Integer ano, String usuarioId){
        // Busca apenas compras desse usuário
        List<Compra> listaDeCompras = listarPorMes(mes, ano, usuarioId);
        BigDecimal total = BigDecimal.ZERO;

        for (Compra c: listaDeCompras){
            BigDecimal valorReal = BigDecimal.ZERO;
            boolean souComprador = c.getComprador().getId().equals(pessoaAlvo.getId());
            boolean temParceiro = c.getParceiro() != null;
            boolean souParceiro = temParceiro && c.getParceiro().getId().equals(pessoaAlvo.getId());

            if (souComprador && !temParceiro) valorReal = c.getValor();
            else if (souComprador && temParceiro) valorReal = c.getValor().divide(BigDecimal.valueOf(2));
            else if (souParceiro) valorReal = c.getValor().divide(BigDecimal.valueOf(2));

            total = total.add(valorReal);
        }
        return total;
    }
}