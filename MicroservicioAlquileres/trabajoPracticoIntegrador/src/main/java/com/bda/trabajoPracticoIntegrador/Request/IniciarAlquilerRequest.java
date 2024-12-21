package com.bda.trabajoPracticoIntegrador.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IniciarAlquilerRequest {
    private String idCliente;
    private int estacionRetiroId;
    
}
