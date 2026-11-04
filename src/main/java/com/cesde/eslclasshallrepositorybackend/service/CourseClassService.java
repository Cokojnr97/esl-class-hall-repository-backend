package com.cesde.eslclasshallrepositorybackend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eslclasshallrepositorybackend.exception.BusinessRuleException;
import com.cesde.eslclasshallrepositorybackend.exception.ResourceNotFoundException;
import com.cesde.eslclasshallrepositorybackend.model.entity.CourseClass;
import com.cesde.eslclasshallrepositorybackend.model.enums.CourseLevel;
import com.cesde.eslclasshallrepositorybackend.repository.CourseClassRepository;

@Service
public class CourseClassService {

    private final CourseClassRepository courseClassRepository;

    public CourseClassService(CourseClassRepository courseClassRepository) {
        this.courseClassRepository = courseClassRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseClass> getAll(CourseLevel level) {
        return level == null
                ? courseClassRepository.findAll()
                : courseClassRepository.findByLevelOrderByUnitNumber(level);
    }

    @Transactional(readOnly = true)
    public CourseClass getById(UUID id) {
        return courseClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course unit not found: " + id));
    }

    @Transactional
    public CourseClass create(CourseClass courseClass) {
        validate(courseClass);
        return courseClassRepository.save(courseClass);
    }

    @Transactional
    public CourseClass update(UUID id, CourseClass input) {
        validate(input);
        CourseClass courseClass = getById(id);
        courseClass.setLevel(input.getLevel());
        courseClass.setUnitNumber(input.getUnitNumber());
        courseClass.setName(input.getName());
        courseClass.setDescription(input.getDescription());
        return courseClassRepository.save(courseClass);
    }

    @Transactional
    public void delete(UUID id) {
        courseClassRepository.delete(getById(id));
    }

    private void validate(CourseClass courseClass) {
        if (courseClass == null
                || courseClass.getLevel() == null
                || courseClass.getUnitNumber() == null
                || courseClass.getUnitNumber() < 1
                || courseClass.getName() == null
                || courseClass.getName().isBlank()) {
            throw new BusinessRuleException(
                    "level, unitNumber, and name are required for a course unit");
        }
    }
}
