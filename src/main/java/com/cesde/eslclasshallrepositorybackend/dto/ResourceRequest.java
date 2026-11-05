package com.cesde.eslclasshallrepositorybackend.dto;

import java.util.Set;
import java.util.UUID;

import com.cesde.eslclasshallrepositorybackend.model.enums.ResourceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceRequest(
        @NotBlank String title,
        String description,
        @NotBlank String url,
        @NotNull ResourceType type,
        @NotNull UUID createdById,
        Set<UUID> courseUnitIds) {
}
