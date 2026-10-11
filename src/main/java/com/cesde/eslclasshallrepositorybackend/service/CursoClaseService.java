package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.RecursoNoEncontradoException;
import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;
import com.cesde.eslclasshallrepositorybackend.repository.CursoClaseRepository;
import com.cesde.eslclasshallrepositorybackend.repository.DocenteRepository;

@Service
public class CursoClaseService {

    private final CursoClaseRepository cursoRepository;
    private final DocenteRepository docenteRepository;

    public CursoClaseService(CursoClaseRepository cursoRepository, DocenteRepository docenteRepository) {
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
    }

    @Transactional(readOnly = true)
    public List<CursoClase> obtenerTodos() {
        return cursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CursoClase obtenerPorId(UUID id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<CursoClase> obtenerPorDocente(UUID docenteId) {
        return cursoRepository.findByDocenteId(docenteId);
    }

    @Transactional
    public CursoClase crear(CursoClase datos) {
        validar(datos);
        datos.setDocente(docenteRepository.findById(datos.getDocente().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Docente no encontrado con id: " + datos.getDocente().getId())));
        return cursoRepository.save(datos);
    }

    @Transactional
    public CursoClase actualizar(UUID id, CursoClase datos) {
        validar(datos);
        CursoClase curso = obtenerPorId(id);
        curso.setNombre(datos.getNombre());
        curso.setDescripcion(datos.getDescripcion());
        curso.setOrden(datos.getOrden());
        curso.setDocente(docenteRepository.findById(datos.getDocente().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Docente no encontrado con id: " + datos.getDocente().getId())));
        return cursoRepository.save(curso);
    }

    @Transactional
    public void eliminar(UUID id) {
        cursoRepository.delete(obtenerPorId(id));
    }

    private void validar(CursoClase datos) {
        if (datos == null || datos.getNombre() == null || datos.getOrden() == null
                || datos.getDocente() == null || datos.getDocente().getId() == null) {
            throw new ReglaDeNegocioException("nombre, orden y docente.id son obligatorios");
        }
    }
}