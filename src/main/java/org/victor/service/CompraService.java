package org.victor.service;

import org.springframework.stereotype.Service;
import org.victor.dto.CompraRequest;
import org.victor.model.Cartao;
import org.victor.model.Compra;
import org.victor.model.Pessoa;
import org.victor.repository.CompraRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompraService {
    private final CompraRepository repositorio;

    public CompraService(CompraRepository repositorio) {
        this.repositorio = repositorio;
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

    public Compra salvarCompraViaDTO(CompraRequest request) {
        Pessoa comprador = new Pessoa();
        comprador.setId(request.compradorId());

        Cartao cartao = new Cartao();
        cartao.setId(request.cartaoId());

        // Criamos a compra
        Compra novaCompra = new Compra(
                null, // ID é null pois é nova
                request.descricao(),
                request.valor(),
                request.data(),
                request.mesFatura(),
                request.anoFatura(),
                cartao,
                comprador
        );

        // Verificando se houve divisão na compra
        if(request.parceiroId() != null){
            Pessoa parceiro = new Pessoa();
            parceiro.setId(request.parceiroId());

            novaCompra.setParceiro(parceiro);
        }

        // Salvamos
        return repositorio.save(novaCompra);
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

    public BigDecimal somarTotalPorPessoa(Pessoa pessoaAlvo){
        List<Compra> listaDeCompras = repositorio.findAll();
        BigDecimal total = BigDecimal.ZERO;

        for (Compra c: listaDeCompras){
            BigDecimal valorReal = BigDecimal.ZERO;

            // Verificações
            boolean souComprador = c.getComprador().getId().equals(pessoaAlvo.getId());
            boolean temParceiro = c.getParceiro() != null; // Precisa do getParceiro() criado no passo 1
            boolean souParceiro = temParceiro && c.getParceiro().getId().equals(pessoaAlvo.getId());

            // REGRA 1: Sou comprador e NÃO dividi com ninguém (100%)
            if (souComprador && !temParceiro) {
                valorReal = c.getValor();
            }
            // REGRA 2: Sou comprador mas dividi (50%)
            else if (souComprador && temParceiro) {
                valorReal = c.getValor().divide(BigDecimal.valueOf(2));
            }
            // REGRA 3: Não comprei, mas sou o parceiro da divisão (50%)
            else if (souParceiro) {
                valorReal = c.getValor().divide(BigDecimal.valueOf(2));
            }

            total = total.add(valorReal);
        }
        return total;
    }
}
