package com.internship.tool.service;

import com.internship.tool.entity.RiskEvent;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.RiskEventRepository;
import com.internship.tool.dto.RiskEventRequest;
import com.internship.tool.dto.RiskEventResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskEventService {

    private final RiskEventRepository repository;

    public RiskEventService(RiskEventRepository repository) {
        this.repository = repository;
    }

    // 🔹 CREATE
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

    // 🔹 SEARCH
    public Page<RiskEventResponse> searchByTitle(String keyword, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCase(
                        keyword == null ? "" : keyword,
                        pageable)
                .map(this::mapToResponse);
    }

    // 🔹 FILTERS (case-insensitive)
    public Page<RiskEventResponse> getByCategory(String category, Pageable pageable) {
        return repository.findByCategoryIgnoreCase(category, pageable)
                .map(this::mapToResponse);
    }

    public Page<RiskEventResponse> getBySeverity(String severity, Pageable pageable) {
        return repository.findBySeverityIgnoreCase(severity, pageable)
                .map(this::mapToResponse);
    }

    public Page<RiskEventResponse> getByStatus(String status, Pageable pageable) {
        return repository.findByStatusIgnoreCase(status, pageable)
                .map(this::mapToResponse);
    }

    // 🔥 ADVANCED (SEARCH + FILTER + PAGINATION)
    public Page<RiskEventResponse> advancedSearch(
            String keyword,
            String category,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<RiskEvent> result = repository
                .findByTitleContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                        keyword == null ? "" : keyword,
                        category == null ? "" : category,
                        pageable
                );

        return result.map(this::mapToResponse);
    }

    // 🔹 GET BY ID
    public RiskEventResponse getById(Long id) {

        RiskEvent event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "RiskEvent not found with id: " + id
                ));

        return mapToResponse(event);
    }

    // 🔹 UPDATE
    public RiskEventResponse updateRiskEvent(Long id, RiskEventRequest request) {

        RiskEvent event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "RiskEvent not found with id: " + id
                ));

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setSeverity(request.getSeverity());
        event.setStatus(request.getStatus());

        RiskEvent updated = repository.save(event);

        return mapToResponse(updated);
    }

    // 🔹 DELETE
    public void deleteRiskEvent(Long id) {

        RiskEvent event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "RiskEvent not found with id: " + id
                ));

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
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}