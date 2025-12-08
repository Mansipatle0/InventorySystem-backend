package com.smartinventory.backend.repository;

import com.smartinventory.backend.entity.HeldBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeldBillRepository extends JpaRepository<HeldBill, Long> {

    Optional<HeldBill> findByHoldCode(String holdCode);
}
