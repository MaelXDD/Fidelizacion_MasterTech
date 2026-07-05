package com.mastertech.mastertech.dto;

import java.math.BigDecimal;

/** Resumen del reporte de ventas diario/mensual filtrado por categoria (CUS 7). */
public record ReporteVentasDTO(
        String periodo,
        String categoria,
        long cantidadVentas,
        BigDecimal totalVendido
) {
}
