package com.cesde.eslclasshallrepositorybackend.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;
import com.cesde.eslclasshallrepositorybackend.model.embeddable.Direccion;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoUsuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "usuarios_migracion")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario extends BaseEntity {

    protected Usuario(String nombre, String email, String passwordHash, RolUsuario rol) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.estado = EstadoUsuario.ACTIVE;
        this.rol = rol;
    }

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    @JsonProperty
    @Column(nullable = false, length = 20, updatable = false)
    private RolUsuario rol;

    @Embedded
    private Direccion direccion;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private PerfilUsuario perfil;

    @OneToMany(mappedBy = "solicitadoPor", orphanRemoval = false)
    @JsonIgnore
    private Set<SolicitudRecurso> solicitudes = new HashSet<>();

    @OneToMany(mappedBy = "creadoPor", orphanRemoval = false)
    @JsonIgnore
    private Set<Recurso> recursosCreados = new HashSet<>();

    @PrePersist
    @PreUpdate
    protected void ensureRoleMatchesSubtype() {
        if (this instanceof Admin) {
            this.rol = RolUsuario.ADMIN;
            return;
        }

        if (this instanceof Docente) {
            this.rol = RolUsuario.DOCENTE;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public Set<SolicitudRecurso> getSolicitudes() {
        return solicitudes;
    }

    public Set<Recurso> getRecursosCreados() {
        return recursosCreados;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public void setSolicitudes(Set<SolicitudRecurso> solicitudes) {
        this.solicitudes = solicitudes;
    }

    public void setRecursosCreados(Set<Recurso> recursosCreados) {
        this.recursosCreados = recursosCreados;
    }


}
