package com.cesde.eslclasshallrepositorybackend.dto;

import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoUsuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDto(
        @Size(max = 120) String nombre,
        @Email @Size(max = 180) String email,
        String passwordHash,
        EstadoUsuario estado,
        RolUsuario rol,
        @Valid DireccionDto direccion) {
}
