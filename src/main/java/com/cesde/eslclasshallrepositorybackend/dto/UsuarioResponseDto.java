package com.cesde.eslclasshallrepositorybackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoUsuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;

public record UsuarioResponseDto(
        UUID id,
        String nombre,
        String email,
        EstadoUsuario estado,
        RolUsuario rol,
        DireccionDto direccion,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
