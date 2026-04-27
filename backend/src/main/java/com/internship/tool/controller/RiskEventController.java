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

    // 🔥 PAGINATION (basic)
    @GetMapping("/paged")
    public Page<RiskEventResponse> getAllPaged(Pageable pageable) {
        return service.getAllWithPagination(pageable);
    }

    // 🔥🔥 DAY 6 — ADVANCED (Single Powerful API)
    @GetMapping("/advanced")
    public Page<RiskEventResponse> advanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return service.advancedSearch(keyword, category, severity, status, page, size);
    }

    // ✅ GET BY ID (KEEP LAST)
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