package com.cesde.eslclasshallrepositorybackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;
import com.cesde.eslclasshallrepositorybackend.model.entity.Docente;
import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoRecurso;
import com.cesde.eslclasshallrepositorybackend.repository.CursoClaseRepository;
import com.cesde.eslclasshallrepositorybackend.repository.RecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class RecursoServiceTest {

    @Mock
    private RecursoRepository recursoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CursoClaseRepository cursoRepository;

    @InjectMocks
    private RecursoService recursoService;

    @Test
    void crearRechazaReferenciaDeCursoSinIdAntesDeConsultarRepositorios() {
        Docente creador = new Docente("Ana", "ana@example.com", "hash");
        UUID creadorId = UUID.randomUUID();
        creador.setId(creadorId);
        Recurso recurso = new Recurso("Material", null, "https://example.com", TipoRecurso.ENLACE, creador);
        recurso.setCursos(Set.of(new CursoClase("Curso", null, 1, null)));
        ReglaDeNegocioException exception = assertThrows(
                ReglaDeNegocioException.class, () -> recursoService.crear(recurso));

        assertEquals("Cada curso asociado debe incluir un id", exception.getMessage());
        verify(usuarioRepository, never()).findById(creadorId);
        verify(cursoRepository, never()).findAllById(org.mockito.ArgumentMatchers.anySet());
        verify(recursoRepository, never()).save(recurso);
    }

    @Test
    void crearRechazaTituloEnBlanco() {
        Docente creador = new Docente("Ana", "ana@example.com", "hash");
        creador.setId(UUID.randomUUID());
        Recurso recurso = new Recurso("   ", null, "https://example.com", TipoRecurso.ENLACE, creador);

        ReglaDeNegocioException exception = assertThrows(
                ReglaDeNegocioException.class, () -> recursoService.crear(recurso));

        assertEquals("titulo, tipo y creadoPor.id son obligatorios", exception.getMessage());
        verify(usuarioRepository, never()).findById(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void crearSiempreDejaElRecursoEnBorrador() {
        Docente creador = new Docente("Ana", "ana@example.com", "hash");
        UUID creadorId = UUID.randomUUID();
        creador.setId(creadorId);
        Recurso recurso = new Recurso("Material", null, "https://example.com", TipoRecurso.ENLACE, creador);
        recurso.setEstado(EstadoRecurso.PUBLICADO);
        when(usuarioRepository.findById(creadorId)).thenReturn(Optional.of(creador));
        when(recursoRepository.save(recurso)).thenReturn(recurso);

        Recurso resultado = recursoService.crear(recurso);

        assertSame(recurso, resultado);
        assertEquals(EstadoRecurso.BORRADOR, resultado.getEstado());
        verify(recursoRepository).save(recurso);
    }

    @Test
    void actualizarNoModificaElRecursoSiUnCursoNoExiste() {
        UUID recursoId = UUID.randomUUID();
        UUID creadorId = UUID.randomUUID();
        UUID cursoId = UUID.randomUUID();
        Docente creador = new Docente("Ana", "ana@example.com", "hash");
        creador.setId(creadorId);
        Recurso existente = new Recurso("Título anterior", null, "https://example.com/old",
                TipoRecurso.ENLACE, creador);
        existente.setId(recursoId);
        Recurso datos = new Recurso("Título nuevo", null, "https://example.com/new",
                TipoRecurso.ENLACE, creador);
        CursoClase cursoReferencia = new CursoClase("Curso", null, 1, null);
        cursoReferencia.setId(cursoId);
        datos.setCursos(Set.of(cursoReferencia));
        when(recursoRepository.findById(recursoId)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(creadorId)).thenReturn(Optional.of(creador));
        when(cursoRepository.findAllById(Set.of(cursoId))).thenReturn(List.of());

        assertThrows(
                com.cesde.eslclasshallrepositorybackend.exception.RecursoNoEncontradoException.class,
                () -> recursoService.actualizar(recursoId, datos));

        assertEquals("Título anterior", existente.getTitulo());
        assertEquals("https://example.com/old", existente.getUrl());
        verify(recursoRepository, never()).save(existente);
    }
}
