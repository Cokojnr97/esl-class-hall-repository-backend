package com.cesde.eslclasshallrepositorybackend.model.entity;

import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "perfiles_usuario_migracion")
public class PerfilUsuario extends BaseEntity {

    public PerfilUsuario(String biografia, String telefono, Usuario usuario) {
        this.biografia = biografia;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    @Column(length = 1000)
    private String biografia;

    @Column(length = 30)
    private String telefono;

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnoreProperties({ "perfil", "solicitudes", "recursosCreados" })
    private Usuario usuario;

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
}