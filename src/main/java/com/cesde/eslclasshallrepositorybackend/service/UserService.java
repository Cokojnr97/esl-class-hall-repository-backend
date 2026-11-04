package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.ResourceNotFoundException;
import com.cesde.eslclasshallrepositorybackend.exception.BusinessRuleException;
import com.cesde.eslclasshallrepositorybackend.model.entity.Administrator;
import com.cesde.eslclasshallrepositorybackend.model.entity.Teacher;
import com.cesde.eslclasshallrepositorybackend.model.entity.User;
import com.cesde.eslclasshallrepositorybackend.model.enums.UserStatus;
import com.cesde.eslclasshallrepositorybackend.model.enums.UserRole;
import com.cesde.eslclasshallrepositorybackend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(UUID id) {
        return obtenerEntidad(id);
    }

    @Transactional(readOnly = true)
    public User obtenerPorEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con email: " + email));
    }

    @Transactional(readOnly = true)
    public User obtenerEntidad(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Administrator obtenerAdministrator(UUID id) {
        User user = obtenerEntidad(id);
        if (!(user instanceof Administrator admin)) {
            throw new ResourceNotFoundException("Administratoristrador no encontrado con id: " + id);
        }
        return admin;
    }

    @Transactional(readOnly = true)
    public Teacher obtenerTeacher(UUID id) {
        User user = obtenerEntidad(id);
        if (!(user instanceof Teacher teacher)) {
            throw new ResourceNotFoundException("Teacher no encontrado con id: " + id);
        }
        return teacher;
    }

    @Transactional
    public User create(User datos) {
        validateCrear(datos);
        validateEmailDisponible(datos.getEmail());
        return userRepository.save(createSubtype(datos));
    }

    @Transactional
    public User create(User datos, UserRole role) {
        validateCrear(datos, role);
        validateEmailDisponible(datos.getEmail());
        return userRepository.save(createSubtype(datos, role));
    }

    @Transactional
    public User update(UUID id, User datos) {
        if (datos == null) {
            throw new BusinessRuleException("Los datos del user son obligatorios");
        }
        User user = obtenerEntidad(id);
        if (datos.getEmail() != null && !datos.getEmail().equalsIgnoreCase(user.getEmail())) {
            validateEmailDisponible(datos.getEmail());
            user.setEmail(datos.getEmail());
        }
        if (datos.getName() != null) {
            user.setName(datos.getName());
        }
        if (datos.getPasswordHash() != null && !datos.getPasswordHash().isBlank()) {
            user.setPasswordHash(datos.getPasswordHash());
        }
        if (datos.getStatus() != null) {
            user.setStatus(datos.getStatus());
        }
        if (datos.getAddress() != null) {
            user.setAddress(datos.getAddress());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void delete(UUID id) {
        userRepository.delete(obtenerEntidad(id));
    }

    private User createSubtype(User datos) {
        return createSubtype(datos, datos.getRole());
    }

    private User createSubtype(User datos, UserRole role) {
        if (role == UserRole.ADMIN) {
            Administrator admin = new Administrator(datos.getName(), datos.getEmail(), datos.getPasswordHash());
            copiarDatos(datos, admin);
            return admin;
        }
        if (role == UserRole.TEACHER) {
            Teacher teacher = new Teacher(datos.getName(), datos.getEmail(), datos.getPasswordHash());
            copiarDatos(datos, teacher);
            return teacher;
        }
        throw new BusinessRuleException("El role debe ser ADMIN o TEACHER");
    }

    private void copiarDatos(User origen, User destino) {
        destino.setStatus(origen.getStatus() == null ? UserStatus.ACTIVE : origen.getStatus());
        destino.setAddress(origen.getAddress());
    }

    private void validateCrear(User datos) {
        validateCrear(datos, datos == null ? null : datos.getRole());
    }

    private void validateCrear(User datos, UserRole role) {
        if (datos == null || datos.getName() == null || datos.getEmail() == null
                || datos.getPasswordHash() == null || role == null) {
            throw new BusinessRuleException("name, passwordHash, email y role son obligatorios");
        }
    }

    private void validateEmailDisponible(String email) {
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new BusinessRuleException("El email " + email + " ya estÃ¡ en uso");
        }
    }
}
