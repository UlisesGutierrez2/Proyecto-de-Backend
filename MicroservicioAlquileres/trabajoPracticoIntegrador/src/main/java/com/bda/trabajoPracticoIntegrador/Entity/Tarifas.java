package com.bda.trabajoPracticoIntegrador.Entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Entity
@Table(name = "TARIFAS")
@Data
public class Tarifas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "TIPO_TARIFA")
    private int tipoTarifa;

    @Column(name = "DEFINICION")
    private char definicion;

    @Column(name = "DIA_SEMANA")
    private Integer diaSemana;

    @Column(name = "DIA_MES")
    private Integer diaMes;

    @Column(name = "MES")
    private Integer mes;

    @Column(name = "ANIO")
    private Integer anio;

    @Column(name = "MONTO_FIJO_ALQUILER")
    private int montoFijoAlquiler;

    @Column(name = "MONTO_MINUTO_FRACCION")
    private int montoMinutoAlquiler;

    @Column(name = "MONTO_KM")
    private int montoKm;

    @Column(name = "MONTO_HORA")
    private int montoHora;

}
