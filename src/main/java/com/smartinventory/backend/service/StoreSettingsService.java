package com.smartinventory.backend.service;

import com.smartinventory.backend.dto.StoreSettingsDto;


public interface StoreSettingsService {
    StoreSettingsDto getSettings();
    StoreSettingsDto saveSettings(StoreSettingsDto dto);
}