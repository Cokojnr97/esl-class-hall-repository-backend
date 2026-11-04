package com.cesde.eslclasshallrepositorybackend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eslclasshallrepositorybackend.dto.ResourceRequest;
import com.cesde.eslclasshallrepositorybackend.dto.ResourceResponse;
import com.cesde.eslclasshallrepositorybackend.model.entity.Resource;
import com.cesde.eslclasshallrepositorybackend.model.enums.CourseLevel;
import com.cesde.eslclasshallrepositorybackend.service.ResourceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Resources", description = "CRUD y consulta public de resources educativos")
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<ResourceResponse> getAll() {
        return resourceService.getAll().stream().map(resourceService::toResponse).toList();
    }

    @Operation(summary = "Consultar resources aprobados")
    @GetMapping("/published")
    public List<ResourceResponse> getPublished(@RequestParam(required = false) CourseLevel level) {
        return resourceService.getPublished(level).stream().map(resourceService::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResourceResponse getById(@PathVariable UUID id) {
        return resourceService.toResponse(resourceService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> create(@Valid @RequestBody ResourceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resourceService.toResponse(resourceService.create(request)));
    }

    @PutMapping("/{id}")
    public ResourceResponse update(@PathVariable UUID id, @Valid @RequestBody ResourceRequest request) {
        return resourceService.toResponse(resourceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
