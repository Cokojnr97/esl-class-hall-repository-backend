package com.cesde.eslclasshallrepositorybackend.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuditoriaUsuarios {

    @Column(name = "pais", length = 80)
    private String pais;

    @Column(name = "ciudad", length = 120)
    private String ciudad;

    @Column(name = "direccion", length = 200)
    private String direccion;
}
