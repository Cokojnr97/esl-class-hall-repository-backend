package com.cesde.eslclasshallrepositorybackend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.cesde.eslclasshallrepositorybackend.model.entity.Admin;

import tools.jackson.databind.ObjectMapper;

class DtoMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void usuarioResponseOmitsPasswordHash() throws Exception {
        Admin usuario = new Admin("Ana", "ana@example.com", "secret-hash");

        UsuarioResponseDto response = DtoMapper.toDto(usuario);
        String json = objectMapper.writeValueAsString(response);

        assertEquals("Ana", response.nombre());
        assertEquals("ana@example.com", response.email());
        assertFalse(json.contains("passwordHash"));
        assertFalse(json.contains("secret-hash"));
    }

    @Test
    void cursoRequestMapsDocenteReferenceById() {
        var docenteId = UUID.randomUUID();
        CursoClaseRequestDto request = new CursoClaseRequestDto(
                "Curso", "Descripcion", Integer.valueOf(1), new ReferenciaDto(docenteId, null));

        var curso = DtoMapper.toEntity(request);

        assertEquals(docenteId, curso.getDocente().getId());
        assertEquals("Curso", curso.getNombre());
    }

    @Test
    void direccionSeMapeaEnAmbasDirecciones() {
        DireccionDto direccionDto = new DireccionDto("Calle 10", "Medellin", "050001", "Colombia");
        UsuarioRequestDto request = new UsuarioRequestDto(
                null, null, null, null, null, direccionDto);

        Admin usuario = (Admin) DtoMapper.toEntity(request,
                com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario.ADMIN);
        DireccionDto resultado = DtoMapper.toDto(usuario).direccion();

        assertEquals("Calle 10", resultado.calle());
        assertEquals("Medellin", resultado.ciudad());
        assertEquals("050001", resultado.codigoPostal());
        assertEquals("Colombia", resultado.pais());
    }
}
