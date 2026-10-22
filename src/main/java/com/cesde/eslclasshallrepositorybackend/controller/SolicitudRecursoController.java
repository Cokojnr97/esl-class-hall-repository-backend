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

import com.cesde.eslclasshallrepositorybackend.model.entity.SolicitudRecurso;
import com.cesde.eslclasshallrepositorybackend.service.SolicitudRecursoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Solicitudes", description = "Flujo de aprobación de recursos por administradores")
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudRecursoController {

    private final SolicitudRecursoService solicitudService;

    public SolicitudRecursoController(SolicitudRecursoService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public List<SolicitudRecurso> obtenerTodos() {
        return solicitudService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public SolicitudRecurso obtenerPorId(@PathVariable UUID id) {
        return solicitudService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<SolicitudRecurso> crear(@RequestBody SolicitudRecurso solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.crear(solicitud));
    }

    @PutMapping("/{id}")
    public SolicitudRecurso actualizar(@PathVariable UUID id, @RequestBody SolicitudRecurso solicitud) {
        return solicitudService.actualizar(id, solicitud);
    }

    @Operation(summary = "Aprobar una solicitud y publicar su recurso")
    @PostMapping("/{id}/aprobar")
    public SolicitudRecurso aprobar(@PathVariable UUID id, @RequestParam UUID adminId) {
        return solicitudService.aprobar(id, adminId);
    }

    @Operation(summary = "Rechazar una solicitud")
    @PostMapping("/{id}/rechazar")
    public SolicitudRecurso rechazar(@PathVariable UUID id, @RequestParam UUID adminId) {
        return solicitudService.rechazar(id, adminId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        solicitudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}