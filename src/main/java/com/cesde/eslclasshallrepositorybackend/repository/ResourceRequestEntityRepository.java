package com.cesde.eslclasshallrepositorybackend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.eslclasshallrepositorybackend.model.entity.ResourceRequestEntity;
import com.cesde.eslclasshallrepositorybackend.model.enums.RequestStatus;

public interface ResourceRequestEntityRepository extends JpaRepository<ResourceRequestEntity, UUID> {

    List<ResourceRequestEntity> findByStatus(RequestStatus status);

    List<ResourceRequestEntity> findByRequestedById(UUID userId);
}
