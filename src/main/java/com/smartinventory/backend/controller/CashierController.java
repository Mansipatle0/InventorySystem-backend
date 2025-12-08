package com.smartinventory.backend.controller;

import com.smartinventory.backend.dto.cashier.*;
import com.smartinventory.backend.service.CashierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cashier")
@CrossOrigin(origins = "*")   // later: set your Angular URL here
public class CashierController {

    private final CashierService cashierService;

    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    // ---------- POS: Complete Sale ----------
    @PostMapping("/sales")
    public ResponseEntity<SaleResponseDto> createSale(@Valid @RequestBody SaleRequestDto request) {
        SaleResponseDto response = cashierService.createSale(request);
        return ResponseEntity.ok(response);
    }
    
    

    // ---------- Invoice: Get Sale ----------
    @GetMapping("/sales/{id}")
    public ResponseEntity<SaleResponseDto> getSaleById(@PathVariable Long id) {
        SaleResponseDto response = cashierService.getSaleById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sales/bill/{billNumber}")
    public ResponseEntity<SaleResponseDto> getSaleByBillNumber(@PathVariable String billNumber) {
        SaleResponseDto response = cashierService.getSaleByBillNumber(billNumber);
        return ResponseEntity.ok(response);
    }

    // ---------- Hold Bill ----------
    @PostMapping("/hold-bills")
    public ResponseEntity<HoldBillSummaryDto> createHoldBill(@Valid @RequestBody HoldBillRequestDto request) {
        HoldBillSummaryDto summary = cashierService.createHoldBill(request);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/hold-bills")
    public ResponseEntity<List<HoldBillSummaryDto>> getAllHeldBills() {
        List<HoldBillSummaryDto> heldBills = cashierService.getAllHeldBills();
        return ResponseEntity.ok(heldBills);
    }

    @GetMapping("/hold-bills/{id}")
    public ResponseEntity<HoldBillDetailDto> getHeldBillDetail(@PathVariable Long id) {
        HoldBillDetailDto detail = cashierService.getHeldBillDetail(id);
        return ResponseEntity.ok(detail);
    }

    @DeleteMapping("/hold-bills/{id}")
    public ResponseEntity<Void> deleteHeldBill(@PathVariable Long id) {
        cashierService.deleteHeldBill(id);
        return ResponseEntity.noContent().build();
    }
}
