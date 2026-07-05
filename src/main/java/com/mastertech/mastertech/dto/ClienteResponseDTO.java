package com.mastertech.mastertech.dto;

import com.mastertech.mastertech.model.Cliente;

public record ClienteResponseDTO(
        Long idCliente,
        String dniRuc,
        String nombres,
        String telefono,
        String email,
        Integer puntosAcumulados
) {
    public static ClienteResponseDTO desde(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getIdCliente(),
                cliente.getDniRuc(),
                cliente.getNombres(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getPuntosAcumulados()
        );
    }
}
