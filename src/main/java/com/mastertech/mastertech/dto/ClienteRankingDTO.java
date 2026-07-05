package com.mastertech.mastertech.dto;

import java.math.BigDecimal;

/** Fila del ranking de clientes frecuentes (CUS 8 / RF-08). */
public record ClienteRankingDTO(
        String nombres,
        String dniRuc,
        Integer puntosAcumulados,
        BigDecimal totalComprado
) {
}
