package com.mastertech.mastertech.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "premios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Premio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_premio")
    private Long idPremio;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "puntos_requeridos", nullable = false)
    private Integer puntosRequeridos;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;
}
