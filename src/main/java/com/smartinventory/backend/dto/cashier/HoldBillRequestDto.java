package com.smartinventory.backend.dto.cashier;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class HoldBillRequestDto {

    @NotEmpty
    private List<CartItemDto> cartItems;

    private String customerName;
    private String customerPhone;
    private String note;

    private Long cashierId;

	public List<CartItemDto> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItemDto> cartItems) {
		this.cartItems = cartItems;
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

	public Long getCashierId() {
		return cashierId;
	}

	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}

  
}
