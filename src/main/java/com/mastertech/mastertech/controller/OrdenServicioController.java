package com.mastertech.mastertech.controller;

import com.mastertech.mastertech.dto.ActualizarEstadoOrdenDTO;
import com.mastertech.mastertech.dto.OrdenServicioRequestDTO;
import com.mastertech.mastertech.dto.OrdenServicioResponseDTO;
import com.mastertech.mastertech.exception.RecursoNoEncontradoException;
import com.mastertech.mastertech.exception.SolicitudInvalidaException;
import com.mastertech.mastertech.model.Cliente;
import com.mastertech.mastertech.model.OrdenServicio;
import com.mastertech.mastertech.model.Usuario;
import com.mastertech.mastertech.repository.ClienteRepository;
import com.mastertech.mastertech.repository.OrdenServicioRepository;
import com.mastertech.mastertech.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Paquete 4: Gestion de Ordenes de Servicio Tecnico.
 * Segun el paquete de analisis del proyecto (3.3.1), este modulo trabaja
 * directamente con OrdenServicioRepository, sin una capa de servicio
 * intermedia, a diferencia de los otros tres paquetes.
 * Expone CUS 4 (Registrar Orden) y CUS 5 (Actualizar Estado).
 */
@RestController
@RequestMapping("/api/ordenes-servicio")
public class OrdenServicioController {

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            OrdenServicio.EN_DIAGNOSTICO, OrdenServicio.EN_REPARACION,
            OrdenServicio.REPARADO, OrdenServicio.ENTREGADO, OrdenServicio.NO_REPARABLE);

    private final OrdenServicioRepository ordenServicioRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public OrdenServicioController(OrdenServicioRepository ordenServicioRepository,
                                    ClienteRepository clienteRepository,
                                    UsuarioRepository usuarioRepository) {
        this.ordenServicioRepository = ordenServicioRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<OrdenServicioResponseDTO> listar() {
        return ordenServicioRepository.findAll().stream().map(OrdenServicioResponseDTO::desde).toList();
    }

    @GetMapping("/{idOrden}")
    public OrdenServicioResponseDTO buscarPorId(@PathVariable Long idOrden) {
        return OrdenServicioResponseDTO.desde(obtenerOrden(idOrden));
    }

    @GetMapping("/cliente/{dniRuc}")
    public List<OrdenServicioResponseDTO> buscarPorCliente(@PathVariable String dniRuc) {
        return ordenServicioRepository.findByCliente_DniRuc(dniRuc).stream()
                .map(OrdenServicioResponseDTO::desde).toList();
    }

    /** CUS 4: Registrar la orden de servicio tecnico. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenServicioResponseDTO registrar(@Valid @RequestBody OrdenServicioRequestDTO dto) {
        Cliente cliente = clienteRepository.findByDniRuc(dto.dniRucCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El cliente con DNI/RUC " + dto.dniRucCliente() + " no existe. Registrelo primero."));

        Usuario tecnico = usuarioRepository.findByNombreUsuario(dto.nombreUsuarioTecnico())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El usuario tecnico " + dto.nombreUsuarioTecnico() + " no existe."));

        OrdenServicio orden = OrdenServicio.builder()
                .cliente(cliente)
                .tecnico(tecnico)
                .dispositivo(dto.dispositivo())
                .diagnostico(dto.diagnostico())
                .estado(OrdenServicio.EN_DIAGNOSTICO)
                .fechaIngreso(LocalDateTime.now())
                .build();

        return OrdenServicioResponseDTO.desde(ordenServicioRepository.save(orden));
    }

    /** CUS 5: Actualizar el estado de la orden de servicio a lo largo de la reparacion. */
    @PutMapping("/{idOrden}/estado")
    public OrdenServicioResponseDTO actualizarEstado(@PathVariable Long idOrden,
                                                       @Valid @RequestBody ActualizarEstadoOrdenDTO dto) {
        OrdenServicio orden = obtenerOrden(idOrden);

        if (!ESTADOS_VALIDOS.contains(dto.nuevoEstado())) {
            throw new SolicitudInvalidaException("Estado invalido: " + dto.nuevoEstado());
        }

        orden.setEstado(dto.nuevoEstado());

        if (OrdenServicio.ENTREGADO.equals(dto.nuevoEstado()) && dto.costo() != null) {
            orden.setCosto(dto.costo());
        }

        return OrdenServicioResponseDTO.desde(ordenServicioRepository.save(orden));
    }

    private OrdenServicio obtenerOrden(Long idOrden) {
        return ordenServicioRepository.findById(idOrden)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de servicio no encontrada: " + idOrden));
    }
}
