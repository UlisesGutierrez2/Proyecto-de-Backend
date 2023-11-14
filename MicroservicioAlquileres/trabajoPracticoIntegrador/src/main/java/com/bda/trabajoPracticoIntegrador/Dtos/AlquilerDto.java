package com.bda.trabajoPracticoIntegrador.Dtos;

import com.bda.trabajoPracticoIntegrador.Entity.Tarifas;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AlquilerDto {
    private int id;
    @JsonIgnore
    private String idCliente;
    @JsonIgnore
    private int estado;

    private EstacionDto estacionRetiro;

    private EstacionDto estacionDevolucion;
    @JsonIgnore
    private LocalDateTime fechaHoraRetiro;
    @JsonIgnore
    private LocalDateTime fechaHoraDevolucion;

    @JsonIgnore
    private double monto;
    /*
    @OneToOne
    @JoinColumn(name = "ID_TARIFA")

     */
    @JsonIgnore
    private Tarifas idTarifa;

    public AlquilerDto(int id) {
        this.id = id;
    }
}