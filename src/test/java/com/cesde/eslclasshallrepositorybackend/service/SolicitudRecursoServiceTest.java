package com.cesde.eslclasshallrepositorybackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.Admin;
import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.model.entity.SolicitudRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoSolicitud;
import com.cesde.eslclasshallrepositorybackend.repository.RecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.SolicitudRecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UsuarioRepository;

import jakarta.validation.Validator;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class SolicitudRecursoServiceTest {

    @Mock
    private SolicitudRecursoRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RecursoRepository recursoRepository;

    @Mock
    private RecursoService recursoService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private SolicitudRecursoService solicitudService;

    @Test
    void crearRechazaRecursoObjetivoEnSolicitudDeCreacion() {
        Admin solicitante = new Admin(null, null, null);
        solicitante.setId(UUID.randomUUID());
        Recurso recursoObjetivo = new Recurso(null, null, null, null, null);
        recursoObjetivo.setId(UUID.randomUUID());
        SolicitudRecurso solicitud = new SolicitudRecurso(
                TipoSolicitud.CREAR, null, solicitante, recursoObjetivo);

        ReglaDeNegocioException exception = assertThrows(
                ReglaDeNegocioException.class, () -> solicitudService.crear(solicitud));

        assertEquals("Una solicitud de creación no debe incluir un recurso objetivo", exception.getMessage());
        verifyNoInteractions(solicitudRepository, usuarioRepository, recursoRepository);
    }

    @Test
    void crearRechazaRecursoObjetivoSinIdAntesDeConsultarRepositorios() {
        Admin solicitante = new Admin(null, null, null);
        solicitante.setId(UUID.randomUUID());
        Recurso recursoObjetivo = new Recurso(null, null, null, null, null);
        SolicitudRecurso solicitud = new SolicitudRecurso(
                TipoSolicitud.ACTUALIZAR, null, solicitante, recursoObjetivo);

        ReglaDeNegocioException exception = assertThrows(
                ReglaDeNegocioException.class, () -> solicitudService.crear(solicitud));

        assertEquals("El recurso objetivo debe incluir un id", exception.getMessage());
        verifyNoInteractions(solicitudRepository, usuarioRepository, recursoRepository);
    }
}
