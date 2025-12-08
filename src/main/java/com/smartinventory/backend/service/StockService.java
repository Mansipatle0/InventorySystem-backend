package com.smartinventory.backend.service;

import com.smartinventory.backend.entity.Product;
import com.smartinventory.backend.entity.Sale;

public interface StockService {

    /**
     * Decrease stock for all sale items and create StockMovement entries.
     * Portable logic: any POS implementation (wine, kirana, etc.) can reuse this.
     */
    void applySaleStockMovement(Sale sale);

    /**
     * Helper method to adjust single product stock.
     */
    void adjustStock(Product product, int quantityChange, String note, Long referenceId);
}
