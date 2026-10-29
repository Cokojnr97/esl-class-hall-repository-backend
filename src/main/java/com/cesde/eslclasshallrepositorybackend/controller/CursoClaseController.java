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

import com.cesde.eslclasshallrepositorybackend.dto.CursoClaseRequestDto;
import com.cesde.eslclasshallrepositorybackend.dto.CursoClaseResponseDto;
import com.cesde.eslclasshallrepositorybackend.dto.DtoMapper;
import com.cesde.eslclasshallrepositorybackend.service.CursoClaseService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cursos", description = "CRUD de cursos o clases de los docentes")
@RestController
@RequestMapping("/api/cursos")
public class CursoClaseController {

    private final CursoClaseService cursoService;

    public CursoClaseController(CursoClaseService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<CursoClaseResponseDto> obtenerTodos(@RequestParam(required = false) UUID docenteId) {
        return (docenteId == null ? cursoService.obtenerTodos() : cursoService.obtenerPorDocente(docenteId))
                .stream().map(DtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public CursoClaseResponseDto obtenerPorId(@PathVariable UUID id) {
        return DtoMapper.toDto(cursoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CursoClaseResponseDto> crear(@Valid @RequestBody CursoClaseRequestDto curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toDto(cursoService.crear(DtoMapper.toEntity(curso))));
    }

    @PutMapping("/{id}")
    public CursoClaseResponseDto actualizar(@PathVariable UUID id, @Valid @RequestBody CursoClaseRequestDto curso) {
        return DtoMapper.toDto(cursoService.actualizar(id, DtoMapper.toEntity(curso)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}