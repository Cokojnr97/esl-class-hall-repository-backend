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

import com.cesde.eslclasshallrepositorybackend.model.entity.PerfilUsuario;
import com.cesde.eslclasshallrepositorybackend.service.PerfilUsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Perfiles", description = "CRUD de perfiles de usuario")
@RestController
@RequestMapping("/api/perfiles")
public class PerfilUsuarioController {

    private final PerfilUsuarioService perfilService;

    public PerfilUsuarioController(PerfilUsuarioService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public List<PerfilUsuario> obtenerTodos() {
        return perfilService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public PerfilUsuario obtenerPorId(@PathVariable UUID id) {
        return perfilService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<PerfilUsuario> crear(@RequestBody PerfilUsuario perfil) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilService.crear(perfil));
    }

    @PutMapping("/{id}")
    public PerfilUsuario actualizar(@PathVariable UUID id, @RequestBody PerfilUsuario perfil) {
        return perfilService.actualizar(id, perfil);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        perfilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}