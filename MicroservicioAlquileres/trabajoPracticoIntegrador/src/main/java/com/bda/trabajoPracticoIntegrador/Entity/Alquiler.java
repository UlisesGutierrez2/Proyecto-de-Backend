package com.bda.trabajoPracticoIntegrador.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "ALQUILERES")
@Data
public class Alquiler {

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
    private LocalDateTime fechaHoraRetiro;

    @Column(name = "FECHA_HORA_DEVOLUCION")
    private LocalDateTime fechaHoraDevolucion;

    @Column(name = "MONTO")
    private double monto;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifa idTarifa;



    public void iniciar(String idCliente, int estacionRetiroId) {
        this.idCliente = idCliente;
        this.estacionRetiro = estacionRetiroId;
        estado = 1;
        fechaHoraRetiro = LocalDateTime.now();
    }

    public void finalizar(double montoFinal, int estacionDevolucion) {
        this.monto = montoFinal;
        estado = 2;
        this.estacionDevolucion = estacionDevolucion;

    }

    public double calcularMontoTotal(Tarifa tarifa, double distanciaEnKm) {

        double costoMinutosFraccionados = 0;

        double montoFijoAlquiler;


        if (tarifa.getTipoTarifa() == 2) {

            // Tarifa tarifa = tarifaOpt.get();

            double costoPorKm = tarifa.getMontoKm(); // Obtén el precio adicional por kilómetro desde la tarifa

            // Calcular el costo adicional por la distancia recorrida
            double costoAdicionalDistancia = distanciaEnKm * costoPorKm;


            montoFijoAlquiler = tarifa.getMontoFijoAlquiler();


            // Calcular la duración del alquiler en minutos
            long duracionEnMinutos = calcularDuracionEnMinutos(getFechaHoraRetiro(), getFechaHoraDevolucion());

            int cantHoras = (int) (duracionEnMinutos / 60);

            int excedenteMinutos = (int) (duracionEnMinutos % 60);

            if (excedenteMinutos <= 30) {

                costoMinutosFraccionados = Math.min(duracionEnMinutos, 30) * tarifa.getMontoMinutoFraccion();

            } else {
                cantHoras += 1;
            }

            // Calcular el costo por hora completa a partir del minuto 31
            double costoHorasCompletas = Math.ceil(cantHoras * tarifa.getMontoHora());

            // Calcular el costo total sumando todas las partes
            double costoTotal = montoFijoAlquiler + costoMinutosFraccionados + costoHorasCompletas + costoAdicionalDistancia;


            return costoTotal;

        } else {
            // No hay tarifa específica para el día, mes y año
            // Se puede aplicar la lógica basada en el día de la semana
            // Optional<Tarifa> tarifaDiaOpt = tarifasRepository.findByDay(diaSemana);

                // Existe una tarifa para el día de la semana
                // Tarifa tarifaDia = tarifaDiaOpt.get();

                double costoPorKm = tarifa.getMontoKm(); // Obtén el precio adicional por kilómetro desde la tarifa

                // Calcular el costo adicional por la distancia recorrida
                double costoAdicionalDistancia = distanciaEnKm * costoPorKm;


                montoFijoAlquiler = tarifa.getMontoFijoAlquiler();

                // Calcular la duración del alquiler en minutos
                long duracionEnMinutos = calcularDuracionEnMinutos(getFechaHoraRetiro(), getFechaHoraDevolucion());

                int cantHoras = (int) (duracionEnMinutos / 60);
                int excedenteMinutos = (int) (duracionEnMinutos % 60);

                if (excedenteMinutos <= 30) {
                    costoMinutosFraccionados = Math.min(duracionEnMinutos, 30) * tarifa.getMontoMinutoFraccion();
                } else {
                    cantHoras += 1;
                }

                // Calcular el costo por hora completa a partir del minuto 31
                double costoHorasCompletas = Math.ceil(cantHoras * tarifa.getMontoHora());

                // Calcular el costo total sumando todas las partes
                double costoTotal = montoFijoAlquiler + costoMinutosFraccionados + costoHorasCompletas + costoAdicionalDistancia;

                return costoTotal;

        }
    }

    private long calcularDuracionEnMinutos(LocalDateTime fechaHoraRetiro, LocalDateTime fechaHoraDevolucion) {

        Duration duration = Duration.between(fechaHoraRetiro, fechaHoraDevolucion);
        // Devuelve la diferencia en minutos
        return duration.toMinutes();
    }
}
