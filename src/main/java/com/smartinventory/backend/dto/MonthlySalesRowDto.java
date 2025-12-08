package com.smartinventory.backend.dto;

import java.math.BigDecimal;

public class MonthlySalesRowDto {

    private String month;
    private Long bills;
    private Long itemsSold;
    private BigDecimal revenue;

    public MonthlySalesRowDto() {}

    public MonthlySalesRowDto(Integer monthNumber, Long bills, Long itemsSold, BigDecimal revenue) {
        this.month = convertMonth(monthNumber);
        this.bills = bills;
        this.itemsSold = itemsSold;
        this.revenue = revenue;
    }

    private String convertMonth(Integer num) {
        return switch (num) {
            case 1 -> "Jan";
            case 2 -> "Feb";
            case 3 -> "Mar";
            case 4 -> "Apr";
            case 5 -> "May";
            case 6 -> "Jun";
            case 7 -> "Jul";
            case 8 -> "Aug";
            case 9 -> "Sep";
            case 10 -> "Oct";
            case 11 -> "Nov";
            case 12 -> "Dec";
            default -> "-";
        };
    }

    public String getMonth() { return month; }
    public Long getBills() { return bills; }
    public Long getItemsSold() { return itemsSold; }
    public BigDecimal getRevenue() { return revenue; }
}
