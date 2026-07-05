package com.mastertech.mastertech.controller;

import com.mastertech.mastertech.dto.ReporteVentasDTO;
import com.mastertech.mastertech.dto.VentaRequestDTO;
import com.mastertech.mastertech.dto.VentaResponseDTO;
import com.mastertech.mastertech.model.Venta;
import com.mastertech.mastertech.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Paquete 1: Gestion de Ventas.
 * Expone CUS 1 (Registrar Venta) y CUS 7 (Generar Reportes de Ventas).
 */
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponseDTO registrarVenta(@Valid @RequestBody VentaRequestDTO dto) {
        Venta venta = ventaService.registrarVenta(dto);
        return VentaResponseDTO.desde(venta);
    }

    @GetMapping
    public List<VentaResponseDTO> listar() {
        return ventaService.listarTodas().stream().map(VentaResponseDTO::desde).toList();
    }

    /**
     * RF-07: reporte de ventas diario o mensual filtrado por categoria.
     * Ejemplo: /api/ventas/reportes?tipo=diario&categoria=Audifonos&fecha=2026-07-05
     */
    @GetMapping("/reportes")
    public ReporteVentasDTO generarReporte(
            @RequestParam(defaultValue = "diario") String tipo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDateTime referencia = (fecha != null ? fecha : LocalDate.now()).atStartOfDay();
        return ventaService.generarReporte(tipo, categoria, referencia);
    }
}
