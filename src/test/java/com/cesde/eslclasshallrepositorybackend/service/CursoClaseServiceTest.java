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
import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;
import com.cesde.eslclasshallrepositorybackend.model.entity.Docente;
import com.cesde.eslclasshallrepositorybackend.repository.CursoClaseRepository;
import com.cesde.eslclasshallrepositorybackend.repository.DocenteRepository;

@ExtendWith(MockitoExtension.class)
class CursoClaseServiceTest {

    @Mock
    private CursoClaseRepository cursoRepository;

    @Mock
    private DocenteRepository docenteRepository;

    @InjectMocks
    private CursoClaseService cursoService;

    @Test
    void crearRechazaOrdenNoPositivoAntesDeConsultarRepositorios() {
        Docente docente = new Docente(null, null, null);
        docente.setId(UUID.randomUUID());
        CursoClase curso = new CursoClase("Curso", null, 0, docente);

        ReglaDeNegocioException exception = assertThrows(
                ReglaDeNegocioException.class, () -> cursoService.crear(curso));

        assertEquals("El orden del curso debe ser mayor que cero", exception.getMessage());
        verifyNoInteractions(cursoRepository, docenteRepository);
    }
}
