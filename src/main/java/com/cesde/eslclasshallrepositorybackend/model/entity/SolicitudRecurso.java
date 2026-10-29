package com.cesde.eslclasshallrepositorybackend.model.entity;

import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoSolicitud;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoSolicitud;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
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
@Table(name = "solicitudes_recursos_migracion")
public class SolicitudRecurso extends BaseEntity {

    public SolicitudRecurso(TipoSolicitud tipoSolicitud, String payloadJson, Usuario solicitadoPor,
            Recurso recursoObjetivo) {
        this.tipoSolicitud = tipoSolicitud;
        this.payloadJson = payloadJson;
        this.solicitadoPor = solicitadoPor;
        this.recursoObjetivo = recursoObjetivo;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoSolicitud tipoSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(length = 4000)
    private String payloadJson;

    @ManyToOne(optional = false)
    @JoinColumn(name = "solicitado_por_id", nullable = false)
    @JsonIgnoreProperties({ "perfil", "solicitudes", "recursosCreados" })
    private Usuario solicitadoPor;

    @ManyToOne
    @JoinColumn(name = "aprobado_por_id")
    @JsonIgnoreProperties({ "perfil", "solicitudes", "recursosCreados" })
    private Usuario aprobadoPor;

    @ManyToOne
    @JoinColumn(name = "recurso_objetivo_id")
    @JsonIgnoreProperties({ "cursos" })
    private Recurso recursoObjetivo;

    public void setEstado(EstadoSolicitud estadoSolicitud) {
        this.estado = estadoSolicitud;
    }

    public void setAprobadoPor(Admin admin) {
        this.aprobadoPor = admin;
    }

    public TipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public Usuario getSolicitadoPor() {
        return solicitadoPor;
    }

    public Usuario getAprobadoPor() {
        return aprobadoPor;
    }

    public Recurso getRecursoObjetivo() {
        return recursoObjetivo;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    public void setSolicitadoPor(Usuario solicitadoPor) {
        this.solicitadoPor = solicitadoPor;
    }

    public void setAprobadoPor(Usuario aprobadoPor) {
        this.aprobadoPor = aprobadoPor;
    }

    public void setRecursoObjetivo(Recurso recursoObjetivo) {
        this.recursoObjetivo = recursoObjetivo;
    }


}
