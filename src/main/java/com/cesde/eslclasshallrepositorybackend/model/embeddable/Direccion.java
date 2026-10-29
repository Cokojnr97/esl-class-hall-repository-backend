package com.cesde.eslclasshallrepositorybackend.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Direccion {

    public Direccion(String calle1, String ciudad1, String codigoPostal1, String pais1) {
        this.calle = calle1;
        this.ciudad = ciudad1;
        this.codigoPostal = codigoPostal1;
        this.pais = pais1;
    }

    @Column(length = 120)
    private String calle;

    @Column(length = 80)
    private String ciudad;

    @Column(length = 20)
    private String codigoPostal;

    @Column(length = 80)
    private String pais;

    public String getCalle() {
        return calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getPais() {
        return pais;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

}
