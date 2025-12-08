package com.smartinventory.backend.controller;

import com.smartinventory.backend.dto.product.ProductRequestDto;
import com.smartinventory.backend.dto.product.ProductResponseDto;
import com.smartinventory.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "http://localhost:4200") // adjust if needed
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    // LIST (used for grid) – optional ?search=
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> list(
            @RequestParam(value = "search", required = false) String search) {

        List<ProductResponseDto> items =
                (search == null || search.isBlank())
                        ? productService.search("")   // returns all
                        : productService.search(search);

        return ResponseEntity.ok(items);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long id,
            @RequestBody ProductRequestDto request) {

        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // ACTIVATE / DEACTIVATE
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ProductResponseDto> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(productService.toggleActive(id));

    }
 // GET ONE – for edit screen
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getOne(@PathVariable Long id) {
        ProductResponseDto dto = productService.getById(id); // assumes this exists
        return ResponseEntity.ok(dto);
    }

}
