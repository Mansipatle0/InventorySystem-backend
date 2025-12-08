package com.smartinventory.backend.dto.product;

public class StockUpdateRequest {
    private Integer currentStock;
    private Integer minStockLevel;

    public Integer getCurrentStock() {
        return currentStock;
    }
    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }
    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
}
