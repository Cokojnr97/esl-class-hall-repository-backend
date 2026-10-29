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
import com.cesde.eslclasshallrepositorybackend.dto.UsuarioCreateRequestDto;
import com.cesde.eslclasshallrepositorybackend.dto.UsuarioRequestDto;
import com.cesde.eslclasshallrepositorybackend.dto.UsuarioResponseDto;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;
import com.cesde.eslclasshallrepositorybackend.service.UsuarioService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Administradores", description = "CRUD de usuarios administradores")
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDto> obtenerTodos() {
        return usuarioService.obtenerPorRol(RolUsuario.ADMIN).stream().map(DtoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto obtenerPorId(@PathVariable UUID id) {
        return DtoMapper.toDto(usuarioService.obtenerAdmin(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crear(@Valid @RequestBody UsuarioCreateRequestDto usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toDto(usuarioService.crear(DtoMapper.toEntity(usuario, RolUsuario.ADMIN), RolUsuario.ADMIN)));
    }

    @PutMapping("/{id}")
    public UsuarioResponseDto actualizar(@PathVariable UUID id, @Valid @RequestBody UsuarioRequestDto usuario) {
        return DtoMapper.toDto(usuarioService.actualizar(id, DtoMapper.toUpdateEntity(usuario), RolUsuario.ADMIN));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        usuarioService.eliminar(id, RolUsuario.ADMIN);
        return ResponseEntity.noContent().build();
    }
}