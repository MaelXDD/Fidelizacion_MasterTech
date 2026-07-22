package com.mastertech.mastertech.repository;

import com.mastertech.mastertech.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Venta> findByCliente_IdCliente(Long idCliente);

    @Query("SELECT DISTINCT v FROM Venta v " +
            "JOIN FETCH v.cliente JOIN FETCH v.usuario LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.producto " +
            "ORDER BY v.idVenta DESC")
    List<Venta> findAllConClienteYUsuario();
}
