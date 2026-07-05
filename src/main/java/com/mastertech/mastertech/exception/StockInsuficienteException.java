package com.mastertech.mastertech.exception;

/** Se lanza cuando no hay stock suficiente para completar una venta (RF-01). */
public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
