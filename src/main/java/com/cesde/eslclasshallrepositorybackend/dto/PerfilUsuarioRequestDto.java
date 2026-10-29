package com.cesde.eslclasshallrepositorybackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

public record PerfilUsuarioRequestDto(
        @Size(max = 1000) String biografia,
        @Size(max = 30) String telefono,
        @Valid ReferenciaDto usuario) {
}
