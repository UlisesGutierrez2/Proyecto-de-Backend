package com.bda.trabajoPracticoIntegrador.Dtos;

import com.bda.trabajoPracticoIntegrador.Entity.Tarifas;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AlquilerDto {
    private int id;
    private String idCliente;
    private int estado;
    private EstacionDto estacionRetiro;
    private EstacionDto estacionDevolucion;
    private LocalDateTime fechaHoraRetiro;
    private LocalDateTime fechaHoraDevolucion;
    private double monto;
    /*
    @OneToOne
    @JoinColumn(name = "ID_TARIFA")

     */
    private Tarifas idTarifa;

    public AlquilerDto(int id) {
        this.id = id;
    }
}