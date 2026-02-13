package org.victor.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pessoas")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    //CONSTRUTOR COM PARAMETROS
    // para chamar construtor:
    // Pessoa victor = new Pessoa(1, "Victor")
    public Pessoa() {}

    public Pessoa(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // GETTERS
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    // SETTERS
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(@NotNull(message = "O id do comprador é obrigatório") Long id) {
        this.id = id;
    }
}
