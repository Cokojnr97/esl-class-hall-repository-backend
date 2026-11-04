package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.ResourceNotFoundException;
import com.cesde.eslclasshallrepositorybackend.exception.BusinessRuleException;
import com.cesde.eslclasshallrepositorybackend.model.entity.UserProfile;
import com.cesde.eslclasshallrepositorybackend.repository.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository profileRepository;
    private final UserService userService;

    public UserProfileService(UserProfileRepository profileRepository, UserService userService) {
        this.profileRepository = profileRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<UserProfile> getAll() {
        return profileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserProfile getById(UUID id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found con id: " + id));
    }

    @Transactional
    public UserProfile create(UserProfile datos) {
        validate(datos);
        datos.setUser(userService.obtenerEntidad(datos.getUser().getId()));
        return profileRepository.save(datos);
    }

    @Transactional
    public UserProfile update(UUID id, UserProfile datos) {
        if (datos == null) {
            throw new BusinessRuleException("Los datos del profile son obligatorios");
        }
        UserProfile profile = getById(id);
        profile.setBiography(datos.getBiography());
        profile.setPhone(datos.getPhone());
        return profileRepository.save(profile);
    }

    @Transactional
    public void delete(UUID id) {
        profileRepository.delete(getById(id));
    }

    private void validate(UserProfile datos) {
        if (datos == null || datos.getUser() == null || datos.getUser().getId() == null) {
            throw new BusinessRuleException("user.id es obligatorio");
        }
    }
}
