package com.mastertech.mastertech.controller;

import com.mastertech.mastertech.dto.LoginRequestDTO;
import com.mastertech.mastertech.dto.UsuarioResponseDTO;
import com.mastertech.mastertech.model.Usuario;
import com.mastertech.mastertech.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Login del sistema devuelve el usuario con su rol para que el
 * frontend habilite las secciones correspondientes segun el rol
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public UsuarioResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        Usuario usuario = usuarioService.autenticar(dto.nombreUsuario(), dto.contrasena());
        return UsuarioResponseDTO.desde(usuario);
    }
}
