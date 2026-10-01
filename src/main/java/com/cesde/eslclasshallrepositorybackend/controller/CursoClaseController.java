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

import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;
import com.cesde.eslclasshallrepositorybackend.service.CursoClaseService;

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
    public List<CursoClase> obtenerTodos(@RequestParam(required = false) UUID docenteId) {
        return docenteId == null ? cursoService.obtenerTodos() : cursoService.obtenerPorDocente(docenteId);
    }

    @GetMapping("/{id}")
    public CursoClase obtenerPorId(@PathVariable UUID id) {
        return cursoService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<CursoClase> crear(@RequestBody CursoClase curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.crear(curso));
    }

    @PutMapping("/{id}")
    public CursoClase actualizar(@PathVariable UUID id, @RequestBody CursoClase curso) {
        return cursoService.actualizar(id, curso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}