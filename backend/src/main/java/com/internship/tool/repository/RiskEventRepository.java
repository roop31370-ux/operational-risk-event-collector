package com.internship.tool.repository;

import com.internship.tool.entity.RiskEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskEventRepository extends JpaRepository<RiskEvent, Long> {

    List<RiskEvent> findByTitleContainingIgnoreCase(String keyword);
}