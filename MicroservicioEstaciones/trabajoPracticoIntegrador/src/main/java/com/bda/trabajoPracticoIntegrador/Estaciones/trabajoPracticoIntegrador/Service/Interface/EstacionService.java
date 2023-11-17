package com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Service.Interface;

import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Entity.Estacion;

import java.util.List;

public interface EstacionService {
    Estacion add(Estacion estacion);

    Estacion update(int id, Estacion estacion);

    void delete(int id);

    Estacion getById(int id);

    List<Estacion> getAll();

    Estacion getEstacionMasCercana(double latitud, double longitud);

    double calcularDistancia(double latitud, double longitud, double latitud2, double longitud2);
}
