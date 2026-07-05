package com.mastertech.mastertech.dto;

import com.mastertech.mastertech.model.Producto;

import java.math.BigDecimal;

public record ProductoResponseDTO(
        Long idProducto,
        String codigo,
        String nombre,
        String categoria,
        BigDecimal precio,
        Integer stockActual,
        Integer stockMinimo,
        boolean stockBajo
) {
    public static ProductoResponseDTO desde(Producto p) {
        return new ProductoResponseDTO(
                p.getIdProducto(), p.getCodigo(), p.getNombre(), p.getCategoria(),
                p.getPrecio(), p.getStockActual(), p.getStockMinimo(), p.isStockBajo()
        );
    }
}
