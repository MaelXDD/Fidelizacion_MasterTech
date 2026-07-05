package com.mastertech.mastertech.repository;

import com.mastertech.mastertech.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {

    List<OrdenServicio> findByCliente_DniRuc(String dniRuc);

    List<OrdenServicio> findByEstado(String estado);
}
