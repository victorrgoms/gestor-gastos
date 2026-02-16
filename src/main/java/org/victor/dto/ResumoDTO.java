package org.victor.dto;

import java.math.BigDecimal;

public record ResumoDTO(
        String nome,
        BigDecimal total
) {}