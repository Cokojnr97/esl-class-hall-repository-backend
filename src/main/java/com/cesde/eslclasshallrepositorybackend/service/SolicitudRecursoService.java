package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class SolicitudRecursoService {

    private final SolicitudRecursoRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecursoRepository recursoRepository;

    public SolicitudRecursoService(SolicitudRecursoRepository solicitudRepository, UsuarioRepository usuarioRepository,
            RecursoRepository recursoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.recursoRepository = recursoRepository;
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
        prepararRelaciones(datos);
        if (datos.getTipoSolicitud() == TipoSolicitud.ACTUALIZAR && datos.getRecursoObjetivo() == null) {
            throw new ReglaDeNegocioException("Una solicitud de actualización requiere un recurso objetivo");
        }
        return solicitudRepository.save(datos);
    }

    @Transactional
    public SolicitudRecurso actualizar(UUID id, SolicitudRecurso datos) {
        validar(datos);
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
        publicarRecurso(solicitud);
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

    private void publicarRecurso(SolicitudRecurso solicitud) {
        Recurso recurso = solicitud.getRecursoObjetivo();
        if (recurso != null) {
            recurso.setEstado(EstadoRecurso.PUBLICADO);
            recursoRepository.save(recurso);
        }
    }

    private void validarPendiente(SolicitudRecurso solicitud) {
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new ReglaDeNegocioException("La solicitud ya fue procesada");
        }
    }

    private void validar(SolicitudRecurso datos) {
        if (datos == null || datos.getTipoSolicitud() == null || datos.getSolicitadoPor() == null
                || datos.getSolicitadoPor().getId() == null) {
            throw new ReglaDeNegocioException("tipoSolicitud y solicitadoPor.id son obligatorios");
        }
    }
}