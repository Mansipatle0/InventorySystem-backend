package com.smartinventory.backend.service;

import com.smartinventory.backend.dto.product.ProductRequestDto;
import com.smartinventory.backend.dto.product.ProductResponseDto;
import com.smartinventory.backend.dto.product.StockUpdateRequest;
import com.smartinventory.backend.entity.Product;

import java.util.List;

public interface ProductService {

    Product getEntityById(Long id);

    Product getEntityByCode(String code);  // sku or barcode

    ProductResponseDto getById(Long id);

    ProductResponseDto getByCode(String code);

    List<ProductResponseDto> search(String query);
    

    // -------- NEW METHODS FOR ADMIN DASHBOARD --------
    
    ProductResponseDto createProduct(ProductRequestDto request);

    ProductResponseDto updateProduct(Long id, ProductRequestDto request);

    ProductResponseDto toggleActive(Long id);
    
    ProductResponseDto updateStock(Long id, StockUpdateRequest request);
}
