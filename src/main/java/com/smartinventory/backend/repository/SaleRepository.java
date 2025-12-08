package com.smartinventory.backend.repository;

import com.smartinventory.backend.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByBillNumber(String billNumber);
    @Query(value = """
            SELECT COALESCE(SUM(s.grand_total), 0) 
            FROM sales s
            WHERE DATE(s.sale_date) = CURRENT_DATE
              AND s.status = 'COMPLETED'
            """, nativeQuery = true)
        BigDecimal getTodayCompletedSalesTotal();
    
    //List<Sale> findBySaleDate(LocalDateTime saleDate);
    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);
}
