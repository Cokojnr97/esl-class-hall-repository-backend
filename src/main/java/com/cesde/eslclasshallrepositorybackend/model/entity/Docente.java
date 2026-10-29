package com.cesde.eslclasshallrepositorybackend.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("DOCENTE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Docente extends Usuario {

    @OneToMany(mappedBy = "docente", orphanRemoval = false)
    @JsonIgnore
    private Set<CursoClase> cursos = new HashSet<>();

    public Docente(String nombre, String email, String passwordHash) {
        super(nombre, email, passwordHash, RolUsuario.DOCENTE);
    }

    public Set<CursoClase> getCursos() {
        return cursos;
    }

    public void setCursos(Set<CursoClase> cursos) {
        this.cursos = cursos;
    }

}
