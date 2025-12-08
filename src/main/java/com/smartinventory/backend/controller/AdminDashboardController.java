package com.smartinventory.backend.controller;


import org.springframework.web.bind.annotation.*;

import com.smartinventory.backend.dto.AdminOverviewDto;
import com.smartinventory.backend.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "http://localhost:4200") // adjust if needed
public class AdminDashboardController {

 private final AdminDashboardService dashboardService;

 public AdminDashboardController(AdminDashboardService dashboardService) {
     this.dashboardService = dashboardService;
 }

 @GetMapping("/overview")
 public AdminOverviewDto getOverview() {
     return dashboardService.getOverviewMetrics();
 }
}
