package com.mastertech.mastertech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CanjeRequestDTO (
    @NotBlank(message = "El DNI/RUC del cliente es obligatorio") String dniRucCliente,
    @NotNull(message = "El ID del premio es obligatorio") Long idPremio
) {}
