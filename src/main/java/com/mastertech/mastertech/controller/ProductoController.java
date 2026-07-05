package com.mastertech.mastertech.controller;

import com.mastertech.mastertech.dto.ProductoRequestDTO;
import com.mastertech.mastertech.dto.ProductoResponseDTO;
import com.mastertech.mastertech.model.Producto;
import com.mastertech.mastertech.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Paquete 3: Gestion de Inventario.
 * Expone CUS 6 (Gestionar Stock y Alertas de Inventario) y RF-06.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final InventarioService inventarioService;

    public ProductoController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return inventarioService.listarTodos().stream().map(ProductoResponseDTO::desde).toList();
    }

    @GetMapping("/{codigo}")
    public ProductoResponseDTO buscarPorCodigo(@PathVariable String codigo) {
        return ProductoResponseDTO.desde(inventarioService.buscarPorCodigo(codigo));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDTO registrar(@Valid @RequestBody ProductoRequestDTO dto) {
        Producto producto = Producto.builder()
                .codigo(dto.codigo())
                .nombre(dto.nombre())
                .categoria(dto.categoria())
                .precio(dto.precio())
                .stockActual(dto.stockActual())
                .stockMinimo(dto.stockMinimo() != null ? dto.stockMinimo() : 3)
                .build();
        return ProductoResponseDTO.desde(inventarioService.guardar(producto));
    }

    @GetMapping("/alertas-stock")
    public List<ProductoResponseDTO> alertasStockBajo() {
        return inventarioService.listarAlertasStockBajo().stream().map(ProductoResponseDTO::desde).toList();
    }
}
