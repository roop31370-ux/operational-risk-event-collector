package com.internship.tool.controller;

import com.internship.tool.dto.ApiResponse;
import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;
import com.internship.tool.service.RiskEventService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<RiskEventResponse>> create(
            @Valid @RequestBody RiskEventRequest request) {

        RiskEventResponse res = service.createRiskEvent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<RiskEventResponse>builder()
                        .success(true)
                        .message("RiskEvent created successfully")
                        .data(res)
                        .build()
        );
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<RiskEventResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<RiskEventResponse>>builder()
                        .success(true)
                        .message("Fetched all risk events")
                        .data(service.getAllRiskEvents())
                        .build()
        );
    }

    // 🔥 PAGINATION
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<RiskEventResponse>>> getAllPaged(Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.<Page<RiskEventResponse>>builder()
                        .success(true)
                        .message("Fetched paginated risk events")
                        .data(service.getAllWithPagination(pageable))
                        .build()
        );
    }

    // 🔥 UPDATED — ADVANCED SEARCH
    @GetMapping("/advanced")
    public ResponseEntity<ApiResponse<Page<RiskEventResponse>>> advanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.<Page<RiskEventResponse>>builder()
                        .success(true)
                        .message("Fetched filtered risk events")
                        .data(service.advancedSearch(keyword, category, severity, status, pageable))
                        .build()
        );
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RiskEventResponse>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<RiskEventResponse>builder()
                        .success(true)
                        .message("RiskEvent fetched successfully")
                        .data(service.getById(id))
                        .build()
        );
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RiskEventResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody RiskEventRequest request) {

        RiskEventResponse updated = service.updateRiskEvent(id, request);

        return ResponseEntity.ok(
                ApiResponse.<RiskEventResponse>builder()
                        .success(true)
                        .message("RiskEvent updated successfully")
                        .data(updated)
                        .build()
        );
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {

        service.deleteRiskEvent(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("RiskEvent deleted successfully")
                        .data(null)
                        .build()
        );
    }
}