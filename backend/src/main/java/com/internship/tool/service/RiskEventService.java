package com.internship.tool.service;

import com.internship.tool.entity.RiskEvent;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.RiskEventRepository;
import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;
import com.internship.tool.specification.RiskEventSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskEventService {

    private final RiskEventRepository repository;
    private final AiService aiService;

    public RiskEventService(RiskEventRepository repository, AiService aiService) {
        this.repository = repository;
        this.aiService = aiService;
    }

    // 🔹 CREATE
    public RiskEventResponse createRiskEvent(RiskEventRequest request) {

        RiskEvent event = new RiskEvent();

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setSeverity(request.getSeverity());
        event.setStatus(request.getStatus());

        // 🔥 AI Integration
        String aiDesc = aiService.generateRiskDescription(
                request.getTitle(),
                request.getDescription()
        );
        event.setAiDescription(aiDesc);

        RiskEvent saved = repository.save(event);

        return mapToResponse(saved);
    }

    // 🔹 GET ALL
    public List<RiskEventResponse> getAllRiskEvents() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔹 GET ALL WITH PAGINATION
    public Page<RiskEventResponse> getAllWithPagination(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // 🔥 ADVANCED SEARCH
    public Page<RiskEventResponse> advancedSearch(
            String keyword,
            String category,
            String severity,
            String status,
            Pageable pageable
    ) {

        Specification<RiskEvent> spec = Specification
                .where(RiskEventSpecification.hasKeyword(keyword))
                .and(RiskEventSpecification.hasCategory(category))
                .and(RiskEventSpecification.hasSeverity(severity))
                .and(RiskEventSpecification.hasStatus(status));

        return repository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    // 🔹 GET BY ID
    public RiskEventResponse getById(Long id) {

        RiskEvent event = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("RiskEvent not found with id: " + id)
                );

        return mapToResponse(event);
    }

    // 🔹 UPDATE
    public RiskEventResponse updateRiskEvent(Long id, RiskEventRequest request) {

        RiskEvent event = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("RiskEvent not found with id: " + id)
                );

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setSeverity(request.getSeverity());
        event.setStatus(request.getStatus());

        // 🔥 Update AI
        String aiDesc = aiService.generateRiskDescription(
                request.getTitle(),
                request.getDescription()
        );
        event.setAiDescription(aiDesc);

        RiskEvent updated = repository.save(event);

        return mapToResponse(updated);
    }

    // 🔹 DELETE
    public void deleteRiskEvent(Long id) {

        RiskEvent event = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("RiskEvent not found with id: " + id)
                );

        repository.delete(event);
    }

    // 🔹 MAPPING
    private RiskEventResponse mapToResponse(RiskEvent event) {
        return RiskEventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(event.getCategory())
                .severity(event.getSeverity())
                .status(event.getStatus())
                .aiDescription(event.getAiDescription())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}