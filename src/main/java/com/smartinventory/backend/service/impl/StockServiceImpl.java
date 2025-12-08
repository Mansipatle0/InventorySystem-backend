package com.smartinventory.backend.service.impl;

import com.smartinventory.backend.common.enums.MovementType;
import com.smartinventory.backend.entity.Product;
import com.smartinventory.backend.entity.Sale;
import com.smartinventory.backend.entity.SaleItem;
import com.smartinventory.backend.entity.StockMovement;
import com.smartinventory.backend.repository.ProductRepository;
import com.smartinventory.backend.repository.StockMovementRepository;
import com.smartinventory.backend.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl implements StockService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    public StockServiceImpl(StockMovementRepository stockMovementRepository,
                            ProductRepository productRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void applySaleStockMovement(Sale sale) {
        if (sale.getItems() == null) {
            return;
        }

        for (SaleItem item : sale.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            // For a sale: quantityChange = -quantity
            adjustStock(product, -quantity,
                    "Sale: " + sale.getBillNumber(),
                    sale.getId());
        }
    }

    @Override
    @Transactional
    public void adjustStock(Product product, int quantityChange, String note, Long referenceId) {
        int previousStock = product.getCurrentStock();
        int newStock = previousStock + quantityChange;

        product.setCurrentStock(newStock);

        // If OUT_SALE (negative change) -> totalUnitsSold++ 
        if (quantityChange < 0) {
            int soldIncrement = Math.abs(quantityChange);
            product.setTotalUnitsSold(product.getTotalUnitsSold() + soldIncrement);
        }

        productRepository.save(product);

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setMovementType(quantityChange < 0 ? MovementType.OUT_SALE : MovementType.ADJUSTMENT_IN);
        movement.setReferenceId(referenceId);
        movement.setQuantityChange(quantityChange);
        movement.setPreviousStock(previousStock);
        movement.setNewStock(newStock);
        movement.setNote(note);

        stockMovementRepository.save(movement);
    }
}
