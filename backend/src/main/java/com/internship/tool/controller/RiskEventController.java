package com.internship.tool.controller;

import com.internship.tool.entity.RiskEvent;
import com.internship.tool.service.RiskEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-events")
public class RiskEventController {

    @Autowired
    private RiskEventService service;

    // POST API → create event
    @PostMapping
    public RiskEvent create(@RequestBody RiskEvent event) {
        return service.save(event);
    }

    // GET API → fetch all events
    @GetMapping
    public List<RiskEvent> getAll() {
        return service.getAll();
    }
}