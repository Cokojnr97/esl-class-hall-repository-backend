package com.cesde.eslclasshallrepositorybackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.eslclasshallrepositorybackend.model.entity.Docente;

public interface DocenteRepository extends JpaRepository<Docente, UUID> {
}