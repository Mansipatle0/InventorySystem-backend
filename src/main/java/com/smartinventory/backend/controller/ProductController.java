package com.smartinventory.backend.controller;

import com.smartinventory.backend.dto.product.ProductResponseDto;
import com.smartinventory.backend.dto.product.StockUpdateRequest;
import com.smartinventory.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")   // later: tighten for your Angular origin
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1) Get product by id
    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // 2) Scan: by skuCode or barcode
    @GetMapping("/code/{code}")
    public ProductResponseDto getByCode(@PathVariable String code) {
        return productService.getByCode(code);
    }

    // 3) Search for dropdown suggestions (name/code/barcode)
    @GetMapping("/search")
    public List<ProductResponseDto> search(@RequestParam("query") String query) {
        return productService.search(query);
    }
    
    @PatchMapping("/{id}/stock")
    public ProductResponseDto updateStock(
            @PathVariable Long id,
            @RequestBody StockUpdateRequest request
    ) {
        return productService.updateStock(id, request);
    }

}
