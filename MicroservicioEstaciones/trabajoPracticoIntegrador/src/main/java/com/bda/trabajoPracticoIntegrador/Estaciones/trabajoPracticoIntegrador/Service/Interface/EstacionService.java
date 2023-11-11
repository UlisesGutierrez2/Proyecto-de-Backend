package com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Service.Interface;

import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Entity.Estacion;

import java.util.List;

public interface EstacionService {
    Estacion add(Estacion estacion);

    Estacion update(int id, Estacion estacion);

    void delete(int id);

    Estacion getById(int id);

    List<Estacion> getAll();

    Estacion getByUbicacion(double latitud, double longitud);
}
