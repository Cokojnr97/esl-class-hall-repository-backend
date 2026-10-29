package com.cesde.eslclasshallrepositorybackend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.RecursoNoEncontradoException;
import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;
import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.model.entity.Usuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;
import com.cesde.eslclasshallrepositorybackend.repository.CursoClaseRepository;
import com.cesde.eslclasshallrepositorybackend.repository.RecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UsuarioRepository;

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
        datos.setEstado(EstadoRecurso.BORRADOR);
        return recursoRepository.save(datos);
    }

    @Transactional
    public Recurso actualizar(UUID id, Recurso datos) {
        validar(datos);
        Recurso recurso = obtenerPorId(id);
        Usuario creador = obtenerCreador(datos.getCreadoPor().getId());
        Set<CursoClase> cursos = obtenerCursos(datos.getCursos());

        recurso.setTitulo(datos.getTitulo());
        recurso.setDescripcion(datos.getDescripcion());
        recurso.setUrl(datos.getUrl());
        recurso.setTipo(datos.getTipo());
        recurso.setCreadoPor(creador);
        recurso.setCursos(cursos);
        return recursoRepository.save(recurso);
    }

    @Transactional
    public void eliminar(UUID id) {
        recursoRepository.delete(obtenerPorId(id));
    }

    private void prepararRelaciones(Recurso recurso) {
        recurso.setCreadoPor(obtenerCreador(recurso.getCreadoPor().getId()));
        recurso.setCursos(obtenerCursos(recurso.getCursos()));
    }

    private Usuario obtenerCreador(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
    }

    private Set<CursoClase> obtenerCursos(Set<CursoClase> datos) {
        if (datos == null || datos.isEmpty()) {
            return new HashSet<>();
        }
        if (datos.stream().anyMatch(curso -> curso == null || curso.getId() == null)) {
            throw new ReglaDeNegocioException("Cada curso asociado debe incluir un id");
        }

        Set<UUID> ids = datos.stream()
                .map(CursoClase::getId)
                .collect(Collectors.toSet());
        List<CursoClase> cursos = cursoRepository.findAllById(ids);
        if (cursos.size() != ids.size()) {
            throw new RecursoNoEncontradoException("Uno o más cursos no existen");
        }
        return new HashSet<>(cursos);
    }

    private void validar(Recurso datos) {
        if (datos == null || datos.getTitulo() == null || datos.getTitulo().isBlank() || datos.getTipo() == null
                || datos.getCreadoPor() == null || datos.getCreadoPor().getId() == null) {
            throw new ReglaDeNegocioException("titulo, tipo y creadoPor.id son obligatorios");
        }
        validarReferenciasCursos(datos.getCursos());
        if (datos.getUrl() == null || datos.getUrl().isBlank()) {
            throw new ReglaDeNegocioException("La url o ruta del recurso es obligatoria");
        }
        if (datos.getTitulo().length() > 200
                || datos.getUrl().length() > 500
                || (datos.getDescripcion() != null && datos.getDescripcion().length() > 2000)) {
            throw new ReglaDeNegocioException("El título, descripción o URL supera la longitud permitida");
        }
    }

    private void validarReferenciasCursos(Set<CursoClase> cursos) {
        if (cursos != null && cursos.stream().anyMatch(curso -> curso == null || curso.getId() == null)) {
            throw new ReglaDeNegocioException("Cada curso asociado debe incluir un id");
        }
    }
}