package com.smartinventory.backend.dto.product;

import java.math.BigDecimal;

public class FastSlowMoverDto {

    private String code;
    private String name;
    private String category;
    private Long qtySold;
    private BigDecimal revenue;

    public FastSlowMoverDto(String code, String name, String category, Long qtySold, BigDecimal revenue) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.qtySold = qtySold;
        this.revenue = revenue;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Long getQtySold() { return qtySold; }
    public BigDecimal getRevenue() { return revenue; }
}
