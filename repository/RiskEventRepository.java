package com.r.backend.repository;

import com.r.backend.entity.RiskEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskEventRepository extends JpaRepository<RiskEvent, Long> {
}