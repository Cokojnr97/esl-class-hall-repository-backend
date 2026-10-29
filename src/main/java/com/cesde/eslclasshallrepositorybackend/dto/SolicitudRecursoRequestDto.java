package com.cesde.eslclasshallrepositorybackend.dto;

import com.cesde.eslclasshallrepositorybackend.model.enums.TipoSolicitud;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SolicitudRecursoRequestDto(
        @NotNull TipoSolicitud tipoSolicitud,
        @Size(max = 4000) String payloadJson,
        @NotNull @Valid ReferenciaDto solicitadoPor,
        @Valid ReferenciaDto recursoObjetivo) {
}
