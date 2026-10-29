package com.cesde.eslclasshallrepositorybackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CursoClaseRequestDto(
        @NotBlank @Size(max = 180) String nombre,
        @Size(max = 1000) String descripcion,
        @NotNull @Positive Integer orden,
        @NotNull @Valid ReferenciaDto docente) {
}