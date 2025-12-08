package com.smartinventory.backend.controller;

//import com.smartinventory.backend.model.Sale;
import com.smartinventory.backend.entity.Sale;
import com.smartinventory.backend.service.SaleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/daily")
    public List<Sale> getDailySales(@RequestParam("date") String date) {
        return saleService.getSalesByDate(date);
    }
}