package com.mastertech.mastertech.dto;

import com.mastertech.mastertech.model.Venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Comprobante de venta devuelto al finalizar la transaccion. */
public record VentaResponseDTO(
        Long idVenta,
        LocalDateTime fecha,
        String clienteNombres,
        String clienteDniRuc,
        Integer puntosAcumuladosCliente,
        List<DetalleVentaResponseDTO> detalles,
        BigDecimal subtotal,
        BigDecimal descuento,
        BigDecimal total
) {
    public static VentaResponseDTO desde(Venta v) {
        return new VentaResponseDTO(
                v.getIdVenta(),
                v.getFecha(),
                v.getCliente().getNombres(),
                v.getCliente().getDniRuc(),
                v.getCliente().getPuntosAcumulados(),
                v.getDetalles().stream().map(DetalleVentaResponseDTO::desde).toList(),
                v.getSubtotal(),
                v.getDescuento(),
                v.getTotal()
        );
    }
}
