package com.bda.trabajoPracticoIntegrador.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinalizarAlquilerRequest {
    private String moneda;
    private int estacionDevolucion;

}
