package com.cesde.eslclasshallrepositorybackend.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.cesde.eslclasshallrepositorybackend.model.enums.ResourceStatus;
import com.cesde.eslclasshallrepositorybackend.model.enums.ResourceType;

public record ResourceResponse(
        UUID id,
        String title,
        String description,
        String url,
        ResourceType type,
        ResourceStatus status,
        UUID createdById,
        Set<UUID> courseUnitIds,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
