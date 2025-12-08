package com.smartinventory.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartinventory.backend.entity.StoreSettings;

public interface StoreSettingsRepository extends JpaRepository<StoreSettings, Long> {

    @Query(value = "SELECT * FROM store_settings LIMIT 1", nativeQuery = true)
    StoreSettings findFirstSettings();
}