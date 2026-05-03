package com.internship.tool.service;

import com.internship.tool.entity.RiskEvent;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.RiskEventRepository;
import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;
import com.internship.tool.dto.RiskEventFilterRequest;
import com.internship.tool.specification.RiskEventSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class RiskEventService {

    private static final Logger log = LoggerFactory.getLogger(RiskEventService.class);

    private final RiskEventRepository repository;
    private final AiService aiService;

    public RiskEventService(RiskEventRepository repository, AiService aiService) {
        this.repository = repository;
        this.aiService = aiService;
    }

    // 🔹 CREATE
    public RiskEventResponse createRiskEvent(RiskEventRequest request) {

        log.info("Creating RiskEvent with title: {}", request.getTitle());

        long start = System.currentTimeMillis();

        RiskEvent event = buildEntityFromRequest(new RiskEvent(), request);
        setAiDescriptionSafely(event, request);

        RiskEvent saved = repository.save(event);

        log.info("RiskEvent created successfully with ID: {}", saved.getId());
        log.debug("Create execution time: {} ms", System.currentTimeMillis() - start);

        return mapToResponse(saved);
    }

    // 🔹 GET ALL
    public List<RiskEventResponse> getAllRiskEvents() {
        log.info("Fetching all risk events");

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔹 GET ALL WITH PAGINATION
    public Page<RiskEventResponse> getAllWithPagination(Pageable pageable) {
        log.info("Fetching paginated risk events: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // 🔥 SEARCH (DTO BASED)
    public Page<RiskEventResponse> search(
            RiskEventFilterRequest filter,
            Pageable pageable
    ) {

        log.info("Searching RiskEvents with filter: {}", filter);

        Specification<RiskEvent> spec = Specification
                .where(RiskEventSpecification.hasKeyword(filter.getKeyword()))
                .and(RiskEventSpecification.hasCategory(filter.getCategory()))
                .and(RiskEventSpecification.hasSeverity(filter.getSeverity()))
                .and(RiskEventSpecification.hasStatus(filter.getStatus()));

        return repository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    // 🔹 GET BY ID
    public RiskEventResponse getById(Long id) {
        log.info("Fetching RiskEvent by ID: {}", id);

        return mapToResponse(getEntityOrThrow(id));
    }

    // 🔹 UPDATE
    public RiskEventResponse updateRiskEvent(Long id, RiskEventRequest request) {

        log.info("Updating RiskEvent ID: {}", id);

        RiskEvent event = getEntityOrThrow(id);

        buildEntityFromRequest(event, request);
        setAiDescriptionSafely(event, request);

        RiskEvent updated = repository.save(event);

        log.info("RiskEvent updated successfully with ID: {}", id);

        return mapToResponse(updated);
    }

    // 🔹 DELETE
    public void deleteRiskEvent(Long id) {

        log.warn("Deleting RiskEvent ID: {}", id);

        repository.delete(getEntityOrThrow(id));

        log.warn("RiskEvent deleted successfully with ID: {}", id);
    }

    // ===============================
    // 🔧 PRIVATE HELPERS
    // ===============================

    private RiskEvent getEntityOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("RiskEvent not found with ID: {}", id);
                    return new ResourceNotFoundException("RiskEvent not found with id: " + id);
                });
    }

    private RiskEvent buildEntityFromRequest(RiskEvent event, RiskEventRequest request) {
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setSeverity(request.getSeverity());
        event.setStatus(request.getStatus());
        return event;
    }

    private void setAiDescriptionSafely(RiskEvent event, RiskEventRequest request) {
        try {
            String aiDesc = aiService.generateRiskDescription(
                    request.getTitle(),
                    request.getDescription()
            );
            event.setAiDescription(aiDesc);
        } catch (Exception e) {
            log.error("AI service failed for title: {}", request.getTitle(), e);
            event.setAiDescription("AI service unavailable");
        }
    }

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