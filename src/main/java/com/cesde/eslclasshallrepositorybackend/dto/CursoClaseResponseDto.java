package com.cesde.eslclasshallrepositorybackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CursoClaseResponseDto(
        UUID id,
        String nombre,
        String descripcion,
        Integer orden,
        ReferenciaDto docente,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
