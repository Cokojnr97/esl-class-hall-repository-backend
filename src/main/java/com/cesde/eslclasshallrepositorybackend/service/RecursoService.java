package com.cesde.eslclasshallrepositorybackend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.RecursoNoEncontradoException;
import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;
import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.repository.CursoClaseRepository;
import com.cesde.eslclasshallrepositorybackend.repository.RecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UsuarioRepository;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoClaseRepository cursoRepository;

    public RecursoService(RecursoRepository recursoRepository, UsuarioRepository usuarioRepository,
            CursoClaseRepository cursoRepository) {
        this.recursoRepository = recursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional(readOnly = true)
    public List<Recurso> obtenerTodos() {
        return recursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Recurso> obtenerPublicados() {
        return recursoRepository.findByEstado(EstadoRecurso.PUBLICADO);
    }

    @Transactional(readOnly = true)
    public Recurso obtenerPorId(UUID id) {
        return recursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Recurso no encontrado con id: " + id));
    }

    @Transactional
    public Recurso crear(Recurso datos) {
        validar(datos);
        prepararRelaciones(datos);
        return recursoRepository.save(datos);
    }

    @Transactional
    public Recurso actualizar(UUID id, Recurso datos) {
        validar(datos);
        Recurso recurso = obtenerPorId(id);
        recurso.setTitulo(datos.getTitulo());
        recurso.setDescripcion(datos.getDescripcion());
        recurso.setUrl(datos.getUrl());
        recurso.setTipo(datos.getTipo());
        recurso.setCreadoPor(usuarioRepository.findById(datos.getCreadoPor().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + datos.getCreadoPor().getId())));
        recurso.setCursos(obtenerCursos(datos.getCursos()));
        return recursoRepository.save(recurso);
    }

    @Transactional
    public void eliminar(UUID id) {
        recursoRepository.delete(obtenerPorId(id));
    }

    private void prepararRelaciones(Recurso recurso) {
        recurso.setCreadoPor(usuarioRepository.findById(recurso.getCreadoPor().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + recurso.getCreadoPor().getId())));
        recurso.setCursos(obtenerCursos(recurso.getCursos()));
    }

    private Set<CursoClase> obtenerCursos(Set<CursoClase> datos) {
        if (datos == null || datos.isEmpty()) {
            return new HashSet<>();
        }
        Set<UUID> ids = datos.stream().map(CursoClase::getId).collect(java.util.stream.Collectors.toSet());
        List<CursoClase> cursos = cursoRepository.findAllById(ids);
        if (cursos.size() != ids.size()) {
            throw new RecursoNoEncontradoException("Uno o más cursos no existen");
        }
        return new HashSet<>(cursos);
    }

    private void validar(Recurso datos) {
        if (datos == null || datos.getTitulo() == null || datos.getTipo() == null
                || datos.getCreadoPor() == null || datos.getCreadoPor().getId() == null) {
            throw new ReglaDeNegocioException("titulo, tipo y creadoPor.id son obligatorios");
        }
        if (datos.getUrl() == null || datos.getUrl().isBlank()) {
            throw new ReglaDeNegocioException("La url o ruta del recurso es obligatoria");
        }
    }
}