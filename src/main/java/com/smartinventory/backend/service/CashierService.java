package com.smartinventory.backend.service;

import com.smartinventory.backend.dto.cashier.*;

import java.util.List;

public interface CashierService {

    // POS - complete sale
    SaleResponseDto createSale(SaleRequestDto request);

    // Fetch sale / invoice
    SaleResponseDto getSaleById(Long saleId);

    SaleResponseDto getSaleByBillNumber(String billNumber);

    // Hold Bill
    HoldBillSummaryDto createHoldBill(HoldBillRequestDto request);

    List<HoldBillSummaryDto> getAllHeldBills();

    HoldBillDetailDto getHeldBillDetail(Long heldBillId);

    void deleteHeldBill(Long heldBillId);
}
