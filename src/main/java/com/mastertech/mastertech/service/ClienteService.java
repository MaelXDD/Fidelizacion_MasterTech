package com.mastertech.mastertech.service;

import com.mastertech.mastertech.dto.ClienteRankingDTO;
import com.mastertech.mastertech.dto.ClienteRequestDTO;
import com.mastertech.mastertech.exception.RecursoNoEncontradoException;
import com.mastertech.mastertech.model.Cliente;
import com.mastertech.mastertech.model.Venta;
import com.mastertech.mastertech.repository.ClienteRepository;
import com.mastertech.mastertech.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Paquete 2: Gestion de Clientes y Fidelizacion.
 * Cubre CUS 2 (Gestionar Cliente) y colabora en CUS 8 (Ranking).
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;

    public ClienteService(ClienteRepository clienteRepository, VentaRepository ventaRepository) {
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorDniRuc(String dniRuc) {
        return clienteRepository.findByDniRuc(dniRuc)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente registrado con DNI/RUC " + dniRuc));
    }

    /** Busca al cliente o lo crea si aun no existe (flujo alterno de CUS 1 y CUS 2). */
    public Cliente buscarOCrear(String dniRuc, ClienteRequestDTO datosSiNoExiste) {
        return clienteRepository.findByDniRuc(dniRuc).orElseGet(() -> {
            if (datosSiNoExiste == null) {
                throw new RecursoNoEncontradoException(
                        "El cliente con DNI/RUC " + dniRuc + " no existe. Registrelo primero.");
            }
            return registrar(datosSiNoExiste);
        });
    }

    public Cliente registrar(ClienteRequestDTO dto) {
        if (clienteRepository.existsByDniRuc(dto.dniRuc())) {
            throw new IllegalArgumentException("Ya existe un cliente con DNI/RUC " + dto.dniRuc());
        }
        Cliente cliente = Cliente.builder()
                .dniRuc(dto.dniRuc())
                .nombres(dto.nombres())
                .telefono(dto.telefono())
                .email(dto.email())
                .puntosAcumulados(0)
                .build();
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Long idCliente, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + idCliente));
        cliente.setNombres(dto.nombres());
        cliente.setTelefono(dto.telefono());
        cliente.setEmail(dto.email());
        return clienteRepository.save(cliente);
    }

    /** CUS 8 / RF-08: ranking de clientes frecuentes por puntos y total comprado. */
    public List<ClienteRankingDTO> obtenerRanking() {
        return clienteRepository.findAllByOrderByPuntosAcumuladosDesc().stream()
                .map(cliente -> {
                    BigDecimal totalComprado = ventaRepository.findByCliente_IdCliente(cliente.getIdCliente())
                            .stream()
                            .map(Venta::getTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new ClienteRankingDTO(
                            cliente.getNombres(), cliente.getDniRuc(),
                            cliente.getPuntosAcumulados(), totalComprado);
                })
                .toList();
    }

    public void guardar(Cliente cliente) {
        clienteRepository.save(cliente);
    }
}
