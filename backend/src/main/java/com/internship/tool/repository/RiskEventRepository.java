package com.internship.tool.repository;

import com.internship.tool.entity.RiskEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RiskEventRepository
        extends JpaRepository<RiskEvent, Long>,
                JpaSpecificationExecutor<RiskEvent> {

    // No custom methods needed
}