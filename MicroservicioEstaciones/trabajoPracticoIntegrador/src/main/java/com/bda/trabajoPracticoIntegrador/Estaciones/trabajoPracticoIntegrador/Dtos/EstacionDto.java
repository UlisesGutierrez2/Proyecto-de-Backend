package com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EstacionDto {

    private int id;

    @JsonIgnore
    private String nombre;

    @JsonIgnore
    private double latitud;

    @JsonIgnore
    private double longitud;
}
