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
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eslclasshallrepositorybackend.dto.DtoMapper;
import com.cesde.eslclasshallrepositorybackend.dto.RecursoRequestDto;
import com.cesde.eslclasshallrepositorybackend.dto.RecursoResponseDto;
import com.cesde.eslclasshallrepositorybackend.service.RecursoService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Recursos", description = "CRUD y consulta pública de recursos educativos")
@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    @GetMapping
    public List<RecursoResponseDto> obtenerTodos() {
        return recursoService.obtenerTodos().stream().map(DtoMapper::toDto).toList();
    }

    @Operation(summary = "Consultar recursos aprobados")
    @GetMapping("/publicados")
    public List<RecursoResponseDto> obtenerPublicados() {
        return recursoService.obtenerPublicados().stream().map(DtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public RecursoResponseDto obtenerPorId(@PathVariable UUID id) {
        return DtoMapper.toDto(recursoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RecursoResponseDto> crear(@Valid @RequestBody RecursoRequestDto recurso) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toDto(recursoService.crear(DtoMapper.toEntity(recurso))));
    }

    @PutMapping("/{id}")
    public RecursoResponseDto actualizar(@PathVariable UUID id, @Valid @RequestBody RecursoRequestDto recurso) {
        return DtoMapper.toDto(recursoService.actualizar(id, DtoMapper.toEntity(recurso)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        recursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}