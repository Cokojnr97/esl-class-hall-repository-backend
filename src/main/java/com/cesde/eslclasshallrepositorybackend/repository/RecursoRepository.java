package com.cesde.eslclasshallrepositorybackend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;

public interface RecursoRepository extends JpaRepository<Recurso, UUID> {

    List<Recurso> findByEstado(EstadoRecurso estado);

    List<Recurso> findByCreadoPorId(UUID usuarioId);
}