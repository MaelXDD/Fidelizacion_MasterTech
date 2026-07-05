package com.mastertech.mastertech.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

/** Payload para actualizar el estado de una orden (CUS 5). */
public record ActualizarEstadoOrdenDTO(
        @NotBlank(message = "El nuevo estado es obligatorio") String nuevoEstado,
        BigDecimal costo
) {
}
