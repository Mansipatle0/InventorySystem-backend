package com.smartinventory.backend.dto;

import java.math.BigDecimal;

public class YearlyExcelRow {
    private int month;
    private long bills;
    private BigDecimal revenue;

    public YearlyExcelRow(int month, long bills, BigDecimal revenue) {
        this.month = month;
        this.bills = bills;
        this.revenue = revenue;
    }

    public int getMonth() { return month; }
    public long getBills() { return bills; }
    public BigDecimal getRevenue() { return revenue; }
}
