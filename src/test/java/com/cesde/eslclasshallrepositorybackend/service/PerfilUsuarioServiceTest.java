package com.cesde.eslclasshallrepositorybackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.Admin;
import com.cesde.eslclasshallrepositorybackend.model.entity.PerfilUsuario;
import com.cesde.eslclasshallrepositorybackend.repository.PerfilUsuarioRepository;

@ExtendWith(MockitoExtension.class)
class PerfilUsuarioServiceTest {

    @Mock
    private PerfilUsuarioRepository perfilRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PerfilUsuarioService perfilService;

    @Test
    void crearRechazaUsuarioQueYaTienePerfil() {
        UUID usuarioId = UUID.randomUUID();
        Admin usuarioReferencia = new Admin(null, null, null);
        usuarioReferencia.setId(usuarioId);
        PerfilUsuario datos = new PerfilUsuario("Bio", "123", null);
        datos.setUsuario(usuarioReferencia);
        when(perfilRepository.existsByUsuario_Id(usuarioId)).thenReturn(true);

        ReglaDeNegocioException exception = assertThrows(
                ReglaDeNegocioException.class, () -> perfilService.crear(datos));

        assertEquals("El usuario ya tiene un perfil asociado", exception.getMessage());
        verify(usuarioService, never()).obtenerEntidad(usuarioId);
        verify(perfilRepository, never()).save(datos);
    }

    @Test
    void crearAsociaElUsuarioExistenteYGuardaElPerfil() {
        UUID usuarioId = UUID.randomUUID();
        Admin usuarioReferencia = new Admin(null, null, null);
        usuarioReferencia.setId(usuarioId);
        Admin usuarioExistente = new Admin("Ana", "ana@example.com", "hash");
        usuarioExistente.setId(usuarioId);
        PerfilUsuario datos = new PerfilUsuario("Bio", "123", null);
        datos.setUsuario(usuarioReferencia);
        when(perfilRepository.existsByUsuario_Id(usuarioId)).thenReturn(false);
        when(usuarioService.obtenerEntidad(usuarioId)).thenReturn(usuarioExistente);
        when(perfilRepository.save(datos)).thenReturn(datos);

        PerfilUsuario resultado = perfilService.crear(datos);

        assertEquals(usuarioExistente, resultado.getUsuario());
        verify(perfilRepository).save(datos);
    }

    @Test
    void actualizarRechazaBiografiaDemasiadoLargaAntesDeBuscarElPerfil() {
        PerfilUsuario datos = new PerfilUsuario("x".repeat(1001), null, null);

        ReglaDeNegocioException exception = assertThrows(
                ReglaDeNegocioException.class, () -> perfilService.actualizar(UUID.randomUUID(), datos));

        assertEquals("La biografía o el teléfono supera la longitud permitida", exception.getMessage());
        verifyNoInteractions(perfilRepository);
    }
}
