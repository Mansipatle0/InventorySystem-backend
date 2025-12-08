package com.smartinventory.backend.dto.cashier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoldBillSummaryDto {

    private Long id;
    private String holdCode;
    private LocalDateTime createdAt;
    private String customerName;
    private BigDecimal estimatedTotal;
    private Integer itemsCount;
	public Integer getItemsCount() {
		return itemsCount;
	}
	public void setItemsCount(Integer itemsCount) {
		this.itemsCount = itemsCount;
	}
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
	public BigDecimal getEstimatedTotal() {
		return estimatedTotal;
	}
	public void setEstimatedTotal(BigDecimal estimatedTotal) {
		this.estimatedTotal = estimatedTotal;
	}

    
}
