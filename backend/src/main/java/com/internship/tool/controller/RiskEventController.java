package com.internship.tool.controller;

import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;
import com.internship.tool.service.RiskEventService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-events")
public class RiskEventController {

    private final RiskEventService service;

    public RiskEventController(RiskEventService service) {
        this.service = service;
    }

    // ✅ CREATE
    @PostMapping
    public RiskEventResponse create(@Valid @RequestBody RiskEventRequest request) {
        return service.createRiskEvent(request);
    }

    // ✅ GET ALL
    @GetMapping
    public List<RiskEventResponse> getAll() {
        return service.getAllRiskEvents();
    }

    // 🔥 PAGINATION
    @GetMapping("/paged")
    public Page<RiskEventResponse> getAllPaged(Pageable pageable) {
        return service.getAllWithPagination(pageable);
    }

    // 🔍 SEARCH
    @GetMapping("/search")
    public Page<RiskEventResponse> search(
            @RequestParam String keyword,
            Pageable pageable) {

        return service.searchByTitle(keyword, pageable);
    }

    // 🔎 FILTER
    @GetMapping("/filter")
    public Page<RiskEventResponse> filter(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            Pageable pageable) {

        if (category != null && !category.isEmpty()) {
            return service.getByCategory(category, pageable);
        }

        if (severity != null && !severity.isEmpty()) {
            return service.getBySeverity(severity, pageable);
        }

        if (status != null && !status.isEmpty()) {
            return service.getByStatus(status, pageable);
        }

        return service.getAllWithPagination(pageable);
    }

    // 🔥🔥 ADVANCED (VERY IMPORTANT — keep above {id})
    @GetMapping("/advanced")
    public Page<RiskEventResponse> advanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return service.advancedSearch(keyword, category, page, size);
    }

    // ✅ GET BY ID (KEEP THIS LAST ALWAYS)
    @GetMapping("/{id}")
    public RiskEventResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public RiskEventResponse update(
            @PathVariable Long id,
            @Valid @RequestBody RiskEventRequest request) {

        return service.updateRiskEvent(id, request);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteRiskEvent(id);
        return "RiskEvent deleted successfully";
    }
}