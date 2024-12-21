package com.bda.trabajoPracticoIntegrador.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TARIFAS")
@Data
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @JsonIgnore
    @Column(name = "TIPO_TARIFA")
    private int tipoTarifa;

    @JsonIgnore
    @Column(name = "DEFINICION")
    private char definicion;

    @JsonIgnore
    @Column(name = "DIA_SEMANA")
    private Integer diaSemana;

    @JsonIgnore
    @Column(name = "DIA_MES")
    private Integer diaMes;

    @JsonIgnore
    @Column(name = "MES")
    private Integer mes;

    @JsonIgnore
    @Column(name = "ANIO")
    private Integer anio;

    @JsonIgnore
    @Column(name = "MONTO_FIJO_ALQUILER")
    private int montoFijoAlquiler;

    @JsonIgnore
    @Column(name = "MONTO_MINUTO_FRACCION")
    private int montoMinutoFraccion;

    @JsonIgnore
    @Column(name = "MONTO_KM")
    private int montoKm;

    @JsonIgnore
    @Column(name = "MONTO_HORA")
    private int montoHora;

}
