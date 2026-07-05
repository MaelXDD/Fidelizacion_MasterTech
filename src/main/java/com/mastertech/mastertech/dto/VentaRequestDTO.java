package com.mastertech.mastertech.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** Payload para registrar una venta (CUS 1). */
public record VentaRequestDTO(
        @NotBlank(message = "El DNI/RUC del cliente es obligatorio") String dniRucCliente,
        @NotBlank(message = "El usuario vendedor es obligatorio") String nombreUsuarioVendedor,
        @NotEmpty(message = "La venta debe tener al menos un producto") @Valid List<ItemVentaDTO> items
) {
}
