package com.mastertech.mastertech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductoRequestDTO(
        @NotBlank(message = "El codigo es obligatorio") String codigo,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String categoria,
        @NotNull(message = "El precio es obligatorio") @PositiveOrZero BigDecimal precio,
        @NotNull(message = "El stock actual es obligatorio") @PositiveOrZero Integer stockActual,
        Integer stockMinimo
) {
}
