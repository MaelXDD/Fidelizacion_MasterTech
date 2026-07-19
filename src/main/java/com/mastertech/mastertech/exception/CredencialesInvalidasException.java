package com.mastertech.mastertech.exception;

/** Se lanza cuando el usuario o la contraseña no coinciden en el login. */
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}