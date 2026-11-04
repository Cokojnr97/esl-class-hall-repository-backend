package com.cesde.eslclasshallrepositorybackend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.ResourceNotFoundException;
import com.cesde.eslclasshallrepositorybackend.exception.BusinessRuleException;
import com.cesde.eslclasshallrepositorybackend.dto.ResourceRequest;
import com.cesde.eslclasshallrepositorybackend.dto.ResourceResponse;
import com.cesde.eslclasshallrepositorybackend.model.entity.CourseClass;
import com.cesde.eslclasshallrepositorybackend.model.entity.Resource;
import com.cesde.eslclasshallrepositorybackend.repository.CourseClassRepository;
import com.cesde.eslclasshallrepositorybackend.repository.ResourceRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UserRepository;
import com.cesde.eslclasshallrepositorybackend.model.enums.ResourceStatus;
import com.cesde.eslclasshallrepositorybackend.model.enums.CourseLevel;

@Service
public class ResourceService {

    private final ResourceRepository recursoRepository;
    private final UserRepository userRepository;
    private final CourseClassRepository cursoRepository;

    public ResourceService(ResourceRepository recursoRepository, UserRepository userRepository,
            CourseClassRepository cursoRepository) {
        this.recursoRepository = recursoRepository;
        this.userRepository = userRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional(readOnly = true)
    public List<Resource> getAll() {
        return recursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Resource> getPublished(CourseLevel level) {
        return level == null
                ? recursoRepository.findByStatus(ResourceStatus.PUBLISHED)
                : recursoRepository.findByStatusAndCourseUnitsLevel(ResourceStatus.PUBLISHED, level);
    }

    @Transactional(readOnly = true)
    public Resource getById(UUID id) {
        return recursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource no encontrado con id: " + id));
    }

    @Transactional
    public Resource create(Resource datos) {
        validate(datos);
        prepareRelationships(datos);
        return recursoRepository.save(datos);
    }

    @Transactional
    public Resource create(ResourceRequest request) {
        Resource resource = new Resource(
                request.title(),
                request.description(),
                request.url(),
                request.type(),
                userRepository.findById(request.createdById())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.createdById())));
        resource.setCourseUnits(getCourseUnitsByIds(request.courseUnitIds()));
        return recursoRepository.save(resource);
    }

    @Transactional
    public Resource update(UUID id, ResourceRequest request) {
        Resource resource = getById(id);
        resource.setTitle(request.title());
        resource.setDescription(request.description());
        resource.setUrl(request.url());
        resource.setType(request.type());
        resource.setCreatedBy(userRepository.findById(request.createdById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.createdById())));
        resource.setCourseUnits(getCourseUnitsByIds(request.courseUnitIds()));
        return recursoRepository.save(resource);
    }

    @Transactional(readOnly = true)
    public ResourceResponse toResponse(Resource resource) {
        return new ResourceResponse(
                resource.getId(),
                resource.getTitle(),
                resource.getDescription(),
                resource.getUrl(),
                resource.getType(),
                resource.getStatus(),
                resource.getCreatedBy().getId(),
                resource.getCourseUnits().stream().map(CourseClass::getId).collect(java.util.stream.Collectors.toSet()),
                resource.getCreatedAt(),
                resource.getUpdatedAt());
    }

    @Transactional
    public Resource update(UUID id, Resource datos) {
        validate(datos);
        Resource recurso = getById(id);
        recurso.setTitle(datos.getTitle());
        recurso.setDescription(datos.getDescription());
        recurso.setUrl(datos.getUrl());
        recurso.setType(datos.getType());
        recurso.setCreatedBy(userRepository.findById(datos.getCreatedBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con id: " + datos.getCreatedBy().getId())));
        recurso.setCourseUnits(getCourseUnits(datos.getCourseUnits()));
        return recursoRepository.save(recurso);
    }

    @Transactional
    public void delete(UUID id) {
        recursoRepository.delete(getById(id));
    }

    private void prepareRelationships(Resource recurso) {
        recurso.setCreatedBy(userRepository.findById(recurso.getCreatedBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con id: " + recurso.getCreatedBy().getId())));
        recurso.setCourseUnits(getCourseUnits(recurso.getCourseUnits()));
    }

    private Set<CourseClass> getCourseUnits(Set<CourseClass> datos) {
        if (datos == null || datos.isEmpty()) {
            return new HashSet<>();
        }
        Set<UUID> ids = datos.stream().map(CourseClass::getId).collect(java.util.stream.Collectors.toSet());
        List<CourseClass> courses = cursoRepository.findAllById(ids);
        if (courses.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more course units do not exist");
        }
        return new HashSet<>(courses);
    }

    private Set<CourseClass> getCourseUnitsByIds(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        List<CourseClass> courses = cursoRepository.findAllById(ids);
        if (courses.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more courses do not exist");
        }
        return new HashSet<>(courses);
    }

    private void validate(Resource datos) {
        if (datos == null || datos.getTitle() == null || datos.getType() == null
                || datos.getCreatedBy() == null || datos.getCreatedBy().getId() == null) {
            throw new BusinessRuleException("title, type y createdBy.id son obligatorios");
        }
        if (datos.getUrl() == null || datos.getUrl().isBlank()) {
            throw new BusinessRuleException("The resource URL or path is required");
        }
    }
}
