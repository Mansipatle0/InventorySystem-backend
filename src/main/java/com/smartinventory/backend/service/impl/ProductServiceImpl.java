package com.smartinventory.backend.service.impl;

import com.smartinventory.backend.common.exception.NotFoundException;
import com.smartinventory.backend.dto.product.ProductRequestDto;
import com.smartinventory.backend.dto.product.ProductResponseDto;
import com.smartinventory.backend.dto.product.StockUpdateRequest;
import com.smartinventory.backend.entity.Product;
import com.smartinventory.backend.repository.ProductRepository;
import com.smartinventory.backend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    @Override
    public Product getEntityByCode(String code) {
        return productRepository.findBySkuCodeOrBarcode(code, code)
                .orElseThrow(() -> new NotFoundException("Product not found with code: " + code));
    }

    @Override
    public ProductResponseDto getById(Long id) {
        return toDto(getEntityById(id));
    }

    @Override
    public ProductResponseDto getByCode(String code) {
        return toDto(getEntityByCode(code));
    }

    @Override
    public List<ProductResponseDto> search(String query) {
        // For admin grid + other callers:
        // if query is empty/null -> return ALL products
        if (query == null || query.trim().isEmpty()) {
            return productRepository.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }

        List<Product> products = productRepository
                .findByNameContainingIgnoreCaseOrSkuCodeContainingIgnoreCaseOrBarcodeContainingIgnoreCase(
                        query, query, query
                );

        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ---------- NEW METHODS FOR ADMIN DASHBOARD ----------

    @Override
    public ProductResponseDto createProduct(ProductRequestDto request) {
        Product product = new Product();
        applyRequestToEntity(request, product);
        // defaults like currentStock, minStockLevel, totalUnitsSold, active
        // are handled in @PrePersist
        Product saved = productRepository.save(product);
        return toDto(saved);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        applyRequestToEntity(request, product);
        Product saved = productRepository.save(product);
        return toDto(saved);
    }

    @Override
    public ProductResponseDto toggleActive(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        Boolean current = product.getActive();
        if (current == null) current = Boolean.TRUE;
        product.setActive(!current);

        Product saved = productRepository.save(product);
        return toDto(saved);
    }
    
    
    @Override
    public ProductResponseDto updateStock(Long id, StockUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        System.out.println(
                "Incoming stock update for id=" + id +
                " | currentStock=" + request.getCurrentStock() +
                " | minStockLevel=" + request.getMinStockLevel()
        );

        // 🔥 IMPORTANT: never write null to NOT NULL columns
        Integer newCurrentStock = request.getCurrentStock() != null
                ? request.getCurrentStock()
                : product.getCurrentStock();    // keep old value if not sent

        Integer newMinStockLevel = request.getMinStockLevel() != null
                ? request.getMinStockLevel()
                : product.getMinStockLevel();   // keep old value if not sent

        product.setCurrentStock(newCurrentStock);
        product.setMinStockLevel(newMinStockLevel);

        Product saved = productRepository.save(product);

        return toDto(saved); // your manual converter
    }


    // ---------- HELPER METHODS ----------

    private void applyRequestToEntity(ProductRequestDto request, Product product) {
        // For CREATE we expect all mandatory fields filled.
        // For UPDATE this also works as full update (form sends all fields).

        product.setSkuCode(request.getSkuCode());
        product.setBarcode(request.getBarcode());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setMrp(request.getMrp());
        product.setSellingPrice(request.getSellingPrice());
        
        if (request.getCurrentStock() != null) {
            product.setCurrentStock(request.getCurrentStock());
        }

        if (request.getMinStockLevel() != null) {
            product.setMinStockLevel(request.getMinStockLevel());
        }

        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }
    }

    private ProductResponseDto toDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setSkuCode(product.getSkuCode());
        dto.setBarcode(product.getBarcode());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setUnitOfMeasure(product.getUnitOfMeasure());
        dto.setMrp(product.getMrp());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setCurrentStock(product.getCurrentStock());
        dto.setMinStockLevel(product.getMinStockLevel());
        dto.setActive(product.getActive());
        return dto;
    }
}
