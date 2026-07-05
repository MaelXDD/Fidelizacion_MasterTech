package com.mastertech.mastertech.exception;

import java.time.LocalDateTime;

/** Estructura estandar de error devuelta por la API. */
public record ErrorRespuesta(
        LocalDateTime fecha,
        int estado,
        String error,
        String mensaje
) {
}
