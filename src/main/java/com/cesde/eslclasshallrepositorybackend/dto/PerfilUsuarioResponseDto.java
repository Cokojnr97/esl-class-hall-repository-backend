package com.cesde.eslclasshallrepositorybackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PerfilUsuarioResponseDto(
        UUID id,
        String biografia,
        String telefono,
        ReferenciaDto usuario,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
