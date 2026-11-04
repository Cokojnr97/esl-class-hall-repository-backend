package com.cesde.eslclasshallrepositorybackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.cesde.eslclasshallrepositorybackend.model.entity.CourseClass;
import com.cesde.eslclasshallrepositorybackend.model.enums.CourseLevel;

public interface CourseClassRepository extends JpaRepository<CourseClass, UUID> {

    List<CourseClass> findByLevelOrderByUnitNumber(CourseLevel level);
}
