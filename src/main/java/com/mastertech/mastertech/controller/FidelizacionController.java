package com.mastertech.mastertech.controller;

import com.mastertech.mastertech.dto.CanjeRequestDTO;
import com.mastertech.mastertech.model.Premio;
import com.mastertech.mastertech.service.FidelizacionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fidelizacion")
public class FidelizacionController {
    private final FidelizacionService fidelizacionService;

    public FidelizacionController(FidelizacionService fidelizacionService) {
        this.fidelizacionService = fidelizacionService;
    }

    @GetMapping("/premios")
    public List<Premio> listarPremios() {
        return fidelizacionService.listarPremiosDisponibles();
    }

    @PostMapping("/canjear")
    public void procesarCanje(@Valid @RequestBody CanjeRequestDTO dto) {
        fidelizacionService.realizarCanjePresencial(dto.dniRucCliente(), dto.idPremio());
    }
}
