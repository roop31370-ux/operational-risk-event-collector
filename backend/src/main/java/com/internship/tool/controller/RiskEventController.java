package com.internship.tool.controller;

import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;
import com.internship.tool.service.RiskEventService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-events")
public class RiskEventController {

    @Autowired
    private RiskEventService service;

    // ✅ CREATE API
    @PostMapping
    public RiskEventResponse create(@Valid @RequestBody RiskEventRequest request) {
        return service.createRiskEvent(request);
    }

    // ✅ GET ALL API
    @GetMapping
    public List<RiskEventResponse> getAll() {
        return service.getAllRiskEvents();
    }

    // ✅ GET BY ID API
    @GetMapping("/{id}")
    public RiskEventResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }
}