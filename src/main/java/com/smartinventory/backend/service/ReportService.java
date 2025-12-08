package com.smartinventory.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartinventory.backend.dto.ProductWiseHistoryDto;
import com.smartinventory.backend.repository.ReportRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<ProductWiseHistoryDto> getProductWiseHistory() {
        return reportRepository.getProductWiseHistory();
    }
}
