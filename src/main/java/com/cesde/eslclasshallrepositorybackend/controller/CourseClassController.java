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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eslclasshallrepositorybackend.model.entity.CourseClass;
import com.cesde.eslclasshallrepositorybackend.model.enums.CourseLevel;
import com.cesde.eslclasshallrepositorybackend.service.CourseClassService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Course units", description = "Manage academy levels and lesson units")
@RestController
@RequestMapping("/api/courses")
public class CourseClassController {

    private final CourseClassService cursoService;

    public CourseClassController(CourseClassService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<CourseClass> getAll(@RequestParam(required = false) CourseLevel level) {
        return cursoService.getAll(level);
    }

    @GetMapping("/{id}")
    public CourseClass getById(@PathVariable UUID id) {
        return cursoService.getById(id);
    }

    @PostMapping
    public ResponseEntity<CourseClass> create(@RequestBody CourseClass curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.create(curso));
    }

    @PutMapping("/{id}")
    public CourseClass update(@PathVariable UUID id, @RequestBody CourseClass curso) {
        return cursoService.update(id, curso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
