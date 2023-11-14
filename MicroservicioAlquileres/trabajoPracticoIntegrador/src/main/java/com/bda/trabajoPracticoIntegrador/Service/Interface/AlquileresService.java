package com.bda.trabajoPracticoIntegrador.Service.Interface;

import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;

import java.util.List;

public interface AlquileresService {

 // Alquileres add(Alquileres alquileres);
 Alquileres update(int id, Alquileres alquileres);

 void delete(int id);

 Alquileres getById(int id);

 List<Alquileres> getAll();

 Alquileres iniciarAlquiler(String idCliente, int estacionRetiroId, int estacionDevolucionId);

 Alquileres finalizarAlquiler(AlquilerDto alquilerDto, String moneda);
}
