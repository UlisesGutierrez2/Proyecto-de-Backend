package com.bda.trabajoPracticoIntegrador.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.NoSuchElementException;
import java.util.Optional;

@Data
public class EstacionDto {
    private int id;

    private String nombre;

    private double latitud;

    private double longitud;
}
