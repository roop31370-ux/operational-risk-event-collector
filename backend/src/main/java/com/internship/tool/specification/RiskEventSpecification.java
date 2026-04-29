package com.internship.tool.specification;

import com.internship.tool.entity.RiskEvent;
import org.springframework.data.jpa.domain.Specification;

public class RiskEventSpecification {

    // 🔍 keyword search
    public static Specification<RiskEvent> hasKeyword(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("title")),
                                  "%" + keyword.toLowerCase() + "%");
    }

    // 🔎 category filter
    public static Specification<RiskEvent> hasCategory(String category) {
        return (root, query, cb) ->
                category == null || category.isEmpty()
                        ? null
                        : cb.equal(cb.lower(root.get("category")),
                                   category.toLowerCase());
    }

    // 🔥 severity filter
    public static Specification<RiskEvent> hasSeverity(String severity) {
        return (root, query, cb) ->
                severity == null || severity.isEmpty()
                        ? null
                        : cb.equal(cb.lower(root.get("severity")),
                                   severity.toLowerCase());
    }

    // 📌 status filter
    public static Specification<RiskEvent> hasStatus(String status) {
        return (root, query, cb) ->
                status == null || status.isEmpty()
                        ? null
                        : cb.equal(cb.lower(root.get("status")),
                                   status.toLowerCase());
    }
}