package com.mastertech.mastertech.exception;

/** Se lanza cuando los datos de una solicitud no cumplen las reglas de negocio. */
public class SolicitudInvalidaException extends RuntimeException {
    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}
