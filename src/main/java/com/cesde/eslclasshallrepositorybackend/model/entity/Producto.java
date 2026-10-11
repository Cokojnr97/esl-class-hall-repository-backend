package com.cesde.eslclasshallrepositorybackend.model.entity;

import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;
import com.cesde.eslclasshallrepositorybackend.model.enums.EstadoProducto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Producto extends BaseEntity {

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "precio")
    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoProducto estado;
}
