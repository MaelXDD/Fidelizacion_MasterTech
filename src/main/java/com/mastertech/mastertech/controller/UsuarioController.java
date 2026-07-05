package com.mastertech.mastertech.controller;

import com.mastertech.mastertech.dto.UsuarioRequestDTO;
import com.mastertech.mastertech.dto.UsuarioResponseDTO;
import com.mastertech.mastertech.model.Usuario;
import com.mastertech.mastertech.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Modulo de soporte: administra a los vendedores, tecnicos y administradores
 * que operan el sistema (referenciados como FK en Venta y OrdenServicio).
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listarTodos().stream().map(UsuarioResponseDTO::desde).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO registrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = Usuario.builder()
                .nombreUsuario(dto.nombreUsuario())
                .contrasena(dto.contrasena())
                .rol(dto.rol())
                .build();
        return UsuarioResponseDTO.desde(usuarioService.registrar(usuario));
    }
}
