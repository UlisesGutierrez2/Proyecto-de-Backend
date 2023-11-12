package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;
import com.bda.trabajoPracticoIntegrador.Repository.AlquileresRepository;
import com.bda.trabajoPracticoIntegrador.Service.Interface.AlquileresService;
import org.springframework.stereotype.Service;

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
    public Alquileres add(Alquileres alquileres) {
        return repository.save(alquileres);
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

    /*
    public EstacionDto getByIdEstacionDto(int id) {
        Optional<EstacionDto> optionalEstacionDto = repository.findById(id);

        if (optionalEstacionDto.isPresent()) {
            return optionalEstacionDto.get();
        } else {
            throw new NoSuchElementException("Estación con ID " + id + " no encontrada");
        }
    }


     */

    @Override
    public List<Alquileres> getAll() {
        return repository.findAll();
    }
}
