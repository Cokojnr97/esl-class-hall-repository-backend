package com.cesde.eslclasshallrepositorybackend.dto;

import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoUsuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCreateRequestDto(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Email @Size(max = 180) String email,
        @NotBlank String passwordHash,
        EstadoUsuario estado,
        RolUsuario rol,
        @Valid DireccionDto direccion) {
}
