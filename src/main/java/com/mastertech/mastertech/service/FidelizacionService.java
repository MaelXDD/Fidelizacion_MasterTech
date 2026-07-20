package com.mastertech.mastertech.service;

import com.mastertech.mastertech.exception.RecursoNoEncontradoException;
import com.mastertech.mastertech.exception.SolicitudInvalidaException;
import com.mastertech.mastertech.model.Cliente;
import com.mastertech.mastertech.model.Premio;
import com.mastertech.mastertech.repository.ClienteRepository;
import com.mastertech.mastertech.repository.PremioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Paquete 2: Gestion de Clientes y Fidelizacion.
 * Cubre CUS 3 (Aplicar Puntos de Fidelizacion) y RF-03.
 *
 * Regla de negocio adoptada para el programa de fidelizacion:
 *  - El cliente acumula 1 punto por cada S/ 100.00 comprado (subtotal).
 *  - Al alcanzar 20 puntos o mas, califica para un 10% de descuento
 *    en la venta actual; al aplicarse, se consumen 20 puntos.
 */
@Service
public class FidelizacionService {

    // Cambiado exitosamente a 20 puntos según la nueva escala
    private static final int PUNTOS_MINIMOS_PARA_BENEFICIO = 20;
    private static final int PUNTOS_CONSUMIDOS_POR_BENEFICIO = 20;
    private static final BigDecimal PORCENTAJE_DESCUENTO = new BigDecimal("0.10");

    // Constante para controlar la equivalencia de puntos (S/ 100 = 1 punto)
    private static final BigDecimal SOLES_POR_PUNTO = new BigDecimal("100");

    private final ClienteRepository clienteRepository;
    private final PremioRepository premioRepository;

    public FidelizacionService(ClienteRepository clienteRepository, PremioRepository premioRepository) {
        this.clienteRepository = clienteRepository;
        this.premioRepository = premioRepository;
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

        // Divide el subtotal entre 100 y redondea hacia abajo
        int puntosGanados = subtotalCompra.divide(SOLES_POR_PUNTO, 0, RoundingMode.DOWN).intValue();
        puntosActuales += puntosGanados;

        cliente.setPuntosAcumulados(Math.max(puntosActuales, 0));
        clienteRepository.save(cliente);
    }

    /**
     * CUS 3 (Módulo Presencial): Permite al vendedor canjear los puntos acumulados
     * del cliente por un producto de fidelización en stock físico.
     */
    @Transactional
    public void realizarCanjePresencial(String dniCliente, Long idPremio) {
        Cliente cliente = clienteRepository.findByDniRuc(dniCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El cliente con DNI/RUC " + dniCliente + " no está registrado."));

        Premio premio = premioRepository.findById(idPremio)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El catálogo no contiene el premio solicitado."));

        if (premio.getStockActual() <= 0) {
            throw new SolicitudInvalidaException(
                    "El premio '" + premio.getNombre() + "' se encuentra agotado.");
        }

        if (cliente.getPuntosAcumulados() < premio.getPuntosRequeridos()) {
            throw new SolicitudInvalidaException(
                    "Puntos insuficientes. El cliente tiene " + cliente.getPuntosAcumulados()
                            + " puntos, pero necesita " + premio.getPuntosRequeridos() + ".");
        }

        // Aplicar la transacción de canje
        cliente.setPuntosAcumulados(cliente.getPuntosAcumulados() - premio.getPuntosRequeridos());
        premio.setStockActual(premio.getStockActual() - 1);

        clienteRepository.save(cliente);
        premioRepository.save(premio);
    }

    /** Metodo auxiliar para listar premios en el combo del frontend. */
    public List<Premio> listarPremiosDisponibles() {
        return premioRepository.findByStockActualGreaterThan(0);
    }
}
