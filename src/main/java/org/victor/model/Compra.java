package org.victor.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private int mesFatura;
    private int anoFatura;
    private Integer parcelaAtual;
    private Integer totalParcelas;

    @ManyToOne
    private Pessoa parceiro;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @ManyToOne
    @JoinColumn(name = "comprador_id")
    private Pessoa comprador;

    public Compra() {}

    public Compra(Long id, String descricao, BigDecimal valor, LocalDate data, int mesFatura, int anoFatura, Cartao cartao, Pessoa comprador) {
    this.id = id;
    this.descricao = descricao;
    this.valor = valor;
    this.data = data;
    this.mesFatura = mesFatura;
    this.anoFatura = anoFatura;
    this.cartao = cartao;
    this.comprador = comprador;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }




    @Override
    public String toString() {
        String compra = "Compra de " + getDescricao() + " no valor de: " + getValor();
        return compra;
    }

    public int getMesFatura() {
        return mesFatura;
    }

    public void setMesFatura(int mesFatura) {
        this.mesFatura = mesFatura;
    }

    public int getAnoFatura() {
        return anoFatura;
    }

    public void setAnoFatura(int anoFatura) {
        this.anoFatura = anoFatura;
    }

    public Pessoa getParceiro() {
        return parceiro;
    }

    public void setParceiro(Pessoa parceiro) {
        this.parceiro = parceiro;
    }

    public Integer getParcelaAtual() {
        return parcelaAtual;
    }

    public void setParcelaAtual(Integer parcelaAtual) {
        this.parcelaAtual = parcelaAtual;
    }

    public Integer getTotalParcelas() {
        return totalParcelas;
    }

    public void setTotalParcelas(Integer totalParcelas) {
        this.totalParcelas = totalParcelas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Pessoa getComprador() {
        return comprador;
    }

    public void setComprador(Pessoa comprador) {
        this.comprador = comprador;
    }
}
