package com.cesde.eslclasshallrepositorybackend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eslclasshallrepositorybackend.model.entity.UserProfile;
import com.cesde.eslclasshallrepositorybackend.service.UserProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Profiles", description = "CRUD de profilees de user")
@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    private final UserProfileService profileService;

    public UserProfileController(UserProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<UserProfile> getAll() {
        return profileService.getAll();
    }

    @GetMapping("/{id}")
    public UserProfile getById(@PathVariable UUID id) {
        return profileService.getById(id);
    }

    @PostMapping
    public ResponseEntity<UserProfile> create(@RequestBody UserProfile profile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.create(profile));
    }

    @PutMapping("/{id}")
    public UserProfile update(@PathVariable UUID id, @RequestBody UserProfile profile) {
        return profileService.update(id, profile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
