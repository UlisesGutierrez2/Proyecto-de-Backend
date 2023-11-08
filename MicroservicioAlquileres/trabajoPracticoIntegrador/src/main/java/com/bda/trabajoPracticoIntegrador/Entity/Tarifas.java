package com.bda.trabajoPracticoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ESTACIONES")
@Data
public class Tarifas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "TIPO_TARIFA")
    private int tipoTarifa;

    @Column(name = "DEFINICIÓN")
    private String definicion;

    @Column(name = "DIA_SEMANA")
    private int diaSemana;

    @Column(name = "DIA_MES")
    private int diaMes;

    @Column(name = "MES")
    private int mes;

    @Column(name = "ANIO")
    private int anio;

    @Column(name = "MONTO_FIJO_ALQUILER")
    private int montoFijoAlquiler;

    @Column(name = "MONTO_MINUTO_FRACCION")
    private int montoMinutoAlquiler;

    @Column(name = "MONTO_KM")
    private int montoKm;

    @Column(name = "MONTO_HORA")
    private int montoHora;

}
