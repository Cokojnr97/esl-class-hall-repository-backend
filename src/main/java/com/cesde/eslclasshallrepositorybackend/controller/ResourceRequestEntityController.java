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

import com.cesde.eslclasshallrepositorybackend.model.entity.ResourceRequestEntity;
import com.cesde.eslclasshallrepositorybackend.service.ResourceRequestEntityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Requests", description = "Approval workflow de resources por administrators")
@RestController
@RequestMapping("/api/requests")
public class ResourceRequestEntityController {

    private final ResourceRequestEntityService solicitudService;

    public ResourceRequestEntityController(ResourceRequestEntityService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public List<ResourceRequestEntity> getAll() {
        return solicitudService.getAll();
    }

    @GetMapping("/{id}")
    public ResourceRequestEntity getById(@PathVariable UUID id) {
        return solicitudService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResourceRequestEntity> create(@RequestBody ResourceRequestEntity solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.create(solicitud));
    }

    @PutMapping("/{id}")
    public ResourceRequestEntity update(@PathVariable UUID id, @RequestBody ResourceRequestEntity solicitud) {
        return solicitudService.update(id, solicitud);
    }

    @Operation(summary = "Aprobar una solicitud y publicar su recurso")
    @PostMapping("/{id}/aprobar")
    public ResourceRequestEntity aprobar(@PathVariable UUID id, @RequestParam UUID adminId) {
        return solicitudService.aprobar(id, adminId);
    }

    @Operation(summary = "Rechazar una solicitud")
    @PostMapping("/{id}/rechazar")
    public ResourceRequestEntity rechazar(@PathVariable UUID id, @RequestParam UUID adminId) {
        return solicitudService.rechazar(id, adminId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        solicitudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
