package com.mastertech.mastertech.exception;

/** Se lanza cuando un cliente, producto, usuario u orden no existe. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
