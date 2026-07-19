package com.mastertech.mastertech.repository;

import com.mastertech.mastertech.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {

    List<OrdenServicio> findByEstado(String estado);

    /**
     * Trae cliente y tecnico junto con la orden en una sola consulta
     * (JOIN FETCH). Es necesario porque spring.jpa.open-in-view=false:
     * sin esto, cliente/tecnico quedan como proxies perezosos y el
     * mapeo a OrdenServicioResponseDTO (que llama a getNombres()/
     * getNombreUsuario()) lanza LazyInitializationException una vez
     * que el metodo del repositorio ya cerro su transaccion.
     */
    @Query("SELECT o FROM OrdenServicio o JOIN FETCH o.cliente JOIN FETCH o.tecnico ORDER BY o.idOrden DESC")
    List<OrdenServicio> findAllConClienteYTecnico();

    @Query("SELECT o FROM OrdenServicio o JOIN FETCH o.cliente JOIN FETCH o.tecnico WHERE o.idOrden = :idOrden")
    Optional<OrdenServicio> findByIdConClienteYTecnico(Long idOrden);

    @Query("SELECT o FROM OrdenServicio o JOIN FETCH o.cliente JOIN FETCH o.tecnico WHERE o.cliente.dniRuc = :dniRuc")
    List<OrdenServicio> findByClienteDniRucConClienteYTecnico(String dniRuc);
}
