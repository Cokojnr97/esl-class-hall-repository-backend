package com.cesde.eslclasshallrepositorybackend.model.entity;

import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "cursos_clase_recursos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"curso_clase_id", "recurso_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"cursoClase"})
@EqualsAndHashCode(callSuper = true)
public class CursoClaseRecurso extends BaseEntity {

    // Lado hijo del @OneToMany de CursoClase -> se detiene aquí para evitar el bucle JSON
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_clase_id", nullable = false)
    @JsonBackReference
    private CursoClase cursoClase;

    // Unidireccional: Recurso no necesita conocer en qué clases se usa
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurso_id", nullable = false)
    private Recurso recurso;
}
