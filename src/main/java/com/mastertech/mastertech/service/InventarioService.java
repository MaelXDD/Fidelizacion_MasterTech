package com.mastertech.mastertech.service;

import com.mastertech.mastertech.exception.RecursoNoEncontradoException;
import com.mastertech.mastertech.exception.StockInsuficienteException;
import com.mastertech.mastertech.model.Producto;
import com.mastertech.mastertech.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Paquete 3: Gestion de Inventario.
 * Cubre CUS 6 (Gestionar Stock y Alertas de Inventario) y RF-06.
 */
@Service
public class InventarioService {

    private final ProductoRepository productoRepository;

    public InventarioService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto buscarPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el producto con codigo " + codigo));
    }

    /** Verifica disponibilidad antes de vender (RF-01). */
    public void verificarStock(Producto producto, int cantidadSolicitada) {
        if (producto.getStockActual() < cantidadSolicitada) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para " + producto.getNombre() +
                            ". Disponible: " + producto.getStockActual() + ", solicitado: " + cantidadSolicitada);
        }
    }

    /** Descuenta stock tras confirmar la venta. */
    public void descontarStock(Producto producto, int cantidad) {
        producto.setStockActual(producto.getStockActual() - cantidad);
        productoRepository.save(producto);
    }

    /** RF-06: productos por debajo de su stock minimo. */
    public List<Producto> listarAlertasStockBajo() {
        return productoRepository.buscarConStockBajo();
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto guardar(Producto producto) {
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con codigo " + producto.getCodigo());
        }
        return productoRepository.save(producto);
    }
}
