package com.r.backend.controller;

import com.r.backend.entity.RiskEvent;
import com.r.backend.service.RiskEventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-events")
public class RiskEventController {

    private final RiskEventService service;

    public RiskEventController(RiskEventService service) {
        this.service = service;
    }

    @PostMapping
    public RiskEvent create(@RequestBody RiskEvent event) {
        return service.save(event);
    }

    @GetMapping
    public List<RiskEvent> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public RiskEvent getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}