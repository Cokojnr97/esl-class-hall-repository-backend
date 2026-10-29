package com.cesde.eslclasshallrepositorybackend.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.cesde.eslclasshallrepositorybackend.exception.ReglaDeNegocioException;
import com.cesde.eslclasshallrepositorybackend.model.embeddable.Direccion;
import com.cesde.eslclasshallrepositorybackend.model.entity.Admin;
import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;
import com.cesde.eslclasshallrepositorybackend.model.entity.Docente;
import com.cesde.eslclasshallrepositorybackend.model.entity.PerfilUsuario;
import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.model.entity.SolicitudRecurso;
import com.cesde.eslclasshallrepositorybackend.model.entity.Usuario;
import com.cesde.eslclasshallrepositorybackend.model.enums.RolUsuario;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static Usuario toEntity(UsuarioRequestDto dto, RolUsuario rolForzado) {
        return toEntity(dto.nombre(), dto.email(), dto.passwordHash(), dto.estado(), dto.rol(),
                dto.direccion(), rolForzado);
    }

    public static Usuario toEntity(UsuarioCreateRequestDto dto, RolUsuario rolForzado) {
        return toEntity(dto.nombre(), dto.email(), dto.passwordHash(), dto.estado(), dto.rol(),
                dto.direccion(), rolForzado);
    }

    private static Usuario toEntity(String nombre, String email, String passwordHash,
            com.cesde.eslclasshallrepositorybackend.model.enums.EstadoUsuario estado,
            RolUsuario rolSolicitado, DireccionDto direccionDto, RolUsuario rolForzado) {
        RolUsuario rol = rolForzado == null ? rolSolicitado : rolForzado;
        if (rol == null) {
            throw new ReglaDeNegocioException("El rol es obligatorio");
        }

        Usuario usuario = rol == RolUsuario.ADMIN
                ? new Admin(nombre, email, passwordHash)
                : new Docente(nombre, email, passwordHash);
        usuario.setEstado(estado);
        usuario.setDireccion(toEntity(direccionDto));
        return usuario;
    }

    public static Usuario toUpdateEntity(UsuarioRequestDto dto) {
        Usuario usuario = new Admin(dto.nombre(), dto.email(), dto.passwordHash());
        usuario.setEstado(dto.estado());
        usuario.setDireccion(toEntity(dto.direccion()));
        return usuario;
    }

    public static UsuarioResponseDto toDto(Usuario usuario) {
        return new UsuarioResponseDto(usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                usuario.getEstado(), usuario.getRol(), DireccionDto.desde(usuario.getDireccion()),
                usuario.getCreatedAt(), usuario.getUpdatedAt());
    }

    public static CursoClase toEntity(CursoClaseRequestDto dto) {
        Docente docente = new Docente(null, null, null);
        docente.setId(dto.docente().id());
        return new CursoClase(dto.nombre(), dto.descripcion(), dto.orden(), docente);
    }

    public static CursoClaseResponseDto toDto(CursoClase curso) {
        return new CursoClaseResponseDto(curso.getId(), curso.getNombre(), curso.getDescripcion(),
                curso.getOrden(), referencia(curso.getDocente()), curso.getCreatedAt(), curso.getUpdatedAt());
    }

    public static PerfilUsuario toEntity(PerfilUsuarioRequestDto dto) {
        PerfilUsuario perfil = new PerfilUsuario(dto.biografia(), dto.telefono(), null);
        if (dto.usuario() != null) {
            perfil.setUsuario(referenciaUsuario(dto.usuario()));
        }
        return perfil;
    }

    public static PerfilUsuario toEntity(PerfilUsuarioCreateRequestDto dto) {
        return toEntity(new PerfilUsuarioRequestDto(dto.biografia(), dto.telefono(), dto.usuario()));
    }

    public static PerfilUsuarioResponseDto toDto(PerfilUsuario perfil) {
        return new PerfilUsuarioResponseDto(perfil.getId(), perfil.getBiografia(), perfil.getTelefono(),
                referencia(perfil.getUsuario()), perfil.getCreatedAt(), perfil.getUpdatedAt());
    }

    public static Recurso toEntity(RecursoRequestDto dto) {
        Recurso recurso = new Recurso(dto.titulo(), dto.descripcion(), dto.url(), dto.tipo(),
                referenciaUsuario(dto.creadoPor()));
        Set<CursoClase> cursos = dto.cursos() == null
                ? new HashSet<>()
                : dto.cursos().stream().map(DtoMapper::referenciaCurso).collect(Collectors.toSet());
        recurso.setCursos(cursos);
        return recurso;
    }

    public static Recurso toEntity(SolicitudRecursoPayloadDto dto, Usuario creadoPor) {
        Recurso recurso = new Recurso(dto.titulo(), dto.descripcion(), dto.url(), dto.tipo(), creadoPor);
        Set<CursoClase> cursos = dto.cursos() == null
                ? new HashSet<>()
                : dto.cursos().stream().map(DtoMapper::referenciaCurso).collect(Collectors.toSet());
        recurso.setCursos(cursos);
        return recurso;
    }

    public static RecursoResponseDto toDto(Recurso recurso) {
        Set<ReferenciaDto> cursos = recurso.getCursos().stream()
                .map(DtoMapper::referencia)
                .collect(Collectors.toSet());
        return new RecursoResponseDto(recurso.getId(), recurso.getTitulo(), recurso.getDescripcion(),
                recurso.getUrl(), recurso.getTipo(), recurso.getEstado(), referencia(recurso.getCreadoPor()),
                cursos, recurso.getCreatedAt(), recurso.getUpdatedAt());
    }

    public static SolicitudRecurso toEntity(SolicitudRecursoRequestDto dto) {
        Recurso recursoObjetivo = null;
        if (dto.recursoObjetivo() != null) {
            recursoObjetivo = new Recurso(null, null, null, null, null);
            recursoObjetivo.setId(dto.recursoObjetivo().id());
        }
        return new SolicitudRecurso(dto.tipoSolicitud(), dto.payloadJson(),
                referenciaUsuario(dto.solicitadoPor()), recursoObjetivo);
    }

    public static SolicitudRecursoResponseDto toDto(SolicitudRecurso solicitud) {
        return new SolicitudRecursoResponseDto(solicitud.getId(), solicitud.getTipoSolicitud(),
                solicitud.getEstado(), solicitud.getPayloadJson(), referencia(solicitud.getSolicitadoPor()),
                referencia(solicitud.getAprobadoPor()), referencia(solicitud.getRecursoObjetivo()),
                solicitud.getCreatedAt(), solicitud.getUpdatedAt());
    }

    private static Direccion toEntity(DireccionDto dto) {
        return dto == null ? null : new Direccion(dto.calle(), dto.ciudad(), dto.codigoPostal(), dto.pais());
    }

    private static Usuario referenciaUsuario(ReferenciaDto referencia) {
        Usuario usuario = new Admin(null, null, null);
        usuario.setId(referencia.id());
        return usuario;
    }

    private static CursoClase referenciaCurso(ReferenciaDto referencia) {
        CursoClase curso = new CursoClase(null, null, null, null);
        curso.setId(referencia.id());
        return curso;
    }

    private static ReferenciaDto referencia(Usuario usuario) {
        return usuario == null ? null : new ReferenciaDto(usuario.getId(), usuario.getNombre());
    }

    private static ReferenciaDto referencia(CursoClase curso) {
        return curso == null ? null : new ReferenciaDto(curso.getId(), curso.getNombre());
    }

    private static ReferenciaDto referencia(Recurso recurso) {
        return recurso == null ? null : new ReferenciaDto(recurso.getId(), recurso.getTitulo());
    }
    
}
