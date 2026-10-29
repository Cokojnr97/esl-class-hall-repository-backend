package com.cesde.eslclasshallrepositorybackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PerfilUsuarioCreateRequestDto(
        @Size(max = 1000) String biografia,
        @Size(max = 30) String telefono,
        @NotNull @Valid ReferenciaDto usuario) {
}
