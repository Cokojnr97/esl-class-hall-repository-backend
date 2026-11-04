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

import com.cesde.eslclasshallrepositorybackend.model.entity.User;
import com.cesde.eslclasshallrepositorybackend.model.enums.UserRole;
import com.cesde.eslclasshallrepositorybackend.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Administrators", description = "CRUD de users administrators")
@RestController
@RequestMapping("/api/administrators")
public class AdministratorController {

    private final UserService userService;

    public AdministratorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll().stream().filter(user -> user.getRole() == UserRole.ADMIN).toList();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable UUID id) {
        return userService.obtenerAdministrator(id);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user, UserRole.ADMIN));
    }

    @PutMapping("/{id}")
    public User update(@PathVariable UUID id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
