package com.smartinventory.backend.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductWiseHistoryDto {

    private String code;
    private String name;
    private String category;
    private Long totalQtySold;
    private BigDecimal totalRevenue;   // FIXED HERE
    private LocalDateTime lastSoldOn;

    public ProductWiseHistoryDto(
            String code,
            String name,
            String category,
            Long totalQtySold,
            BigDecimal totalRevenue,      // FIXED HERE
            LocalDateTime lastSoldOn
    ) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.totalQtySold = totalQtySold;
        this.totalRevenue = totalRevenue;
        this.lastSoldOn = lastSoldOn;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getTotalQtySold() {
		return totalQtySold;
	}

	public void setTotalQtySold(Long totalQtySold) {
		this.totalQtySold = totalQtySold;
	}

	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public LocalDateTime getLastSoldOn() {
		return lastSoldOn;
	}

	public void setLastSoldOn(LocalDateTime lastSoldOn) {
		this.lastSoldOn = lastSoldOn;
	}



}
