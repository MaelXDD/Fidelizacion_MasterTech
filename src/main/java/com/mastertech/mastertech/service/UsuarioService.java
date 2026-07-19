package com.mastertech.mastertech.service;

import com.mastertech.mastertech.exception.RecursoNoEncontradoException;
import com.mastertech.mastertech.model.Usuario;
import com.mastertech.mastertech.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de soporte: administra vendedores, tecnicos y administradores
 * que participan como FK en Venta (vendedor) y OrdenServicio (tecnico).
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el usuario " + nombreUsuario + ". Registrelo primero."));
    }
//Hola
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            throw new IllegalArgumentException("Ya existe el usuario " + usuario.getNombreUsuario());
        }
        // Nota: en un entorno productivo la contrasena debe cifrarse (BCrypt).
        return usuarioRepository.save(usuario);
    }
}
