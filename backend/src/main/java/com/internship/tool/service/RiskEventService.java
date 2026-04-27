package com.internship.tool.service;


import com.internship.tool.entity.RiskEvent;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.RiskEventRepository;
import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskEventService {

    @Autowired
    private RiskEventRepository repository;

    // 🔹 CREATE METHOD
    public RiskEventResponse createRiskEvent(RiskEventRequest request) {

        RiskEvent event = new RiskEvent();

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setSeverity(request.getSeverity());
        event.setStatus(request.getStatus());

        RiskEvent saved = repository.save(event);

        return mapToResponse(saved);
    }

    // 🔹 GET ALL METHOD
    public List<RiskEventResponse> getAllRiskEvents() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    } 

    public RiskEventResponse getById(Long id) {

    RiskEvent event = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "RiskEvent not found with id: " + id
            ));

    return mapToResponse(event);
   }

    // 🔹 🔥 PASTE HERE (mapping method)
    public RiskEventResponse mapToResponse(RiskEvent event) {
        return RiskEventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(event.getCategory())
                .severity(event.getSeverity())
                .status(event.getStatus())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}