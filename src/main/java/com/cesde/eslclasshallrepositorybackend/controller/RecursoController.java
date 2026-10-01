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

import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.service.RecursoService;

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
    public List<Recurso> obtenerTodos() {
        return recursoService.obtenerTodos();
    }

    @Operation(summary = "Consultar recursos aprobados")
    @GetMapping("/publicados")
    public List<Recurso> obtenerPublicados() {
        return recursoService.obtenerPublicados();
    }

    @GetMapping("/{id}")
    public Recurso obtenerPorId(@PathVariable UUID id) {
        return recursoService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Recurso> crear(@RequestBody Recurso recurso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recursoService.crear(recurso));
    }

    @PutMapping("/{id}")
    public Recurso actualizar(@PathVariable UUID id, @RequestBody Recurso recurso) {
        return recursoService.actualizar(id, recurso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        recursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}