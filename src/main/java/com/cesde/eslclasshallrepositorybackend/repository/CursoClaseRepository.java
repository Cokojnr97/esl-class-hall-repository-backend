package com.cesde.eslclasshallrepositorybackend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;

public interface CursoClaseRepository extends JpaRepository<CursoClase, UUID> {

    List<CursoClase> findByDocenteId(UUID docenteId);
}