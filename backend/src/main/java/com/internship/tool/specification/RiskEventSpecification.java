package com.internship.tool.specification;

import com.internship.tool.entity.RiskEvent;
import org.springframework.data.jpa.domain.Specification;

public class RiskEventSpecification {

    // 🔍 Keyword search (title + description)
    public static Specification<RiskEvent> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String like = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("description")), like)
            );
        };
    }

    // 📂 Category filter
    public static Specification<RiskEvent> hasCategory(String category) {
        return (root, query, cb) ->
                (category == null || category.isBlank()) ? null :
                        cb.equal(root.get("category"), category);
    }

    // ⚠️ Severity filter
    public static Specification<RiskEvent> hasSeverity(String severity) {
        return (root, query, cb) ->
                (severity == null || severity.isBlank()) ? null :
                        cb.equal(root.get("severity"), severity);
    }

    // 📊 Status filter
    public static Specification<RiskEvent> hasStatus(String status) {
        return (root, query, cb) ->
                (status == null || status.isBlank()) ? null :
                        cb.equal(root.get("status"), status);
    }
}