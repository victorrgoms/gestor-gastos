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
import java.util.UUID;

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

    @Transactional
    public void deletarCompra(Long id) {
        Compra c = repositorio.findById(id).orElseThrow();

        // se a compra faz parte de um parcelamento, a gente apaga todas as irmas dela
        if (c.getGrupoParcelamento() != null) {
            List<Compra> grupo = repositorio.findByGrupoParcelamento(c.getGrupoParcelamento());
            repositorio.deleteAll(grupo);
        } else {
            // se for compra normal, passa o rodo só nela
            repositorio.delete(c);
        }
    }

    public List<Compra> listarPorMes(Integer mes, Integer ano, String usuarioId) {
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

        // ve de qual parcela a gente vai comecar a contar
        int parcelaInicial = request.parcelaAtual() != null && request.parcelaAtual() > 0 ? request.parcelaAtual() : 1;

        BigDecimal valorParcela = request.valor().divide(BigDecimal.valueOf(qtdParcelas), 2, BigDecimal.ROUND_HALF_UP);

        List<Compra> comprasGeradas = new ArrayList<>();
        String grupoId = UUID.randomUUID().toString(); // id pra juntar a galera

        for (int i = (parcelaInicial - 1); i < qtdParcelas; i++) {
            int mesesParaAdicionar = i - (parcelaInicial - 1);
            int mesCalculado = request.mesFatura() + mesesParaAdicionar;
            int anoCalculado = request.anoFatura();

            // vira o ano se passar de dezembro
            while (mesCalculado > 12) {
                mesCalculado -= 12;
                anoCalculado++;
            }

            BigDecimal totalGastoNoMes = repositorio.somarGastosPorCartao(cartao.getId(), mesCalculado, anoCalculado, usuarioId);
            if (totalGastoNoMes == null) totalGastoNoMes = BigDecimal.ZERO;

            if (totalGastoNoMes.add(valorParcela).compareTo(cartao.getLimite()) > 0) {
                throw new IllegalArgumentException("Limite insuficiente em " + mesCalculado + "/" + anoCalculado);
            }

            Compra c = new Compra();
            c.setDescricao(request.descricao());
            c.setValor(valorParcela);
            c.setData(request.data().plusMonths(mesesParaAdicionar));
            c.setMesFatura(mesCalculado);
            c.setAnoFatura(anoCalculado);
            c.setCartao(cartao);
            c.setComprador(comprador);
            c.setParceiro(parceiro);
            c.setParcelaAtual(i + 1);
            c.setTotalParcelas(qtdParcelas);
            c.setUsuarioId(usuarioId);
            c.setGrupoParcelamento(grupoId); // salva o id do grupo

            comprasGeradas.add(repositorio.save(c));
        }
        return comprasGeradas;
    }

    @Transactional
    public void deletarTodasAsCompras(String usuarioId) {
        repositorio.deleteByUsuarioId(usuarioId);
    }

    @Transactional
    public Compra atualizarCompra(Long id, CompraRequest request) {
        Compra cOriginal = repositorio.findById(id).orElseThrow();

        int qtdParcelas = request.totalParcelas() != null ? request.totalParcelas() : 1;
        BigDecimal valorParcela = request.valor().divide(BigDecimal.valueOf(qtdParcelas), 2, BigDecimal.ROUND_HALF_UP);

        Cartao cartao = cartaoRepository.findById(request.cartaoId()).orElseThrow();
        Pessoa comprador = new Pessoa();
        comprador.setId(request.compradorId());

        Pessoa parceiro = null;
        if(request.parceiroId() != null && request.parceiroId() > 0) {
            parceiro = new Pessoa();
            parceiro.setId(request.parceiroId());
        }

        // se tiver grupo a gente atualiza todas as parcelas de uma vez
        if (cOriginal.getGrupoParcelamento() != null) {
            List<Compra> grupo = repositorio.findByGrupoParcelamento(cOriginal.getGrupoParcelamento());
            for (Compra c : grupo) {
                c.setDescricao(request.descricao());
                c.setValor(valorParcela);
                c.setCartao(cartao);
                c.setComprador(comprador);
                c.setParceiro(parceiro);
                // nao mexe na data pra nao baguncar os meses
                repositorio.save(c);
            }
            return cOriginal;
        } else {
            cOriginal.setDescricao(request.descricao());
            cOriginal.setValor(valorParcela);
            cOriginal.setData(request.data());
            cOriginal.setMesFatura(request.mesFatura());
            cOriginal.setAnoFatura(request.anoFatura());
            cOriginal.setCartao(cartao);
            cOriginal.setComprador(comprador);
            cOriginal.setParceiro(parceiro);
            cOriginal.setTotalParcelas(qtdParcelas);
            return repositorio.save(cOriginal);
        }
    }

    public List<ResumoDTO> gerarResumo(Integer mes, Integer ano, String usuarioId) {
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

    private BigDecimal somarTotalPorPessoa(Pessoa pessoaAlvo, Integer mes, Integer ano, String usuarioId){
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