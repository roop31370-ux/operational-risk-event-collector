package com.internship.tool.repository;

import com.internship.tool.entity.RiskEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiskEventRepository extends JpaRepository<RiskEvent, Long> {

    // 🔍 Basic search (no pagination)
    List<RiskEvent> findByTitleContainingIgnoreCase(String keyword);

    // 🔍 Search with pagination
    Page<RiskEvent> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    // 🔍 Filter (case-insensitive)
    Page<RiskEvent> findByCategoryIgnoreCase(String category, Pageable pageable);
    Page<RiskEvent> findBySeverityIgnoreCase(String severity, Pageable pageable);
    Page<RiskEvent> findByStatusIgnoreCase(String status, Pageable pageable);

    // 🔥 ADVANCED COMBINED QUERY (IMPORTANT)
    Page<RiskEvent> findByTitleContainingIgnoreCaseAndCategoryContainingIgnoreCase(
            String keyword,
            String category,
            Pageable pageable
    );
}