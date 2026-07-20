package com.mastertech.mastertech.repository;

import com.mastertech.mastertech.model.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PremioRepository extends JpaRepository<Premio, Long> {
    List<Premio> findByStockActualGreaterThan(int stockMinimo);
}
