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
    @Column(name = "grupo_parcelamento")
    private String grupoParcelamento;

    @ManyToOne
    private Pessoa parceiro;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @ManyToOne
    @JoinColumn(name = "comprador_id")
    private Pessoa comprador;

    @Column(name = "usuario_id")
    private String usuarioId; // Identifica o dono do registro

    public Compra() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getMesFatura() { return mesFatura; }
    public void setMesFatura(int mesFatura) { this.mesFatura = mesFatura; }

    public int getAnoFatura() { return anoFatura; }
    public void setAnoFatura(int anoFatura) { this.anoFatura = anoFatura; }

    public Integer getParcelaAtual() { return parcelaAtual; }
    public void setParcelaAtual(Integer parcelaAtual) { this.parcelaAtual = parcelaAtual; }

    public Integer getTotalParcelas() { return totalParcelas; }
    public void setTotalParcelas(Integer totalParcelas) { this.totalParcelas = totalParcelas; }

    public Pessoa getParceiro() { return parceiro; }
    public void setParceiro(Pessoa parceiro) { this.parceiro = parceiro; }

    public Cartao getCartao() { return cartao; }
    public void setCartao(Cartao cartao) { this.cartao = cartao; }

    public Pessoa getComprador() { return comprador; }
    public void setComprador(Pessoa comprador) { this.comprador = comprador; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getGrupoParcelamento() { return grupoParcelamento; }

    public void setGrupoParcelamento(String grupoParcelamento) { this.grupoParcelamento = grupoParcelamento; }
}