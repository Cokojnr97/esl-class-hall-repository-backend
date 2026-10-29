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

import com.cesde.eslclasshallrepositorybackend.dto.DtoMapper;
import com.cesde.eslclasshallrepositorybackend.dto.SolicitudRecursoRequestDto;
import com.cesde.eslclasshallrepositorybackend.dto.SolicitudRecursoResponseDto;
import com.cesde.eslclasshallrepositorybackend.service.SolicitudRecursoService;

import jakarta.validation.Valid;
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
    public List<SolicitudRecursoResponseDto> obtenerTodos() {
        return solicitudService.obtenerTodos().stream().map(DtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public SolicitudRecursoResponseDto obtenerPorId(@PathVariable UUID id) {
        return DtoMapper.toDto(solicitudService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SolicitudRecursoResponseDto> crear(@Valid @RequestBody SolicitudRecursoRequestDto solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toDto(solicitudService.crear(DtoMapper.toEntity(solicitud))));
    }

    @PutMapping("/{id}")
    public SolicitudRecursoResponseDto actualizar(@PathVariable UUID id,
            @Valid @RequestBody SolicitudRecursoRequestDto solicitud) {
        return DtoMapper.toDto(solicitudService.actualizar(id, DtoMapper.toEntity(solicitud)));
    }

    @Operation(summary = "Aprobar una solicitud y publicar su recurso")
    @PostMapping("/{id}/aprobar")
    public SolicitudRecursoResponseDto aprobar(@PathVariable UUID id, @RequestParam UUID adminId) {
        return DtoMapper.toDto(solicitudService.aprobar(id, adminId));
    }

    @Operation(summary = "Rechazar una solicitud")
    @PostMapping("/{id}/rechazar")
    public SolicitudRecursoResponseDto rechazar(@PathVariable UUID id, @RequestParam UUID adminId) {
        return DtoMapper.toDto(solicitudService.rechazar(id, adminId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        solicitudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}