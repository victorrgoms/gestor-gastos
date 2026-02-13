package org.victor.dto;

import jakarta.validation.constraints.NotBlank;

public record PessoaRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome
) { }
