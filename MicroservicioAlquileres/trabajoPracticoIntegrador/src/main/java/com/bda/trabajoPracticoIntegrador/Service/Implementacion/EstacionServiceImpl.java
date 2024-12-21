package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Service.Interface.EstacionService;

@Service
public class EstacionServiceImpl implements EstacionService{

    private RestTemplate restTemplate;

    public EstacionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public EstacionDto obtenerEstacionDto(int estacionId) {
        String url = "http://localhost:8084/api/estaciones/" + estacionId;
        return restTemplate.getForObject(url, EstacionDto.class);
    }

    public double calcularDistancia(double latitud, double longitud, double latitud2, double longitud2) {

        // Radio de la Tierra en kilometros
        final double radioTierra = 6371;

        // Se calculan las diferencias y se convierte a radianes
        double distLatitud = Math.toRadians(latitud2 - latitud);
        double distLongitud = Math.toRadians(longitud2 - longitud);
        double latRad = Math.toRadians(latitud);
        double lat2Rad = Math.toRadians(latitud2);

        // Se implementa la formula euclidiana
        double calc1 = Math.sin(distLatitud / 2) * Math.sin(distLatitud / 2) +
                Math.cos(latRad) * Math.cos(lat2Rad) * Math.sin(distLongitud / 2) * Math.sin(distLongitud / 2);
        double calcFinal = 2 * Math.atan2(Math.sqrt(calc1), Math.sqrt(1 - calc1));

        // Distancia en kilometros
        double distancia = radioTierra * calcFinal;

        return distancia;
    }

    
}
