package com.cesde.eslclasshallrepositorybackend.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cesde.eslclasshallrepositorybackend.model.entity.Admin;
import com.cesde.eslclasshallrepositorybackend.model.entity.CursoClase;
import com.cesde.eslclasshallrepositorybackend.model.entity.Docente;
import com.cesde.eslclasshallrepositorybackend.model.entity.PerfilUsuario;
import com.cesde.eslclasshallrepositorybackend.model.entity.Recurso;
import com.cesde.eslclasshallrepositorybackend.model.entity.SolicitudRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoSolicitud;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoRecurso;
import com.cesde.eslclasshallrepositorybackend.model.enums.TipoSolicitud;
import com.cesde.eslclasshallrepositorybackend.repository.CursoClaseRepository;
import com.cesde.eslclasshallrepositorybackend.repository.PerfilUsuarioRepository;
import com.cesde.eslclasshallrepositorybackend.repository.RecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.SolicitudRecursoRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UsuarioRepository;

@Configuration
public class DataSeederConfig {

    @Bean
    public CommandLineRunner seedDatabase(
            UsuarioRepository usuarioRepository,
            PerfilUsuarioRepository perfilRepository,
            CursoClaseRepository cursoRepository,
            RecursoRepository recursoRepository,
            SolicitudRecursoRepository solicitudRepository) {
        return args -> {
            if (usuarioRepository.count() > 0) {
                return;
            }

            Admin admin = usuarioRepository.save(new Admin(
                    "Administradora ESL Class Hall",
                    "admin@eslclasshall.com",
                    "admin123"));
            Docente docenteAna = usuarioRepository.save(new Docente(
                    "Ana Rodriguez",
                    "ana.rodriguez@eslclasshall.com",
                    "docente123"));
            Docente docenteCarlos = usuarioRepository.save(new Docente(
                    "Carlos Martinez",
                    "carlos.martinez@eslclasshall.com",
                    "docente123"));

            perfilRepository.saveAll(Set.of(
                    new PerfilUsuario("Coordinadora del repositorio digital.", "+57 300 000 0001", admin),
                    new PerfilUsuario("Docente de ingles general y conversacion.", "+57 300 000 0002", docenteAna),
                    new PerfilUsuario("Docente de preparacion para examenes.", "+57 300 000 0003", docenteCarlos)));

            CursoClase inglesGeneral = cursoRepository.save(new CursoClase(
                    "Ingles general B1",
                    "Materiales para desarrollar competencias comunicativas de nivel B1.",
                    1,
                    docenteAna));
            CursoClase preparacionExamenes = cursoRepository.save(new CursoClase(
                    "Preparacion para examenes",
                    "Recursos para practicar pruebas internacionales de ingles.",
                    2,
                    docenteCarlos));

            Recurso video = new Recurso(
                    "Video: conversaciones cotidianas",
                    "Video para practicar expresiones de la vida diaria.",
                    "https://www.youtube.com/watch?v=example-b1",
                    TipoRecurso.VIDEO,
                    docenteAna);
            video.setEstado(EstadoRecurso.PUBLICADO);
            video.setCursos(Set.of(inglesGeneral));

            Recurso guia = new Recurso(
                    "Guia de practica IELTS",
                    "Documento pendiente de aprobacion por el administrador.",
                    "/recursos/guia-practica-ielts.pdf",
                    TipoRecurso.DOCUMENTO,
                    docenteCarlos);
            guia.setCursos(Set.of(preparacionExamenes));

            recursoRepository.saveAll(Set.of(video, guia));

            solicitudRepository.save(new SolicitudRecurso(
                    TipoSolicitud.CREAR,
                    "Solicitud inicial del recurso Guia de practica IELTS",
                    docenteCarlos,
                    guia));
            SolicitudRecurso solicitudAprobada = solicitudRepository.save(new SolicitudRecurso(
                    TipoSolicitud.CREAR,
                    "Solicitud aprobada para el video de conversaciones cotidianas",
                    docenteAna,
                    video));
            solicitudAprobada.setEstado(EstadoSolicitud.APROBADA);
            solicitudAprobada.setAprobadoPor(admin);
            solicitudRepository.save(solicitudAprobada);
        };
    }
}
