package com.cesde.eslclasshallrepositorybackend.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "curso_clases_migracion")
public class CursoClase extends BaseEntity {

    public CursoClase(String nombre, String descripcion, Integer orden, Docente docente) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
        this.docente = docente;
    }

    

    @Column(nullable = false, length = 180)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Integer orden;

    @ManyToOne(optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToMany(mappedBy = "cursos")
    @OrderBy("titulo ASC")
    @JsonIgnore
    private Set<Recurso> recursos = new HashSet<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Set<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(Set<Recurso> recursos) {
        this.recursos = recursos;
    }
}
