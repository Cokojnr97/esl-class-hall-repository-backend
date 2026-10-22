package com.cesde.eslclasshallrepositorybackend.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<Map<String, String>> manejarReglaDeNegocio(ReglaDeNegocioException ex) {
        return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
    }
}
