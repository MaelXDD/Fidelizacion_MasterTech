package com.mastertech.mastertech.controller;

import com.mastertech.mastertech.dto.ClienteRankingDTO;
import com.mastertech.mastertech.dto.ClienteRequestDTO;
import com.mastertech.mastertech.dto.ClienteResponseDTO;
import com.mastertech.mastertech.model.Cliente;
import com.mastertech.mastertech.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Paquete 2: Gestion de Clientes y Fidelizacion.
 * Expone CUS 2 (Gestionar Cliente) y CUS 8 (Ranking de clientes frecuentes).
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listarTodos().stream().map(ClienteResponseDTO::desde).toList();
    }

    @GetMapping("/{dniRuc}")
    public ClienteResponseDTO buscarPorDniRuc(@PathVariable String dniRuc) {
        return ClienteResponseDTO.desde(clienteService.buscarPorDniRuc(dniRuc));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO registrar(@Valid @RequestBody ClienteRequestDTO dto) {
        Cliente cliente = clienteService.registrar(dto);
        return ClienteResponseDTO.desde(cliente);
    }

    @PutMapping("/{idCliente}")
    public ClienteResponseDTO actualizar(@PathVariable Long idCliente, @Valid @RequestBody ClienteRequestDTO dto) {
        return ClienteResponseDTO.desde(clienteService.actualizar(idCliente, dto));
    }

    @GetMapping("/ranking")
    public List<ClienteRankingDTO> ranking() {
        return clienteService.obtenerRanking();
    }
}
