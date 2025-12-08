package com.smartinventory.backend.service.impl;

import com.smartinventory.backend.common.enums.PaymentMode;
import com.smartinventory.backend.common.exception.BadRequestException;
import com.smartinventory.backend.common.exception.NotFoundException;
import com.smartinventory.backend.dto.cashier.*;
import com.smartinventory.backend.entity.*;
import com.smartinventory.backend.repository.HeldBillRepository;
import com.smartinventory.backend.repository.SaleRepository;
import com.smartinventory.backend.service.CashierService;
import com.smartinventory.backend.service.ProductService;
import com.smartinventory.backend.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CashierServiceImpl implements CashierService {

    private final ProductService productService;
    private final StockService stockService;
    private final SaleRepository saleRepository;
    private final HeldBillRepository heldBillRepository;

    public CashierServiceImpl(ProductService productService,
                              StockService stockService,
                              SaleRepository saleRepository,
                              HeldBillRepository heldBillRepository) {
        this.productService = productService;
        this.stockService = stockService;
        this.saleRepository = saleRepository;
        this.heldBillRepository = heldBillRepository;
    }

    // --------------- CREATE SALE (Complete Sale) -----------------

    @Override
    @Transactional
    public SaleResponseDto createSale(SaleRequestDto request) {
        if (request.getCartItems() == null || request.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        PaymentMode paymentMode;
        try {
            paymentMode = PaymentMode.valueOf(request.getPaymentMode().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid payment mode: " + request.getPaymentMode());
        }

        // Validate stock + prepare entities
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;   // for now no tax logic
        List<SaleItem> saleItems = new ArrayList<>();

        for (CartItemDto cartItem : request.getCartItems()) {
            Product product = productService.getEntityById(cartItem.getProductId());

            int qty = cartItem.getQuantity();
            if (qty <= 0) {
                throw new BadRequestException("Invalid quantity for product: " + product.getName());
            }

            if (product.getCurrentStock() < qty) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName()
                        + " (available: " + product.getCurrentStock() + ", requested: " + qty + ")");
            }

            BigDecimal unitPrice = cartItem.getUnitPrice();
            BigDecimal discount = cartItem.getDiscountAmount() != null
                    ? cartItem.getDiscountAmount()
                    : BigDecimal.ZERO;

            BigDecimal lineTotal = unitPrice
                    .multiply(BigDecimal.valueOf(qty))
                    .subtract(discount);

            subtotal = subtotal.add(lineTotal);
            totalDiscount = totalDiscount.add(discount);

            SaleItem item = new SaleItem();
            item.setProduct(product);
            item.setQuantity(qty);
            item.setUnitPrice(unitPrice);
            item.setDiscountAmount(discount);
            item.setLineTotal(lineTotal);

            saleItems.add(item);
        }

        BigDecimal grandTotal = subtotal.add(taxAmount); // - other discounts if needed

        Sale sale = new Sale();
        sale.setBillNumber(generateBillNumber());
        sale.setSaleDate(LocalDateTime.now());
        sale.setCashierId(request.getCashierId());
        sale.setSubtotal(subtotal);
        sale.setTotalDiscount(totalDiscount);
        sale.setTaxAmount(taxAmount);
        sale.setGrandTotal(grandTotal);
        sale.setPaymentMode(paymentMode);
        sale.setCustomerName(request.getCustomerName());
        sale.setCustomerPhone(request.getCustomerPhone());
        sale.setStatus("COMPLETED");

        // set back-reference sale in saleItems
        for (SaleItem item : saleItems) {
            item.setSale(sale);
        }
        sale.setItems(saleItems);

        // Save sale + items
        Sale savedSale = saleRepository.save(sale);

        // Apply stock movement (OUT_SALE)
        stockService.applySaleStockMovement(savedSale);

        return toSaleResponse(savedSale);
    }

    // --------------- GET SALE / INVOICE -----------------

    @Override
    @Transactional(readOnly = true)
    public SaleResponseDto getSaleById(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new NotFoundException("Sale not found with id: " + saleId));
        return toSaleResponse(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponseDto getSaleByBillNumber(String billNumber) {
        Sale sale = saleRepository.findByBillNumber(billNumber)
                .orElseThrow(() -> new NotFoundException("Sale not found with bill number: " + billNumber));
        return toSaleResponse(sale);
    }
    
    

    // --------------- HOLD BILL -----------------

    @Override
    @Transactional
    public HoldBillSummaryDto createHoldBill(HoldBillRequestDto request) {
        if (request.getCartItems() == null || request.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        HeldBill heldBill = new HeldBill();
        heldBill.setHoldCode(generateHoldCode());
        heldBill.setCashierId(request.getCashierId());
        heldBill.setCustomerName(request.getCustomerName());
        heldBill.setCustomerPhone(request.getCustomerPhone());
        heldBill.setNote(request.getNote());

        List<HeldBillItem> items = new ArrayList<>();
        BigDecimal estimatedTotal = BigDecimal.ZERO;

        for (CartItemDto cartItem : request.getCartItems()) {
            Product product = productService.getEntityById(cartItem.getProductId());
            int qty = cartItem.getQuantity();

            BigDecimal unitPrice = cartItem.getUnitPrice();
            BigDecimal discount = cartItem.getDiscountAmount() != null
                    ? cartItem.getDiscountAmount()
                    : BigDecimal.ZERO;

            BigDecimal lineTotal = unitPrice
                    .multiply(BigDecimal.valueOf(qty))
                    .subtract(discount);

            HeldBillItem item = new HeldBillItem();
            item.setHeldBill(heldBill);
            item.setProduct(product);
            item.setQuantity(qty);
            item.setUnitPrice(unitPrice);
            item.setDiscountAmount(discount);
            item.setLineTotal(lineTotal);

            items.add(item);
            estimatedTotal = estimatedTotal.add(lineTotal);
        }

        heldBill.setItems(items);

        HeldBill saved = heldBillRepository.save(heldBill);

        HoldBillSummaryDto summary = new HoldBillSummaryDto();
        summary.setId(saved.getId());
        summary.setHoldCode(saved.getHoldCode());
        summary.setCreatedAt(saved.getCreatedAt());
        summary.setCustomerName(saved.getCustomerName());
        summary.setEstimatedTotal(estimatedTotal);
        summary.setItemsCount(items.size());   // ✅ NEW: items count

        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoldBillSummaryDto> getAllHeldBills() {
        List<HeldBill> heldBills = heldBillRepository.findAll();
        List<HoldBillSummaryDto> summaries = new ArrayList<>();

        for (HeldBill hb : heldBills) {
            BigDecimal total = BigDecimal.ZERO;
            int count = 0;

            if (hb.getItems() != null) {
                count = hb.getItems().size();
                for (HeldBillItem item : hb.getItems()) {
                    total = total.add(item.getLineTotal());
                }
            }

            HoldBillSummaryDto dto = new HoldBillSummaryDto();
            dto.setId(hb.getId());
            dto.setHoldCode(hb.getHoldCode());
            dto.setCreatedAt(hb.getCreatedAt());
            dto.setCustomerName(hb.getCustomerName());
            dto.setEstimatedTotal(total);
            dto.setItemsCount(count);        

            summaries.add(dto);
        }
        return summaries;
    }

    @Override
    @Transactional(readOnly = true)
    public HoldBillDetailDto getHeldBillDetail(Long heldBillId) {
        HeldBill hb = heldBillRepository.findById(heldBillId)
                .orElseThrow(() -> new NotFoundException("Held bill not found with id: " + heldBillId));

        HoldBillDetailDto dto = new HoldBillDetailDto();
        dto.setId(hb.getId());
        dto.setHoldCode(hb.getHoldCode());
        dto.setCreatedAt(hb.getCreatedAt());
        dto.setCustomerName(hb.getCustomerName());
        dto.setCustomerPhone(hb.getCustomerPhone());
        dto.setNote(hb.getNote());

        BigDecimal total = BigDecimal.ZERO;
        List<SaleItemResponseDto> itemsDto = new ArrayList<>();

        if (hb.getItems() != null) {
            for (HeldBillItem item : hb.getItems()) {
                SaleItemResponseDto itemDto = new SaleItemResponseDto();
                itemDto.setProductId(item.getProduct().getId());
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setSkuCode(item.getProduct().getSkuCode());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setUnitPrice(item.getUnitPrice());
                itemDto.setDiscountAmount(item.getDiscountAmount());
                itemDto.setLineTotal(item.getLineTotal());

                itemsDto.add(itemDto);
                total = total.add(item.getLineTotal());
            }
        }

        dto.setItems(itemsDto);
        dto.setEstimatedTotal(total);
        return dto;
    }

    @Override
    @Transactional
    public void deleteHeldBill(Long heldBillId) {
        if (!heldBillRepository.existsById(heldBillId)) {
            throw new NotFoundException("Held bill not found with id: " + heldBillId);
        }
        heldBillRepository.deleteById(heldBillId);
    }

    // --------------- Helper mappers --------------------------------

    private SaleResponseDto toSaleResponse(Sale sale) {
        SaleResponseDto dto = new SaleResponseDto();
        dto.setSaleId(sale.getId());
        dto.setBillNumber(sale.getBillNumber());
        dto.setSaleDate(sale.getSaleDate());
        dto.setPaymentMode(sale.getPaymentMode().name());
        dto.setCustomerName(sale.getCustomerName());
        dto.setCustomerPhone(sale.getCustomerPhone());
        dto.setSubtotal(sale.getSubtotal());
        dto.setTotalDiscount(sale.getTotalDiscount());
        dto.setTaxAmount(sale.getTaxAmount());
        dto.setGrandTotal(sale.getGrandTotal());

        List<SaleItemResponseDto> itemsDto = new ArrayList<>();
        if (sale.getItems() != null) {
            for (SaleItem item : sale.getItems()) {
                SaleItemResponseDto itemDto = new SaleItemResponseDto();
                itemDto.setProductId(item.getProduct().getId());
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setSkuCode(item.getProduct().getSkuCode());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setUnitPrice(item.getUnitPrice());
                itemDto.setDiscountAmount(item.getDiscountAmount());
                itemDto.setLineTotal(item.getLineTotal());
                itemsDto.add(itemDto);
            }
        }

        dto.setItems(itemsDto);
        return dto;
    }

    private String generateBillNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
        long millisPart = System.currentTimeMillis() % 100000;
        return "SI-" + datePart + "-" + String.format("%05d", millisPart);
        // e.g. SI-20251126-01234
    }

    private String generateHoldCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        long millisPart = System.currentTimeMillis() % 100000;
        return "HB-" + datePart + "-" + String.format("%05d", millisPart);
    }
}
