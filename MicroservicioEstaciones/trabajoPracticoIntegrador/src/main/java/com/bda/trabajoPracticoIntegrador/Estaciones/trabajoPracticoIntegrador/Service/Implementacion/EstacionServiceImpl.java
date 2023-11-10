package com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Service.Implementacion;

        import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Entity.Estacion;
        import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Repository.EstacionRepository;
        import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Service.Interface.EstacionService;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.NoSuchElementException;
        import java.util.Optional;

@Service
public class EstacionServiceImpl implements EstacionService {
    private EstacionRepository repository;

    public EstacionServiceImpl(EstacionRepository repository) {
        this.repository = repository;
    }

    public Estacion add(Estacion estacion) {
        return repository.save(estacion);
    }

    public Estacion update(int id, Estacion estacion) {
        Optional<Estacion> optionalEstacion = repository.findById(id);

        if(optionalEstacion.isPresent()) {
            Estacion estacionEncontrado = optionalEstacion.get();

            estacionEncontrado.setNombre(estacion.getNombre());
            estacionEncontrado.setLatitud(estacion.getLatitud());
            estacionEncontrado.setLongitud(estacion.getLongitud());

            return repository.save(estacionEncontrado);

        }
        else {
            throw new NoSuchElementException("Alquiler no encontrado");
        }
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Estacion getById(int id) {
        Optional<Estacion> optionalEstacion = repository.findById(id);
        return optionalEstacion.orElse(null);
    }

    public List<Estacion> getAll() {
        return repository.findAll();
    }
}
