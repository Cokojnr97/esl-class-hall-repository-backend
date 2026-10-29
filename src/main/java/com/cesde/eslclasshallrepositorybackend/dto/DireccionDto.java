package com.cesde.eslclasshallrepositorybackend.dto;

import com.cesde.eslclasshallrepositorybackend.model.embeddable.Direccion;

import jakarta.validation.constraints.Size;

public record DireccionDto(
        @Size(max = 120) String calle,
        @Size(max = 80) String ciudad,
        @Size(max = 20) String codigoPostal,
        @Size(max = 80) String pais) {

    public static DireccionDto desde(Direccion direccion) {
        if (direccion == null) {
            return null;
        }
        return new DireccionDto(direccion.getCalle(), direccion.getCiudad(),
                direccion.getCodigoPostal(), direccion.getPais());
    }
}
