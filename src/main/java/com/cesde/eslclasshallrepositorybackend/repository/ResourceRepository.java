package com.cesde.eslclasshallrepositorybackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import com.cesde.eslclasshallrepositorybackend.model.entity.Resource;
import com.cesde.eslclasshallrepositorybackend.model.enums.ResourceStatus;
import com.cesde.eslclasshallrepositorybackend.model.enums.CourseLevel;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    @Override
    @EntityGraph(attributePaths = "courseUnits")
    List<Resource> findAll();

    @EntityGraph(attributePaths = "courseUnits")
    List<Resource> findByStatus(ResourceStatus status);

    @EntityGraph(attributePaths = "courseUnits")
    List<Resource> findByStatusAndCourseUnitsLevel(ResourceStatus status, CourseLevel level);

    @EntityGraph(attributePaths = "courseUnits")
    List<Resource> findByCreatedById(UUID userId);

    Optional<Resource> findByTitle(String title);
}
