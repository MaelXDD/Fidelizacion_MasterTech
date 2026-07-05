package com.mastertech.mastertech.dto;

import com.mastertech.mastertech.model.OrdenServicio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdenServicioResponseDTO(
        Long idOrden,
        String clienteNombres,
        String clienteDniRuc,
        String tecnico,
        String dispositivo,
        String diagnostico,
        String estado,
        BigDecimal costo,
        LocalDateTime fechaIngreso
) {
    public static OrdenServicioResponseDTO desde(OrdenServicio o) {
        return new OrdenServicioResponseDTO(
                o.getIdOrden(),
                o.getCliente().getNombres(),
                o.getCliente().getDniRuc(),
                o.getTecnico().getNombreUsuario(),
                o.getDispositivo(),
                o.getDiagnostico(),
                o.getEstado(),
                o.getCosto(),
                o.getFechaIngreso()
        );
    }
}
