package com.mastertech.mastertech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Datos que el vendedor ingresa para registrar o actualizar un cliente (CUS 2). */
public record ClienteRequestDTO(
        @NotBlank(message = "El DNI o RUC es obligatorio") String dniRuc,
        @NotBlank(message = "Los nombres son obligatorios") String nombres,
        String telefono,
        @Email(message = "El email no es valido") String email
) {
}
