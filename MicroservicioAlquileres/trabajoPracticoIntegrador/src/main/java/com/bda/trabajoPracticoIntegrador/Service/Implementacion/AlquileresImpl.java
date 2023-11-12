package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;
import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;
import com.bda.trabajoPracticoIntegrador.Repository.AlquileresRepository;
import com.bda.trabajoPracticoIntegrador.Service.Interface.AlquileresService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AlquileresImpl implements AlquileresService {
    private AlquileresRepository repository;

    public AlquileresImpl(AlquileresRepository repository) {
        this.repository = repository;
    }




    @Override
    public Alquileres update(int id, Alquileres alquileres) {
        Optional<Alquileres> optionalAlquileres = repository.findById(id);

        if(optionalAlquileres.isPresent()) {
            Alquileres alquilerEncontrado = optionalAlquileres.get();

            alquilerEncontrado.setMonto(alquileres.getMonto());

            return repository.save(alquilerEncontrado);

        }
        else {
            throw new NoSuchElementException("Alquiler no encontrado");
        }
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public Alquileres getById(int id) {
        Optional<Alquileres> optionalAlquileres = repository.findById(id);
        return optionalAlquileres.orElse(null);
    }

    public Alquileres iniciarAlquiler(String idCliente, int estacionRetiroId) {
        // Verificar si el cliente está registrado en el sistema
        // Realizar las validaciones necesarias antes de iniciar el alquiler
        // (por ejemplo, asegurarse de que el cliente esté registrado y la estación tenga una bicicleta disponible)

        // Obtener la información de la estación de retiro

        // Crear una instancia de Alquileres y asignar los valores necesarios
        Alquileres nuevoAlquiler = new Alquileres();
        nuevoAlquiler.setIdCliente(idCliente);
        nuevoAlquiler.setEstado(1);
        nuevoAlquiler.setEstacionRetiro(estacionRetiroId);
        nuevoAlquiler.setFechaHoraRetiro(LocalDateTime.now());

        // Guardar la instancia en la base de datos
        return repository.save(nuevoAlquiler);
    }

    @Override
    public List<Alquileres> getAll() {
        return repository.findAll();
    }
}
