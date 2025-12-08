package com.smartinventory.backend.controller;


import org.springframework.web.bind.annotation.*;

import com.smartinventory.backend.dto.StoreSettingsDto;
import com.smartinventory.backend.service.StoreSettingsService;

@RestController
@RequestMapping("/api/admin/settings")
@CrossOrigin(origins = "http://localhost:4200")
public class StoreSettingsController {

 private final StoreSettingsService service;

 public StoreSettingsController(StoreSettingsService service) {
     this.service = service;
 }

 @GetMapping
 public StoreSettingsDto getSettings() {
     return service.getSettings();
 }

 @PutMapping
 public StoreSettingsDto saveSettings(@RequestBody StoreSettingsDto dto) {
     return service.saveSettings(dto);
 }
}
