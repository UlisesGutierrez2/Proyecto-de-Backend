package com.bda.trabajoPracticoIntegrador.Service.Interface;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;

public interface EstacionService {
    EstacionDto obtenerEstacionDto(int id);
    double calcularDistancia(double latitudOrigen, double longitudOrigen, double latitudDestino, double longitudDestino);
}