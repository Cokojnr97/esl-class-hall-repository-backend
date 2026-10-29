package com.cesde.eslclasshallrepositorybackend.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoRecurso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "recursos_migracion")
public class Recurso extends BaseEntity {

    public Recurso(String titulo, String descripcion, String url, TipoRecurso tipo, Usuario creadoPor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.tipo = tipo;
        this.creadoPor = creadoPor;
    }

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @Column(length = 500)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRecurso tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoRecurso estado = EstadoRecurso.BORRADOR;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creado_por_id", nullable = false)
    @JsonIgnoreProperties({ "perfil", "solicitudes", "recursosCreados" })
    private Usuario creadoPor;

    @ManyToMany
    @JoinTable(
            name = "curso_clase_recurso_migracion",
            joinColumns = @JoinColumn(name = "recurso_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_clase_id"))
    @JsonIgnore
    private Set<CursoClase> cursos = new HashSet<>();

    public void setEstado(EstadoRecurso estadoRecurso) {
        this.estado = estadoRecurso;
    }

    public void setCursos(Set<CursoClase> cursos) {
        if (this.cursos == null) {
            this.cursos = new HashSet<>();
        } else {
            this.cursos.clear();
        }
        if (cursos != null) {
            this.cursos.addAll(cursos);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrl() {
        return url;
    }

    public TipoRecurso getTipo() {
        return tipo;
    }

    public EstadoRecurso getEstado() {
        return estado;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public Set<CursoClase> getCursos() {
        return cursos;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTipo(TipoRecurso tipo) {
        this.tipo = tipo;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }


}
