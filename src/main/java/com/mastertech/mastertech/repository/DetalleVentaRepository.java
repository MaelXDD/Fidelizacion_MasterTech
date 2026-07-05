package com.mastertech.mastertech.repository;

import com.mastertech.mastertech.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByVenta_IdVenta(Long idVenta);

    List<DetalleVenta> findByVenta_FechaBetweenAndProducto_CategoriaIgnoreCase(
            LocalDateTime inicio, LocalDateTime fin, String categoria);

    List<DetalleVenta> findByVenta_FechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
