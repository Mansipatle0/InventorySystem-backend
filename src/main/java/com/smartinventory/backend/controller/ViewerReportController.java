package com.smartinventory.backend.controller;

import com.smartinventory.backend.dto.MonthlySalesRowDto;
import com.smartinventory.backend.dto.YearlyExcelRow;
import com.smartinventory.backend.dto.product.FastSlowMoverDto;
import com.smartinventory.backend.service.ExcelExportService;
import com.smartinventory.backend.service.FastSlowMoverService;
import com.smartinventory.backend.repository.ReportRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/viewer/reports")
@CrossOrigin(origins = "*")
public class ViewerReportController {

    private final ReportRepository reportRepository;
    private final ExcelExportService excelExportService;
    private final FastSlowMoverService moverService;

    public ViewerReportController(
            ReportRepository reportRepository,
            ExcelExportService excelExportService,
            FastSlowMoverService moverService
    ) {
        this.reportRepository = reportRepository;
        this.excelExportService = excelExportService;
        this.moverService = moverService;
    }


    @GetMapping("/monthly")
    public List<MonthlySalesRowDto> getMonthly(@RequestParam int year) {
        return reportRepository.getMonthlySales(year);
    }

    @GetMapping("/yearly-summary")
    public List<YearlyExcelRow> getYearlySummary(@RequestParam int year) {

        List<Object[]> raw = reportRepository.getYearlySummaryRaw(year);

        return raw.stream()
                .map(row -> new YearlyExcelRow(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).longValue(),
                        (java.math.BigDecimal) row[2]
                ))
                .toList();
    }

    @GetMapping("/yearly/export")
    public ResponseEntity<byte[]> exportYearly(@RequestParam int year) {
        try {
            ByteArrayInputStream excelStream = excelExportService.generateYearlyExcel(year);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",
                    "attachment; filename=Yearly-Report-" + year + ".xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(excelStream.readAllBytes());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/fast-movers")
    public List<FastSlowMoverDto> getFastMovers() {
        return moverService.getFastMovers();
    }

    @GetMapping("/slow-movers")
    public List<FastSlowMoverDto> getSlowMovers() {
        return moverService.getSlowMovers();
    }
}
