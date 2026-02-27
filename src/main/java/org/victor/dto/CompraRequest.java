package org.victor.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompraRequest(

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "A data é obrigatória")
        LocalDate data,

        @NotNull(message = "O id do cartão é obrigatório")
        Long cartaoId,

        @NotNull(message = "O id do comprador é obrigatório")
        Long compradorId,

        @Min(1)
        @Max(12)
        @NotNull(message = "O mes é obrigatório")
        Integer mesFatura,

        @NotNull(message = "O ano é obrigatório")
        Integer anoFatura,

        Long parceiroId,

        @Positive(message = "O valor deve ser positivo")
        Integer totalParcelas,

        @Positive(message = "O valor deve ser positivo")
        Integer parcelaAtual
) { }
