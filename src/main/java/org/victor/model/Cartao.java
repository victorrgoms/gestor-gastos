package org.victor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table (name = "cartoes")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apelido;
    private BigDecimal limite;

    @Column(name = "diavencimento")
    private int diaVencimento;

    @ManyToOne
    @JoinColumn(name = "dono_id")
    private Pessoa dono;

    @Column(name = "usuario_id")
    private String usuarioId; // Identifica o dono do registro no login

    public Cartao() {}

    public Cartao(Long id, String apelido, BigDecimal limite, Pessoa dono, int diaVencimento, String usuarioId) {
        this.id = id;
        this.apelido = apelido;
        this.limite = limite;
        this.dono = dono;
        this.diaVencimento = diaVencimento;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApelido() { return apelido; }
    public void setApelido(String apelido) { this.apelido = apelido; }

    public BigDecimal getLimite() { return limite; }
    public void setLimite(BigDecimal limite) { this.limite = limite; }

    public int getDiaVencimento() { return diaVencimento; }
    public void setDiaVencimento(int diaVencimento) { this.diaVencimento = diaVencimento; }

    public Pessoa getDono() { return dono; }
    public void setDono(Pessoa dono) { this.dono = dono; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
}