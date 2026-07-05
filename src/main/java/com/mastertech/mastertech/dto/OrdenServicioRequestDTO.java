package com.mastertech.mastertech.dto;

import jakarta.validation.constraints.NotBlank;

/** Datos para registrar una nueva orden de servicio tecnico (CUS 4). */
public record OrdenServicioRequestDTO(
        @NotBlank(message = "El DNI/RUC del cliente es obligatorio") String dniRucCliente,
        @NotBlank(message = "El usuario tecnico es obligatorio") String nombreUsuarioTecnico,
        @NotBlank(message = "El dispositivo es obligatorio") String dispositivo,
        @NotBlank(message = "El diagnostico inicial es obligatorio") String diagnostico
) {
}
