package com.internship.tool.service;

import com.internship.tool.entity.RiskEvent;
import com.internship.tool.repository.RiskEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskEventService {

    @Autowired
    private RiskEventRepository repository;

    // Save event
    public RiskEvent save(RiskEvent event) {
        return repository.save(event);
    }

    // Get all events
    public List<RiskEvent> getAll() {
        return repository.findAll();
    }
}