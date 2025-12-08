package com.smartinventory.backend.dto.cashier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class HoldBillDetailDto {

    private Long id;
    private String holdCode;
    private LocalDateTime createdAt;

    private String customerName;
    private String customerPhone;
    private String note;

    private BigDecimal estimatedTotal;
    private List<SaleItemResponseDto> items;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHoldCode() {
		return holdCode;
	}
	public void setHoldCode(String holdCode) {
		this.holdCode = holdCode;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public BigDecimal getEstimatedTotal() {
		return estimatedTotal;
	}
	public void setEstimatedTotal(BigDecimal estimatedTotal) {
		this.estimatedTotal = estimatedTotal;
	}
	public List<SaleItemResponseDto> getItems() {
		return items;
	}
	public void setItems(List<SaleItemResponseDto> items) {
		this.items = items;
	}

 
}
