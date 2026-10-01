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

import com.cesde.eslclasshallrepositorybackend.model.entity.Usuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;
import com.cesde.eslclasshallrepositorybackend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuarios", description = "CRUD de usuarios, administradores y docentes")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable UUID id) {
        return usuarioService.obtenerPorId(id);
    }

    @GetMapping("/email/{email}")
    public Usuario obtenerPorEmail(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email);
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(usuario));
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable UUID id, @RequestBody Usuario usuario) {
        return usuarioService.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consultar administradores")
    @GetMapping("/rol/admin")
    public List<Usuario> obtenerAdministradores() {
        return usuarioService.obtenerTodos().stream().filter(usuario -> usuario.getRol() == RolUsuario.ADMIN).toList();
    }

    @Operation(summary = "Consultar docentes")
    @GetMapping("/rol/docente")
    public List<Usuario> obtenerDocentes() {
        return usuarioService.obtenerTodos().stream().filter(usuario -> usuario.getRol() == RolUsuario.DOCENTE).toList();
    }
}