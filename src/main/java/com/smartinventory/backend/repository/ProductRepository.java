package com.smartinventory.backend.repository;

import com.smartinventory.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySkuCode(String skuCode);

    Optional<Product> findByBarcode(String barcode);

    Optional<Product> findBySkuCodeOrBarcode(String skuCode, String barcode);

    List<Product> findByNameContainingIgnoreCaseOrSkuCodeContainingIgnoreCaseOrBarcodeContainingIgnoreCase(
            String name, String sku, String barcode
    );
    
    @Query(value = """
            SELECT COUNT(*)
            FROM products p
            WHERE p.is_active = 1
            """, nativeQuery = true)
        long countActiveProducts();

    @Query(value = """
        SELECT COUNT(*) 
        FROM products p
        WHERE p.is_active = 1
          AND p.current_stock IS NOT NULL
          AND p.min_stock_level IS NOT NULL
          AND p.current_stock <= p.min_stock_level
        """, nativeQuery = true)
    long countLowStockItems();
}
