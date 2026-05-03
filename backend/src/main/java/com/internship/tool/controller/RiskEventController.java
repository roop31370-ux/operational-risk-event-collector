package com.internship.tool.controller;

import com.internship.tool.dto.ApiResponse;
import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;
import com.internship.tool.service.RiskEventService;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
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

    // 🔥 PAGINATION + SORT (Swagger Friendly)
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<RiskEventResponse>>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortParams[0])
        );

        return ResponseEntity.ok(
                ApiResponse.<Page<RiskEventResponse>>builder()
                        .success(true)
                        .message("Fetched paginated & sorted risk events")
                        .data(service.getAllWithPagination(pageable))
                        .build()
        );
    }

    // 🔥 ADVANCED SEARCH + SORT
    @GetMapping("/advanced")
    public ResponseEntity<ApiResponse<Page<RiskEventResponse>>> advanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortParams[0])
        );

        return ResponseEntity.ok(
                ApiResponse.<Page<RiskEventResponse>>builder()
                        .success(true)
                        .message("Fetched filtered & sorted risk events")
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