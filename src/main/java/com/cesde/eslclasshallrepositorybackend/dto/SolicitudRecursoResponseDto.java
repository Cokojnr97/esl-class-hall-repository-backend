package com.cesde.eslclasshallrepositorybackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoSolicitud;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoSolicitud;

public record SolicitudRecursoResponseDto(
        UUID id,
        TipoSolicitud tipoSolicitud,
        EstadoSolicitud estado,
        String payloadJson,
        ReferenciaDto solicitadoPor,
        ReferenciaDto aprobadoPor,
        ReferenciaDto recursoObjetivo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
