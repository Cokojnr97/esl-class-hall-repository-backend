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
import com.cesde.eslclasshallrepositorybackend.dto.PerfilUsuarioCreateRequestDto;
import com.cesde.eslclasshallrepositorybackend.dto.PerfilUsuarioRequestDto;
import com.cesde.eslclasshallrepositorybackend.dto.PerfilUsuarioResponseDto;
import com.cesde.eslclasshallrepositorybackend.service.PerfilUsuarioService;

import jakarta.validation.Valid;
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
    public List<PerfilUsuarioResponseDto> obtenerTodos() {
        return perfilService.obtenerTodos().stream().map(DtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public PerfilUsuarioResponseDto obtenerPorId(@PathVariable UUID id) {
        return DtoMapper.toDto(perfilService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PerfilUsuarioResponseDto> crear(@Valid @RequestBody PerfilUsuarioCreateRequestDto perfil) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toDto(perfilService.crear(DtoMapper.toEntity(perfil))));
    }

    @PutMapping("/{id}")
    public PerfilUsuarioResponseDto actualizar(@PathVariable UUID id, @Valid @RequestBody PerfilUsuarioRequestDto perfil) {
        return DtoMapper.toDto(perfilService.actualizar(id, DtoMapper.toEntity(perfil)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        perfilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}