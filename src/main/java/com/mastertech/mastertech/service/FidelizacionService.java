package com.mastertech.mastertech.service;

import com.mastertech.mastertech.model.Cliente;
import com.mastertech.mastertech.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Paquete 2: Gestion de Clientes y Fidelizacion.
 * Cubre CUS 3 (Aplicar Puntos de Fidelizacion) y RF-03.
 *
 * Regla de negocio adoptada para el programa de fidelizacion:
 *  - El cliente acumula 1 punto por cada S/ 1.00 comprado (subtotal).
 *  - Al alcanzar 100 puntos o mas, califica para un 10% de descuento
 *    en la venta actual; al aplicarse, se consumen 100 puntos.
 */
@Service
public class FidelizacionService {

    private static final int PUNTOS_MINIMOS_PARA_BENEFICIO = 100;
    private static final int PUNTOS_CONSUMIDOS_POR_BENEFICIO = 100;
    private static final BigDecimal PORCENTAJE_DESCUENTO = new BigDecimal("0.10");

    //Constante añadida para controlar la equivalencia de puntos
    private static final BigDecimal SOLES_POR_PUNTO = new BigDecimal("100")

    private final ClienteRepository clienteRepository;

    public FidelizacionService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /** Evalua si el cliente califica para un beneficio y calcula el descuento (CUS 3, paso 2-3). */
    public BigDecimal calcularDescuento(Cliente cliente, BigDecimal subtotal) {
        if (cliente.getPuntosAcumulados() != null && cliente.getPuntosAcumulados() >= PUNTOS_MINIMOS_PARA_BENEFICIO) {
            return subtotal.multiply(PORCENTAJE_DESCUENTO).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Actualiza los puntos del cliente tras completar la venta (CUS 3, paso 4).
     * Si se aplico un descuento por beneficio, primero se consumen los puntos usados.
     */
    public void actualizarPuntos(Cliente cliente, BigDecimal subtotalCompra, boolean seAplicoBeneficio) {
        int puntosActuales = cliente.getPuntosAcumulados() == null ? 0 : cliente.getPuntosAcumulados();

        if (seAplicoBeneficio) {
            puntosActuales -= PUNTOS_CONSUMIDOS_POR_BENEFICIO;
        }


        //Divide el subtotal entre 100 y redonde hacia abajo
        int puntosGanados = subtotalCompra.divide(SOLES_POR_PUNTO, 0, RoundingMode.DOWN).intValue();
        puntosActuales += puntosGanados;

        cliente.setPuntosAcumulados(Math.max(puntosActuales, 0));
        clienteRepository.save(cliente);
    }
}
