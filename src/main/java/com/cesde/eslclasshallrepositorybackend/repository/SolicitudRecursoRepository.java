package com.cesde.eslclasshallrepositorybackend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.eslclasshallrepositorybackend.model.entity.SolicitudRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoSolicitud;

public interface SolicitudRecursoRepository extends JpaRepository<SolicitudRecurso, UUID> {

    List<SolicitudRecurso> findByEstado(EstadoSolicitud estado);

    List<SolicitudRecurso> findBySolicitadoPorId(UUID usuarioId);
}