package com.cesde.eslclasshallrepositorybackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.Docente;
import com.cesde.eslclasshallrepositorybackend.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void crearRechazaNombreEnBlancoAntesDeConsultarRepositorios() {
        Docente datos = new Docente("   ", "ana@example.com", "hash");

        ReglaDeNegocioException exception = assertThrows(
                ReglaDeNegocioException.class, () -> usuarioService.crear(datos));

        assertEquals("nombre, passwordHash, email y rol son obligatorios", exception.getMessage());
        verifyNoInteractions(usuarioRepository);
    }
}
