package com.mastertech.mastertech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/** Una linea del carrito: codigo de producto + cantidad. */
public record ItemVentaDTO(
        @NotBlank(message = "El codigo del producto es obligatorio") String codigoProducto,
        @Positive(message = "La cantidad debe ser mayor a 0") Integer cantidad
) {
}
