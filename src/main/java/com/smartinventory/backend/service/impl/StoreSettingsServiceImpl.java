package com.smartinventory.backend.service.impl;

import org.springframework.stereotype.Service;

import com.smartinventory.backend.dto.StoreSettingsDto;
import com.smartinventory.backend.entity.StoreSettings;
import com.smartinventory.backend.repository.StoreSettingsRepository;
import com.smartinventory.backend.service.StoreSettingsService;

import java.math.BigDecimal;

@Service
public class StoreSettingsServiceImpl implements StoreSettingsService {

 private final StoreSettingsRepository repository;

 public StoreSettingsServiceImpl(StoreSettingsRepository repository) {
     this.repository = repository;
 }

 @Override
 public StoreSettingsDto getSettings() {
     StoreSettings entity = repository.findFirstSettings();
     if (entity == null) {
         entity = new StoreSettings();
         entity.setShopName("My SmartInventory Store");
         entity.setInvoicePrefix("BILL-");
         entity.setTaxPercent(BigDecimal.ZERO);
         entity.setDiscountPercent(BigDecimal.ZERO);
         entity = repository.save(entity);
     }
     return toDto(entity);
 }

 @Override
 public StoreSettingsDto saveSettings(StoreSettingsDto dto) {
     StoreSettings entity = repository.findFirstSettings();
     if (entity == null) {
         entity = new StoreSettings();
     }

     entity.setShopName(dto.getShopName());
     entity.setGstNumber(dto.getGstNumber());
     entity.setAddress(dto.getAddress());
     entity.setInvoicePrefix(dto.getInvoicePrefix());
     entity.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : BigDecimal.ZERO);
     entity.setDiscountPercent(dto.getDiscountPercent() != null ? dto.getDiscountPercent() : BigDecimal.ZERO);

     entity = repository.save(entity);
     return toDto(entity);
 }

 private StoreSettingsDto toDto(StoreSettings e) {
     StoreSettingsDto dto = new StoreSettingsDto();
     dto.setShopName(e.getShopName());
     dto.setGstNumber(e.getGstNumber());
     dto.setAddress(e.getAddress());
     dto.setInvoicePrefix(e.getInvoicePrefix());
     dto.setTaxPercent(e.getTaxPercent());
     dto.setDiscountPercent(e.getDiscountPercent());
     return dto;
 }
}
