package org.victor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CartaoRequest(

        @NotBlank(message = "O apelido é obrigatório")
        String apelido,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal limite,

        @NotNull(message = "O dia é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        int diaVencimento,

        @NotNull(message = "O donoId é obrigatório")
        Long donoId
) {}
