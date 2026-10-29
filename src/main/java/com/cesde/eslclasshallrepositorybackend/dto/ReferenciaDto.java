package com.cesde.eslclasshallrepositorybackend.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ReferenciaDto(@NotNull UUID id, String nombre) {
}
