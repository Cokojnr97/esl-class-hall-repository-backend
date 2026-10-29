package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.RecursoNoEncontradoException;
import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.PerfilUsuario;
import com.cesde.eslclasshallrepositorybackend.repository.PerfilUsuarioRepository;

@Service
public class PerfilUsuarioService {

    private final PerfilUsuarioRepository perfilRepository;
    private final UsuarioService usuarioService;

    public PerfilUsuarioService(PerfilUsuarioRepository perfilRepository, UsuarioService usuarioService) {
        this.perfilRepository = perfilRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<PerfilUsuario> obtenerTodos() {
        return perfilRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PerfilUsuario obtenerPorId(UUID id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil no encontrado con id: " + id));
    }

    @Transactional
    public PerfilUsuario crear(PerfilUsuario datos) {
        validar(datos);
        UUID usuarioId = datos.getUsuario().getId();
        if (perfilRepository.existsByUsuario_Id(usuarioId)) {
            throw new ReglaDeNegocioException("El usuario ya tiene un perfil asociado");
        }
        datos.setUsuario(usuarioService.obtenerEntidad(usuarioId));
        return perfilRepository.save(datos);
    }

    @Transactional
    public PerfilUsuario actualizar(UUID id, PerfilUsuario datos) {
        if (datos == null) {
            throw new ReglaDeNegocioException("Los datos del perfil son obligatorios");
        }
        validarCampos(datos);
        PerfilUsuario perfil = obtenerPorId(id);
        perfil.setBiografia(datos.getBiografia());
        perfil.setTelefono(datos.getTelefono());
        return perfilRepository.save(perfil);
    }

    @Transactional
    public void eliminar(UUID id) {
        perfilRepository.delete(obtenerPorId(id));
    }

    private void validar(PerfilUsuario datos) {
        if (datos == null || datos.getUsuario() == null || datos.getUsuario().getId() == null) {
            throw new ReglaDeNegocioException("usuario.id es obligatorio");
        }
        validarCampos(datos);
    }

    private void validarCampos(PerfilUsuario datos) {
        if (datos.getBiografia() != null && datos.getBiografia().length() > 1000
                || datos.getTelefono() != null && datos.getTelefono().length() > 30) {
            throw new ReglaDeNegocioException("La biografía o el teléfono supera la longitud permitida");
        }
    }
}