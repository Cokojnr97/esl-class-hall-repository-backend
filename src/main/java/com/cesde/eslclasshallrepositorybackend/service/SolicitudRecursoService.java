package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import com.cesde.eslclasshallrepositorybackend.dto.DtoMapper;
import com.cesde.eslclasshallrepositorybackend.dto.SolicitudRecursoPayloadDto;
import com.cesde.eslclasshallrepositorybackend.exception.RecursoNoEncontradoException;
import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.Admin;
import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.model.entity.SolicitudRecurso;
import com.cesde.eslclasshallrepositorybackend.model.entity.Usuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoSolicitud;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoSolicitud;
import com.cesde.eslclasshallrepositorybackend.repository.RecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.SolicitudRecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UsuarioRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class SolicitudRecursoService {

    private final SolicitudRecursoRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecursoRepository recursoRepository;
    private final RecursoService recursoService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public SolicitudRecursoService(SolicitudRecursoRepository solicitudRepository, UsuarioRepository usuarioRepository,
            RecursoRepository recursoRepository, RecursoService recursoService, ObjectMapper objectMapper,
            Validator validator) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.recursoRepository = recursoRepository;
        this.recursoService = recursoService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public List<SolicitudRecurso> obtenerTodos() {
        return solicitudRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SolicitudRecurso obtenerPorId(UUID id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada con id: " + id));
    }

    @Transactional
    public SolicitudRecurso crear(SolicitudRecurso datos) {
        validar(datos);
        validarRecursoObjetivo(datos);
        prepararRelaciones(datos);
        return solicitudRepository.save(datos);
    }

    @Transactional
    public SolicitudRecurso actualizar(UUID id, SolicitudRecurso datos) {
        validar(datos);
        validarRecursoObjetivo(datos);
        SolicitudRecurso solicitud = obtenerPorId(id);
        validarPendiente(solicitud);
        prepararRelaciones(datos);
        solicitud.setTipoSolicitud(datos.getTipoSolicitud());
        solicitud.setPayloadJson(datos.getPayloadJson());
        solicitud.setSolicitadoPor(datos.getSolicitadoPor());
        solicitud.setRecursoObjetivo(datos.getRecursoObjetivo());
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public SolicitudRecurso aprobar(UUID id, UUID adminId) {
        SolicitudRecurso solicitud = obtenerPorId(id);
        Admin admin = obtenerAdmin(adminId);
        validarPendiente(solicitud);
        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitud.setAprobadoPor(admin);
        procesarRecursoAprobado(solicitud);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public SolicitudRecurso rechazar(UUID id, UUID adminId) {
        SolicitudRecurso solicitud = obtenerPorId(id);
        Admin admin = obtenerAdmin(adminId);
        validarPendiente(solicitud);
        solicitud.setEstado(EstadoSolicitud.RECHAZADA);
        solicitud.setAprobadoPor(admin);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public void eliminar(UUID id) {
        solicitudRepository.delete(obtenerPorId(id));
    }

    private void prepararRelaciones(SolicitudRecurso solicitud) {
        solicitud.setSolicitadoPor(usuarioRepository.findById(solicitud.getSolicitadoPor().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario solicitante no encontrado")));
        if (solicitud.getRecursoObjetivo() != null) {
            solicitud.setRecursoObjetivo(recursoRepository.findById(solicitud.getRecursoObjetivo().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Recurso objetivo no encontrado")));
        }
    }

    private Admin obtenerAdmin(UUID adminId) {
        if (adminId == null) {
            throw new ReglaDeNegocioException("adminId es obligatorio");
        }
        Usuario usuario = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Administrador no encontrado"));
        if (!(usuario instanceof Admin admin)) {
            throw new ReglaDeNegocioException("Solo un administrador puede decidir una solicitud");
        }
        return admin;
    }

    private void procesarRecursoAprobado(SolicitudRecurso solicitud) {
        Recurso recurso = solicitud.getRecursoObjetivo();
        if (solicitud.getTipoSolicitud() == TipoSolicitud.CREAR && recurso == null) {
            SolicitudRecursoPayloadDto payload = leerPayload(solicitud.getPayloadJson());
            validarPayload(payload);
            Recurso nuevo = recursoService.crear(DtoMapper.toEntity(payload, solicitud.getSolicitadoPor()));
            nuevo.setEstado(EstadoRecurso.PUBLICADO);
            recursoRepository.save(nuevo);
            return;
        }

        if (recurso == null) {
            throw new ReglaDeNegocioException("La solicitud requiere un recurso objetivo");
        }

        Recurso recursoActualizado = recurso;
        if (solicitud.getTipoSolicitud() == TipoSolicitud.ACTUALIZAR) {
            SolicitudRecursoPayloadDto payload = leerPayload(solicitud.getPayloadJson());
            validarPayload(payload);
            recursoActualizado = recursoService.actualizar(
                    recurso.getId(), DtoMapper.toEntity(payload, recurso.getCreadoPor()));
        }

        recursoActualizado.setEstado(EstadoRecurso.PUBLICADO);
        recursoRepository.save(recursoActualizado);
    }

    private SolicitudRecursoPayloadDto leerPayload(String payloadJson) {
        if (payloadJson == null || payloadJson.isBlank()) {
            throw new ReglaDeNegocioException("El payloadJson con los datos del recurso es obligatorio");
        }
        try {
            SolicitudRecursoPayloadDto payload = objectMapper.readValue(payloadJson, SolicitudRecursoPayloadDto.class);
            if (payload == null) {
                throw new ReglaDeNegocioException("El payloadJson debe contener un objeto de recurso");
            }
            return payload;
        } catch (JacksonException ex) {
            throw new ReglaDeNegocioException("El payloadJson no contiene un recurso válido", ex);
        }
    }

    private void validarPayload(SolicitudRecursoPayloadDto payload) {
        Set<ConstraintViolation<SolicitudRecursoPayloadDto>> violations = validator.validate(payload);
        if (!violations.isEmpty()) {
            String mensaje = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .sorted()
                    .collect(java.util.stream.Collectors.joining("; "));
            throw new ReglaDeNegocioException("El payloadJson del recurso no es válido: " + mensaje);
        }
    }

    private void validarPendiente(SolicitudRecurso solicitud) {
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new ReglaDeNegocioException("La solicitud ya fue procesada");
        }
    }

    private void validarRecursoObjetivo(SolicitudRecurso solicitud) {
        Recurso recursoObjetivo = solicitud.getRecursoObjetivo();
        if (solicitud.getTipoSolicitud() == TipoSolicitud.ACTUALIZAR && recursoObjetivo == null) {
            throw new ReglaDeNegocioException("Una solicitud de actualización requiere un recurso objetivo");
        }
        if (solicitud.getTipoSolicitud() == TipoSolicitud.CREAR && recursoObjetivo != null) {
            throw new ReglaDeNegocioException("Una solicitud de creación no debe incluir un recurso objetivo");
        }
        if (recursoObjetivo != null && recursoObjetivo.getId() == null) {
            throw new ReglaDeNegocioException("El recurso objetivo debe incluir un id");
        }
    }

    private void validar(SolicitudRecurso datos) {
        if (datos == null || datos.getTipoSolicitud() == null || datos.getSolicitadoPor() == null
                || datos.getSolicitadoPor().getId() == null) {
            throw new ReglaDeNegocioException("tipoSolicitud y solicitadoPor.id son obligatorios");
        }
        if (datos.getPayloadJson() != null && datos.getPayloadJson().length() > 4000) {
            throw new ReglaDeNegocioException("payloadJson supera la longitud permitida");
        }
    }
}