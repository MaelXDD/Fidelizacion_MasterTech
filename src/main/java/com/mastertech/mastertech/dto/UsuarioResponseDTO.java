package com.mastertech.mastertech.dto;

import com.mastertech.mastertech.model.Usuario;

public record UsuarioResponseDTO(
        Long idUsuario,
        String nombreUsuario,
        String rol
) {
    public static UsuarioResponseDTO desde(Usuario u) {
        return new UsuarioResponseDTO(u.getIdUsuario(), u.getNombreUsuario(), u.getRol());
    }
}
