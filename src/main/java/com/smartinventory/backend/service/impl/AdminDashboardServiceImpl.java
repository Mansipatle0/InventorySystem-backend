package com.smartinventory.backend.service.impl;



import org.springframework.stereotype.Service; 

import com.smartinventory.backend.dto.AdminOverviewDto;
import com.smartinventory.backend.repository.ProductRepository;
import com.smartinventory.backend.repository.SaleRepository;
import com.smartinventory.backend.repository.UserRepository;
import com.smartinventory.backend.service.AdminDashboardService;

import java.math.BigDecimal;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

 private final ProductRepository productRepository;
 private final SaleRepository saleRepository;
 private final UserRepository userRepository;

 public AdminDashboardServiceImpl(ProductRepository productRepository,
                                  SaleRepository saleRepository,
                                  UserRepository userRepository) {
     this.productRepository = productRepository;
     this.saleRepository = saleRepository;
     this.userRepository = userRepository;
 }

 @Override
 public AdminOverviewDto getOverviewMetrics() {
     AdminOverviewDto dto = new AdminOverviewDto();

     long totalProducts = productRepository.countActiveProducts();
     long lowStockItems = productRepository.countLowStockItems();
     long activeUsers = userRepository.countActiveUsers();

     BigDecimal todaySales = saleRepository.getTodayCompletedSalesTotal();
     if (todaySales == null) {
         todaySales = BigDecimal.ZERO;
     }

     dto.setTotalProducts(totalProducts);
     dto.setLowStockItems(lowStockItems);
     dto.setActiveUsers(activeUsers);
     dto.setTodaySales(todaySales);

     return dto;
 }
}
