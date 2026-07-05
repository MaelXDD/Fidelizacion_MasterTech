package com.mastertech.mastertech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** Alta de usuarios del sistema (vendedor, tecnico o administrador). */
public record UsuarioRequestDTO(
        @NotBlank(message = "El nombre de usuario es obligatorio") String nombreUsuario,
        @NotBlank(message = "La contrasena es obligatoria") String contrasena,
        @NotBlank(message = "El rol es obligatorio")
        @Pattern(regexp = "VENDEDOR|TECNICO|ADMIN", message = "El rol debe ser VENDEDOR, TECNICO o ADMIN")
        String rol
) {
}
