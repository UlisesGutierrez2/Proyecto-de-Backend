package com.bda.trabajoPracticoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Data;

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
    private int fechaHoraRetiro;

    @Column(name = "FECHA_HORA_DEVOLUCION")
    private int fechaHoraDevolucion;

    @Column(name = "MONTO")
    private int monto;

    @OneToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifas idTarifa;

}
