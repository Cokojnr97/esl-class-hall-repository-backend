package com.cesde.eslclasshallrepositorybackend.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoRecurso;

public record RecursoResponseDto(
        UUID id,
        String titulo,
        String descripcion,
        String url,
        TipoRecurso tipo,
        EstadoRecurso estado,
        ReferenciaDto creadoPor,
        Set<ReferenciaDto> cursos,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
