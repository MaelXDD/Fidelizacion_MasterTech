package com.mastertech.mastertech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ciclo de vida de la reparacion de un dispositivo.
 * Tabla mapeada: ordenes_servicio
 */
@Entity
@Table(name = "ordenes_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenServicio {

    /** Estados validos del ciclo de vida de la orden (RF-05). */
    public static final String EN_DIAGNOSTICO = "En Diagnóstico";
    public static final String EN_REPARACION = "En Reparación";
    public static final String REPARADO = "Reparado";
    public static final String ENTREGADO = "Entregado";
    public static final String NO_REPARABLE = "No Reparable";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Long idOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Usuario tecnico;

    @Column(name = "dispositivo", nullable = false, length = 150)
    private String dispositivo;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private String estado = EN_DIAGNOSTICO;

    @Column(name = "costo", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal costo = BigDecimal.ZERO;

    @Column(name = "fecha_ingreso", nullable = false)
    @Builder.Default
    private LocalDateTime fechaIngreso = LocalDateTime.now();
}
