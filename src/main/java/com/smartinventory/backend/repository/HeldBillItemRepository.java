package com.smartinventory.backend.repository;

import com.smartinventory.backend.entity.HeldBillItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeldBillItemRepository extends JpaRepository<HeldBillItem, Long> {
}
