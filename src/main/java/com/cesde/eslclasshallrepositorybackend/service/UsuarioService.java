package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.RecursoNoEncontradoException;
import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.entity.Admin;
import com.cesde.eslclasshallrepositorybackend.model.entity.Docente;
import com.cesde.eslclasshallrepositorybackend.model.entity.Usuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoUsuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;
import com.cesde.eslclasshallrepositorybackend.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorRol(RolUsuario rol) {
        if (rol == null) {
            throw new ReglaDeNegocioException("El rol es obligatorio");
        }
        return usuarioRepository.findByRol(rol);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(UUID id) {
        return obtenerEntidad(id);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con email: " + email));
    }

    @Transactional(readOnly = true)
    public Usuario obtenerEntidad(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Admin obtenerAdmin(UUID id) {
        Usuario usuario = obtenerEntidad(id);
        if (!(usuario instanceof Admin admin)) {
            throw new RecursoNoEncontradoException("Administrador no encontrado con id: " + id);
        }
        return admin;
    }

    @Transactional(readOnly = true)
    public Docente obtenerDocente(UUID id) {
        Usuario usuario = obtenerEntidad(id);
        if (!(usuario instanceof Docente docente)) {
            throw new RecursoNoEncontradoException("Docente no encontrado con id: " + id);
        }
        return docente;
    }

    @Transactional
    public Usuario crear(Usuario datos) {
        validarCrear(datos);
        validarEmailDisponible(datos.getEmail());
        return usuarioRepository.save(crearSubtipo(datos));
    }

    @Transactional
    public Usuario crear(Usuario datos, RolUsuario rol) {
        validarCrear(datos, rol);
        validarEmailDisponible(datos.getEmail());
        return usuarioRepository.save(crearSubtipo(datos, rol));
    }

    @Transactional
    public Usuario actualizar(UUID id, Usuario datos) {
        return actualizar(id, datos, null);
    }

    @Transactional
    public Usuario actualizar(UUID id, Usuario datos, RolUsuario rolEsperado) {
        if (datos == null) {
            throw new ReglaDeNegocioException("Los datos del usuario son obligatorios");
        }
        validarActualizacion(datos);
        Usuario usuario;
        if (rolEsperado == RolUsuario.ADMIN) {
            usuario = obtenerAdmin(id);
        } else if (rolEsperado == RolUsuario.DOCENTE) {
            usuario = obtenerDocente(id);
        } else {
            usuario = obtenerEntidad(id);
        }
        if (datos.getEmail() != null && !datos.getEmail().equalsIgnoreCase(usuario.getEmail())) {
            validarEmailDisponible(datos.getEmail());
            usuario.setEmail(datos.getEmail());
        }
        if (datos.getNombre() != null) {
            usuario.setNombre(datos.getNombre());
        }
        if (datos.getPasswordHash() != null && !datos.getPasswordHash().isBlank()) {
            usuario.setPasswordHash(datos.getPasswordHash());
        }
        if (datos.getEstado() != null) {
            usuario.setEstado(datos.getEstado());
        }
        if (datos.getDireccion() != null) {
            usuario.setDireccion(datos.getDireccion());
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(UUID id) {
        usuarioRepository.delete(obtenerEntidad(id));
    }

    @Transactional
    public void eliminar(UUID id, RolUsuario rolEsperado) {
        if (rolEsperado == RolUsuario.ADMIN) {
            usuarioRepository.delete(obtenerAdmin(id));
        } else if (rolEsperado == RolUsuario.DOCENTE) {
            usuarioRepository.delete(obtenerDocente(id));
        } else {
            eliminar(id);
        }
    }

    private Usuario crearSubtipo(Usuario datos) {
        return crearSubtipo(datos, datos.getRol());
    }

    private Usuario crearSubtipo(Usuario datos, RolUsuario rol) {
        if (rol == RolUsuario.ADMIN) {
            Admin admin = new Admin(datos.getNombre(), datos.getEmail(), datos.getPasswordHash());
            copiarDatos(datos, admin);
            return admin;
        }
        if (rol == RolUsuario.DOCENTE) {
            Docente docente = new Docente(datos.getNombre(), datos.getEmail(), datos.getPasswordHash());
            copiarDatos(datos, docente);
            return docente;
        }
        throw new ReglaDeNegocioException("El rol debe ser ADMIN o DOCENTE");
    }

    private void copiarDatos(Usuario origen, Usuario destino) {
        destino.setEstado(origen.getEstado() == null ? EstadoUsuario.ACTIVE : origen.getEstado());
        destino.setDireccion(origen.getDireccion());
    }

    private void validarCrear(Usuario datos) {
        validarCrear(datos, datos == null ? null : datos.getRol());
    }

    private void validarCrear(Usuario datos, RolUsuario rol) {
        if (datos == null || datos.getNombre() == null || datos.getNombre().isBlank()
                || datos.getEmail() == null || datos.getEmail().isBlank()
                || datos.getPasswordHash() == null || datos.getPasswordHash().isBlank() || rol == null) {
            throw new ReglaDeNegocioException("nombre, passwordHash, email y rol son obligatorios");
        }
        if (datos.getNombre().length() > 120 || datos.getEmail().length() > 180) {
            throw new ReglaDeNegocioException("El nombre o el email supera la longitud permitida");
        }
    }

    private void validarActualizacion(Usuario datos) {
        if (datos.getNombre() != null && (datos.getNombre().isBlank() || datos.getNombre().length() > 120)) {
            throw new ReglaDeNegocioException("El nombre debe contener texto y no superar 120 caracteres");
        }
        if (datos.getEmail() != null && (datos.getEmail().isBlank() || datos.getEmail().length() > 180)) {
            throw new ReglaDeNegocioException("El email debe contener texto y no superar 180 caracteres");
        }
    }

    private void validarEmailDisponible(String email) {
        if (usuarioRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new ReglaDeNegocioException("El email " + email + " ya está en uso");
        }
    }
}