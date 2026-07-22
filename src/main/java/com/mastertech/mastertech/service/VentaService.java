package com.mastertech.mastertech.service;

import com.mastertech.mastertech.dto.DetalleVentaResponseDTO;
import com.mastertech.mastertech.dto.ItemVentaDTO;
import com.mastertech.mastertech.dto.ReporteVentasDTO;
import com.mastertech.mastertech.dto.VentaRequestDTO;
import com.mastertech.mastertech.model.Cliente;
import com.mastertech.mastertech.model.DetalleVenta;
import com.mastertech.mastertech.model.Producto;
import com.mastertech.mastertech.model.Usuario;
import com.mastertech.mastertech.model.Venta;
import com.mastertech.mastertech.repository.DetalleVentaRepository;
import com.mastertech.mastertech.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ClienteService clienteService;
    private final InventarioService inventarioService;
    private final FidelizacionService fidelizacionService;
    private final UsuarioService usuarioService;

    public VentaService(VentaRepository ventaRepository,
                        DetalleVentaRepository detalleVentaRepository,
                        ClienteService clienteService,
                        InventarioService inventarioService,
                        FidelizacionService fidelizacionService,
                        UsuarioService usuarioService) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.clienteService = clienteService;
        this.inventarioService = inventarioService;
        this.fidelizacionService = fidelizacionService;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public Venta registrarVenta(VentaRequestDTO dto) {
        Cliente cliente = clienteService.buscarPorDniRuc(dto.dniRucCliente());
        Usuario vendedor = usuarioService.buscarPorNombreUsuario(dto.nombreUsuarioVendedor());

        Venta venta = Venta.builder()
                .cliente(cliente)
                .usuario(vendedor)
                .fecha(LocalDateTime.now())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemVentaDTO item : dto.items()) {
            Producto producto = inventarioService.buscarPorCodigo(item.codigoProducto());
            inventarioService.verificarStock(producto, item.cantidad());

            DetalleVenta detalle = DetalleVenta.builder()
                    .producto(producto)
                    .cantidad(item.cantidad())
                    .precioUnitario(producto.getPrecio())
                    .build();
            venta.agregarDetalle(detalle);

            subtotal = subtotal.add(detalle.getSubtotalLinea());
            inventarioService.descontarStock(producto, item.cantidad());
        }

        BigDecimal descuento = fidelizacionService.calcularDescuento(cliente, subtotal);
        BigDecimal total = subtotal.subtract(descuento);

        venta.setSubtotal(subtotal);
        venta.setDescuento(descuento);
        venta.setTotal(total);

        Venta ventaGuardada = ventaRepository.save(venta);

        fidelizacionService.actualizarPuntos(cliente, subtotal, descuento.compareTo(BigDecimal.ZERO) > 0);

        return ventaGuardada;
    }

    public ReporteVentasDTO generarReporte(String tipo, String categoria, LocalDateTime fechaReferencia) {
        LocalDateTime inicio;
        LocalDateTime fin;

        if ("mensual".equalsIgnoreCase(tipo)) {
            inicio = fechaReferencia.withDayOfMonth(1).toLocalDate().atStartOfDay();
            fin = inicio.plusMonths(1);
        } else {
            inicio = fechaReferencia.toLocalDate().atStartOfDay();
            fin = inicio.plusDays(1);
        }

        List<DetalleVenta> detalles = (categoria == null || categoria.isBlank())
                ? detalleVentaRepository.findByVenta_FechaBetween(inicio, fin)
                : detalleVentaRepository.findByVenta_FechaBetweenAndProducto_CategoriaIgnoreCase(inicio, fin, categoria);

        BigDecimal totalVendido = detalles.stream()
                .map(DetalleVenta::getSubtotalLinea)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long cantidadVentas = detalles.stream()
                .map(d -> d.getVenta().getIdVenta())
                .distinct()
                .count();

        return new ReporteVentasDTO(tipo, categoria, cantidadVentas, totalVendido);
    }

    public List<DetalleVentaResponseDTO> detalleDeVenta(Long idVenta) {
        return detalleVentaRepository.findByVenta_IdVenta(idVenta).stream()
                .map(DetalleVentaResponseDTO::desde)
                .toList();
    }

    public List<Venta> listarTodas() {
        return ventaRepository.findAllConClienteYUsuario();
    }
}
