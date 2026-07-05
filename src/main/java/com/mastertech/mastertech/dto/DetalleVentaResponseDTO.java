package com.mastertech.mastertech.dto;

import com.mastertech.mastertech.model.DetalleVenta;

import java.math.BigDecimal;

public record DetalleVentaResponseDTO(
        String codigoProducto,
        String nombreProducto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotalLinea
) {
    public static DetalleVentaResponseDTO desde(DetalleVenta d) {
        return new DetalleVentaResponseDTO(
                d.getProducto().getCodigo(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getSubtotalLinea()
        );
    }
}
