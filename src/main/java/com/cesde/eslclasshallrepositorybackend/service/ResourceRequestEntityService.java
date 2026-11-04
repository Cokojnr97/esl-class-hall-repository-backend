package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.ResourceNotFoundException;
import com.cesde.eslclasshallrepositorybackend.exception.BusinessRuleException;
import com.cesde.eslclasshallrepositorybackend.model.entity.Administrator;
import com.cesde.eslclasshallrepositorybackend.model.entity.Resource;
import com.cesde.eslclasshallrepositorybackend.model.entity.ResourceRequestEntity;
import com.cesde.eslclasshallrepositorybackend.model.entity.User;
import com.cesde.eslclasshallrepositorybackend.model.enums.ResourceStatus;
import com.cesde.eslclasshallrepositorybackend.model.enums.RequestStatus;
import com.cesde.eslclasshallrepositorybackend.model.enums.RequestType;
import com.cesde.eslclasshallrepositorybackend.repository.ResourceRepository;
import com.cesde.eslclasshallrepositorybackend.repository.ResourceRequestEntityRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UserRepository;

@Service
public class ResourceRequestEntityService {

    private final ResourceRequestEntityRepository solicitudRepository;
    private final UserRepository userRepository;
    private final ResourceRepository recursoRepository;

    public ResourceRequestEntityService(ResourceRequestEntityRepository solicitudRepository, UserRepository userRepository,
            ResourceRepository recursoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.userRepository = userRepository;
        this.recursoRepository = recursoRepository;
    }

    @Transactional(readOnly = true)
    public List<ResourceRequestEntity> getAll() {
        return solicitudRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ResourceRequestEntity getById(UUID id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request no encontrada con id: " + id));
    }

    @Transactional
    public ResourceRequestEntity create(ResourceRequestEntity datos) {
        validate(datos);
        prepareRelationships(datos);
        if (datos.getRequestType() == RequestType.ACTUALIZAR && datos.getTargetResource() == null) {
            throw new BusinessRuleException("Una solicitud de actualizaciÃ³n requiere un recurso objetivo");
        }
        return solicitudRepository.save(datos);
    }

    @Transactional
    public ResourceRequestEntity update(UUID id, ResourceRequestEntity datos) {
        validate(datos);
        ResourceRequestEntity solicitud = getById(id);
        validatePendiente(solicitud);
        prepareRelationships(datos);
        solicitud.setRequestType(datos.getRequestType());
        solicitud.setDetailsJson(datos.getDetailsJson());
        solicitud.setRequestedBy(datos.getRequestedBy());
        solicitud.setTargetResource(datos.getTargetResource());
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public ResourceRequestEntity aprobar(UUID id, UUID adminId) {
        ResourceRequestEntity solicitud = getById(id);
        Administrator admin = obtenerAdministrator(adminId);
        validatePendiente(solicitud);
        solicitud.setStatus(RequestStatus.APPROVED);
        solicitud.setApprovedBy(admin);
        publicarResource(solicitud);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public ResourceRequestEntity rechazar(UUID id, UUID adminId) {
        ResourceRequestEntity solicitud = getById(id);
        Administrator admin = obtenerAdministrator(adminId);
        validatePendiente(solicitud);
        solicitud.setStatus(RequestStatus.REJECTED);
        solicitud.setApprovedBy(admin);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public void delete(UUID id) {
        solicitudRepository.delete(getById(id));
    }

    private void prepareRelationships(ResourceRequestEntity solicitud) {
        solicitud.setRequestedBy(userRepository.findById(solicitud.getRequestedBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User solicitante no encontrado")));
        if (solicitud.getTargetResource() != null) {
            solicitud.setTargetResource(recursoRepository.findById(solicitud.getTargetResource().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Resource objetivo no encontrado")));
        }
    }

    private Administrator obtenerAdministrator(UUID adminId) {
        if (adminId == null) {
            throw new BusinessRuleException("adminId es obligatorio");
        }
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Administratoristrador no encontrado"));
        if (!(user instanceof Administrator admin)) {
            throw new BusinessRuleException("Solo un administrador puede decidir una solicitud");
        }
        return admin;
    }

    private void publicarResource(ResourceRequestEntity solicitud) {
        Resource recurso = solicitud.getTargetResource();
        if (recurso != null) {
            recurso.setStatus(ResourceStatus.PUBLISHED);
            recursoRepository.save(recurso);
        }
    }

    private void validatePendiente(ResourceRequestEntity solicitud) {
        if (solicitud.getStatus() != RequestStatus.PENDING) {
            throw new BusinessRuleException("La solicitud ya fue procesada");
        }
    }

    private void validate(ResourceRequestEntity datos) {
        if (datos == null || datos.getRequestType() == null || datos.getRequestedBy() == null
                || datos.getRequestedBy().getId() == null) {
            throw new BusinessRuleException("requestType y requestedBy.id son obligatorios");
        }
    }
}
