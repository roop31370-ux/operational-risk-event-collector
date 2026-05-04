package com.internship.tool.controller;

import com.internship.tool.dto.ApiResponse;
import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;
import com.internship.tool.dto.RiskEventFilterRequest;
import com.internship.tool.service.RiskEventService;

import jakarta.validation.Valid;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Swagger imports (ONLY THESE)
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/risk-events")
@Tag(name = "Risk Events", description = "APIs for managing risk events")
@SecurityRequirement(name = "bearerAuth")
public class RiskEventController {

    private final RiskEventService service;

    public RiskEventController(RiskEventService service) {
        this.service = service;
    }

    // ✅ CREATE
    @Operation(summary = "Create risk event", description = "Create a new risk event (ADMIN only)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "RiskEvent created successfully"
    )
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
    @Operation(summary = "Get all risk events", description = "Fetch all risk events")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Fetched successfully"
    )
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

    // 🔥 PAGINATION + SORT
    @Operation(summary = "Get paginated risk events", description = "Fetch risk events with pagination and sorting")
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

    // 🔥 SEARCH
    @Operation(summary = "Search risk events", description = "Filter risk events using multiple criteria")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<RiskEventResponse>>> search(
            @RequestBody RiskEventFilterRequest filter,
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
                        .data(service.search(filter, pageable))
                        .build()
        );
    }

    // ✅ GET BY ID
    @Operation(summary = "Get risk event by ID", description = "Fetch a specific risk event")
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
    @Operation(summary = "Update risk event", description = "Update an existing risk event (ADMIN only)")
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
    @Operation(summary = "Delete risk event", description = "Delete a risk event (ADMIN only)")
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