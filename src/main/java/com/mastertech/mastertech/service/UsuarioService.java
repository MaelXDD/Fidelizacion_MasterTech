package com.mastertech.mastertech.service;

import com.mastertech.mastertech.exception.CredencialesInvalidasException;
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
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            throw new IllegalArgumentException("Ya existe el usuario " + usuario.getNombreUsuario());
        }
        return usuarioRepository.save(usuario);
    }


    /**
     * Valida las credenciales del login contra la tabla usuarios.
     */
    public Usuario autenticar(String nombreUsuario, String contrasena) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos."));

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos.");
        }

        return usuario;
    }
}

