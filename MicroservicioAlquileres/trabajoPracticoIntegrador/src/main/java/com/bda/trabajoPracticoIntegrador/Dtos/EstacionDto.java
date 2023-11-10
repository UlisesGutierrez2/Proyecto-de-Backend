package com.bda.trabajoPracticoIntegrador.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EstacionDto {
    @JsonIgnore
    private int id;

    private String nombre;
   @JsonIgnore
    private int latitud;
   @JsonIgnore
   private int longitud;


}
