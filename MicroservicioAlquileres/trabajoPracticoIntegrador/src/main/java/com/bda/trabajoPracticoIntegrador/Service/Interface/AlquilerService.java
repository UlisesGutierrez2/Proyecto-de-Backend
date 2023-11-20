package com.bda.trabajoPracticoIntegrador.Service.Interface;

import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquiler;

import java.util.List;

public interface AlquilerService {

 // Alquileres add(Alquileres alquileres);
 Alquiler update(int id, Alquiler alquiler);

 void delete(int id);

 Alquiler getById(int id);

 List<Alquiler> getAll();

 Alquiler iniciarAlquiler(String idCliente, int estacionRetiroId);

 Alquiler finalizarAlquiler(int id, String moneda);
 
}
