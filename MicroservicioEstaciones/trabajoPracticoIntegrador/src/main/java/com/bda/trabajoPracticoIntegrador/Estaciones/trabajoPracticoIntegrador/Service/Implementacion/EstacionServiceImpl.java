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

    // Obtener todas las estaciones de la ciudad
    public List<Estacion> getAll() {
        return repository.findAll();
    }

    public Estacion getEstacionMasCercana(double latitud, double longitud) {
        //Se obtienen todas las estaciones
        List<Estacion> estacionList = repository.findAll();

        Estacion estacionMasCercana = estacionList.get(0);
        double minimaDistancia = calcularDistancia(latitud, longitud, estacionMasCercana.getLatitud(), estacionMasCercana.getLongitud());

        // Se itera la lista y se calcula la estacion mas cercana a la latitud y longitud presentadas.
        for (Estacion e : estacionList) {
            double distancia = calcularDistancia(latitud, longitud, e.getLatitud(), e.getLongitud());
            if (distancia < minimaDistancia) {
                minimaDistancia = distancia;
                estacionMasCercana = e;
            }
        }

        return estacionMasCercana;
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
