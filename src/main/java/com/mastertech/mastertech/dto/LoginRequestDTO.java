package com.mastertech.mastertech.dto;

import jakarta.validation.constraints.NotBlank;

/** Credenciales enviadas por el formulario de login. */
public record LoginRequestDTO(
        @NotBlank(message = "El nombre de usuario es obligatorio") String nombreUsuario,
        @NotBlank(message = "La contrasena es obligatoria") String contrasena
) {
}
