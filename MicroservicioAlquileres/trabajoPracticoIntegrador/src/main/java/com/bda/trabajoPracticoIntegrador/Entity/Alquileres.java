package com.bda.trabajoPracticoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "ALQUILERES")
@Data
public class Alquileres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "ID_CLIENTE")
    private String idCliente;

    @Column(name = "ESTADO")
    private int estado;


    @Column(name = "ESTACION_RETIRO", nullable = false)
    private int estacionRetiro;


    @Column(name = "ESTACION_DEVOLUCION", nullable = false)
    private int estacionDevolucion;

    @Column(name = "FECHA_HORA_RETIRO")
    private String fechaHoraRetiro;

    @Column(name = "FECHA_HORA_DEVOLUCION")
    private String fechaHoraDevolucion;

    @Column(name = "MONTO")
    private double monto;

    @OneToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifas idTarifa;

}
