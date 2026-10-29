package com.cesde.eslclasshallrepositorybackend.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.cesde.eslclasshallrepositorybackend.model.enums.TipoRecurso;

import jakarta.validation.Validation;

class SolicitudRecursoPayloadDtoTest {

    @Test
    void validaCamposRequeridosYReferenciasDeCursos() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var payloadInvalido = new SolicitudRecursoPayloadDto(
                    " ", null, "", null, Set.of(new ReferenciaDto(null, null)));

            assertFalse(validator.validate(payloadInvalido).isEmpty());

            var payloadValido = new SolicitudRecursoPayloadDto(
                    "Recurso", null, "https://example.com/recurso", TipoRecurso.ENLACE,
                    Set.of(new ReferenciaDto(UUID.randomUUID(), null)));
            assertTrue(validator.validate(payloadValido).isEmpty());
        }
    }
}
