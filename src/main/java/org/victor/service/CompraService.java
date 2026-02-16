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

    public Compra salvarCompra(Compra compra) {
        return repositorio.save(compra);
    }

    public List<Compra> listarTodas() {
        return repositorio.findAll();
    }

    public void deletarCompra(Long id) {
        repositorio.deleteById(id);
    }

    @Transactional // Importante para desfazer tudo se der erro na metade
    public List<Compra> salvarCompraViaDTO(CompraRequest request) {
        // 1. Busca os objetos vinculados
        Pessoa comprador = new Pessoa();
        comprador.setId(request.compradorId());

        // Precisamos buscar o cartão COMPLETO para saber o limite dele
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

            // Lógica de virada de ano
            int mesCalculado = request.mesFatura() + i;
            int anoCalculado = request.anoFatura();

            while (mesCalculado > 12) {
                mesCalculado -= 12;
                anoCalculado++;
            }

            // 🛑 VALIDAÇÃO DE LIMITE AQUI 🛑
            // 1. Quanto já foi gasto neste cartão neste mês específico?
            BigDecimal totalGastoNoMes = repositorio.somarGastosPorCartao(cartao.getId(), mesCalculado, anoCalculado);
            if (totalGastoNoMes == null) totalGastoNoMes = BigDecimal.ZERO;

            // 2. Quanto sobraria se eu adicionar essa compra?
            BigDecimal novoTotal = totalGastoNoMes.add(valorParcela);

            // 3. Se passar do limite, BLOQUEIA!
            if (novoTotal.compareTo(cartao.getLimite()) > 0) {
                throw new IllegalArgumentException("Limite insuficiente para a fatura de " + mesCalculado + "/" + anoCalculado +
                        ". Disponível: R$ " + cartao.getLimite().subtract(totalGastoNoMes));
            }

            // ... Se passou, cria o objeto (código normal abaixo) ...
            Compra c = new Compra();
            c.setDescricao(request.descricao());
            c.setValor(valorParcela);
            c.setData(request.data().plusMonths(i));
            c.setMesFatura(mesCalculado);
            c.setAnoFatura(anoCalculado);
            c.setCartao(cartao); // Usa o objeto cartao que buscamos lá em cima
            c.setComprador(comprador);
            c.setParceiro(parceiro);
            c.setParcelaAtual(i + 1);
            c.setTotalParcelas(qtdParcelas);

            comprasGeradas.add(repositorio.save(c));
        }

        return comprasGeradas;
    }

    public List<Compra> listarPorMes(Integer mes, Integer ano) {
        if (mes == null || ano == null) {
            return repositorio.findAll(); // Se não informar, traz tudo
        }
        return repositorio.findByMesFaturaAndAnoFatura(mes, ano);
    }

    public List<ResumoDTO> gerarResumo(Integer mes, Integer ano) {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        List<ResumoDTO> resumo = new ArrayList<>();

        for (Pessoa p : pessoas) {
            // Passa o mês e ano para o cálculo
            BigDecimal total = somarTotalPorPessoa(p, mes, ano);

            if (total.compareTo(BigDecimal.ZERO) > 0) {
                resumo.add(new ResumoDTO(p.getNome(), total));
            }
        }
        return resumo;
    }

    public Compra atualizarCompra(Long id, CompraRequest request) {
        // Busca a original
        Compra compraExistente = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada"));

        // Atualiza os dados
        compraExistente.setDescricao(request.descricao());
        compraExistente.setValor(request.valor());
        compraExistente.setData(request.data());
        compraExistente.setMesFatura(request.mesFatura());
        compraExistente.setAnoFatura(request.anoFatura());

        return repositorio.save(compraExistente);
    }

    public BigDecimal somarTotalGeral() {
        List<Compra> listaDeCompras = repositorio.findAll();
        BigDecimal total = BigDecimal.ZERO;

        for (Compra c: listaDeCompras){
            BigDecimal valorDaCompra = c.getValor();
            total = total.add(valorDaCompra);
        }
        return total;
    }

    public BigDecimal somarTotalPorPessoa(Pessoa pessoaAlvo, Integer mes, Integer ano){
        // Em vez de findAll(), usamos o método de filtro que já criamos
        List<Compra> listaDeCompras = listarPorMes(mes, ano);

        BigDecimal total = BigDecimal.ZERO;

        for (Compra c: listaDeCompras){
            // ... (MANTENHA A LÓGICA DO IF/ELSE DOS 50% IGUALZINHO ESTAVA) ...
            BigDecimal valorReal = BigDecimal.ZERO;
            boolean souComprador = c.getComprador().getId().equals(pessoaAlvo.getId());
            boolean temParceiro = c.getParceiro() != null;
            boolean souParceiro = temParceiro && c.getParceiro().getId().equals(pessoaAlvo.getId());

            if (souComprador && !temParceiro) {
                valorReal = c.getValor();
            } else if (souComprador && temParceiro) {
                valorReal = c.getValor().divide(BigDecimal.valueOf(2));
            } else if (souParceiro) {
                valorReal = c.getValor().divide(BigDecimal.valueOf(2));
            }
            total = total.add(valorReal);
        }
        return total;
    }
}
