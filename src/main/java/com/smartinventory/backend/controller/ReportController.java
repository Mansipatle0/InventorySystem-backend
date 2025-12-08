package com.smartinventory.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.backend.dto.ProductWiseHistoryDto;
import com.smartinventory.backend.service.ReportService;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/product-history")
    public List<ProductWiseHistoryDto> getProductHistory() {
        return reportService.getProductWiseHistory();
    }
}
