package com.bda.trabajoPracticoIntegrador.Dtos;

import lombok.Data;

@Data
public class AlquilerDto {
    private int id;
    private String idCliente;
    private int estado;
    private EstacionDto estacionRetiro;
    private EstacionDto estacionDevolucion;
    private String fechaHoraRetiro;
    private String fechaHoraDevolucion;
    private double monto;
}