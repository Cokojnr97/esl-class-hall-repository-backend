package com.cesde.eslclasshallrepositorybackend.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoSolicitud;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoSolicitud;

class EntitySetterTest {

    @Test
    void recursoActualizaEstadoYCursosDeFormaMutable() {
        Recurso recurso = new Recurso("Recurso", null, "https://example.com", TipoRecurso.ENLACE, null);
        CursoClase curso = new CursoClase("Curso", null, 1, null);

        recurso.setEstado(EstadoRecurso.PUBLICADO);
        recurso.setCursos(Set.of(curso));

        assertEquals(EstadoRecurso.PUBLICADO, recurso.getEstado());
        assertEquals(Set.of(curso), recurso.getCursos());
        recurso.getCursos().clear();
        assertEquals(0, recurso.getCursos().size());
    }

    @Test
    void solicitudActualizaEstadoYAdministradorAprobador() {
        Admin admin = new Admin("Admin", "admin@example.com", "hash");
        SolicitudRecurso solicitud = new SolicitudRecurso(TipoSolicitud.CREAR, "{}", admin, null);

        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitud.setAprobadoPor(admin);

        assertEquals(EstadoSolicitud.APROBADA, solicitud.getEstado());
        assertEquals(admin, solicitud.getAprobadoPor());
    }
}
