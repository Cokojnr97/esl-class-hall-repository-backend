package com.cesde.eslclasshallrepositorybackend.dto;

import java.util.Set;

import com.cesde.eslclasshallrepositorybackend.model.enums.TipoRecurso;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecursoRequestDto(
        @NotBlank @Size(max = 200) String titulo,
        @Size(max = 2000) String descripcion,
        @NotBlank @Size(max = 500) String url,
        @NotNull TipoRecurso tipo,
        @NotNull @Valid ReferenciaDto creadoPor,
        Set<@NotNull @Valid ReferenciaDto> cursos) {
}
