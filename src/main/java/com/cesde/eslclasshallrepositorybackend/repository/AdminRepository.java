package com.cesde.eslclasshallrepositorybackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.eslclasshallrepositorybackend.model.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
}