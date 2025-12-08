package com.smartinventory.backend.service;

import com.smartinventory.backend.dto.product.FastSlowMoverDto;
import com.smartinventory.backend.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FastSlowMoverService {

    private final ReportRepository repo;

    public FastSlowMoverService(ReportRepository repo) {
        this.repo = repo;
    }

    public List<FastSlowMoverDto> getFastMovers() {
        List<Object[]> raw = repo.getFastMovers();
        return convert(raw);
    }

    public List<FastSlowMoverDto> getSlowMovers() {
        List<Object[]> raw = repo.getSlowMovers();
        return convert(raw);
    }

    private List<FastSlowMoverDto> convert(List<Object[]> raw) {
        return raw.stream()
                .map(r -> new FastSlowMoverDto(
                        (String) r[0],                   // code
                        (String) r[1],                   // name
                        (String) r[2],                   // category
                        ((Number) r[3]).longValue(),     // qtySold
                        (BigDecimal) r[4]               // revenue
                ))
                .toList();
    }
}
