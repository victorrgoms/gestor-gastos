package org.victor.dto;

import java.math.BigDecimal;

public record ResumoCartaoDTO(
        String apelido,
        BigDecimal limite,
        BigDecimal totalGasto,
        BigDecimal disponivel,
        Double porcentagemUso
) {}