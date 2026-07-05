package com.mastertech.mastertech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de negocio que representa a un cliente de Master Tech.
 * Tabla mapeada: clientes
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "dni_ruc", nullable = false, unique = true, length = 20)
    private String dniRuc;

    @Column(name = "nombres", nullable = false, length = 150)
    private String nombres;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "puntos_acumulados", nullable = false)
    @Builder.Default
    private Integer puntosAcumulados = 0;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Venta> ventas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<OrdenServicio> ordenesServicio = new ArrayList<>();
}
