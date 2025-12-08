package com.smartinventory.backend.service;

import com.smartinventory.backend.dto.YearlyExcelRow;
import com.smartinventory.backend.repository.ReportRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelExportService {

    private final ReportRepository reportRepo;

    // Month names array
    private static final String[] MONTH_NAMES = {
            "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public ExcelExportService(ReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    public ByteArrayInputStream generateYearlyExcel(int year) throws Exception {

        List<Object[]> raw = reportRepo.getYearlySummaryRaw(year);

        List<YearlyExcelRow> rows = new ArrayList<>();
        for (Object[] obj : raw) {
            rows.add(new YearlyExcelRow(
                    ((Number) obj[0]).intValue(),     // month
                    ((Number) obj[1]).longValue(),    // bills
                    (java.math.BigDecimal) obj[2]     // revenue
            ));
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Yearly Report");

        // Header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Month");
        header.createCell(1).setCellValue("Bills");
        header.createCell(2).setCellValue("Revenue");

        int rowIndex = 1;
        for (YearlyExcelRow r : rows) {
            Row row = sheet.createRow(rowIndex++);

            // ⭐ Convert number → name (11 → Nov, 12 → Dec)
            String monthName = MONTH_NAMES[r.getMonth()];
            row.createCell(0).setCellValue(monthName);

            row.createCell(1).setCellValue(r.getBills());
            row.createCell(2).setCellValue(r.getRevenue().doubleValue());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
