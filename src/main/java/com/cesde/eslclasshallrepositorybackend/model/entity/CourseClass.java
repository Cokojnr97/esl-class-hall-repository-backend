package com.cesde.eslclasshallrepositorybackend.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;
import com.cesde.eslclasshallrepositorybackend.model.enums.CourseLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "course_classes")
public class CourseClass extends BaseEntity {

    public CourseClass(CourseLevel level, Integer unitNumber, String name, String description) {
        this.level = level;
        this.unitNumber = unitNumber;
        this.name = name;
        this.description = description;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private CourseLevel level;

    @Column(nullable = false)
    private Integer unitNumber;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToMany(mappedBy = "courseUnits")
    @OrderBy("title ASC")
    @JsonIgnore
    private Set<Resource> resources = new HashSet<>();
}
