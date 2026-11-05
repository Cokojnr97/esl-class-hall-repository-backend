package com.cesde.eslclasshallrepositorybackend.model.entity;

import com.cesde.eslclasshallrepositorybackend.model.enums.UserRole;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("TEACHER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Teacher extends User {

    public Teacher(String name, String email, String passwordHash) {
        super(name, email, passwordHash, UserRole.TEACHER);
    }
}
