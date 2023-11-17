package com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador;

import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Entity.Estacion;
import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Repository.EstacionRepository;
import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Service.Implementacion.EstacionServiceImpl;
import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Service.Interface.EstacionService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EstacionServiceTests {

    @InjectMocks
    private EstacionServiceImpl estacionServiceImpl;

    @Mock
    private EstacionRepository estacionRepository;

    @Test
    public void testGetById() {
        Estacion estacion = new Estacion();
        estacion.setId(50);
        estacion.setNombre("Arguello");
        estacion.setLatitud(-31.452961123175);
        estacion.setLongitud(-64.2540911211195);

        when(estacionRepository.findById(50)).thenReturn(Optional.of(estacion));

        Estacion resultado = estacionServiceImpl.getById(50);

        assertEquals(estacion.getId(), resultado.getId());
        assertEquals(estacion.getNombre(), resultado.getNombre());

    }

    @Test
    public void testGetAll() {
        Estacion estacion1 = new Estacion();
        estacion1.setId(50);
        estacion1.setNombre("Arguello");
        estacion1.setLatitud(-31.452961123175);
        estacion1.setLongitud(-64.2540911211195);

        Estacion estacion2 = new Estacion();
        estacion2.setId(51);
        estacion2.setNombre("Villa Allende");
        estacion2.setLatitud(-31.462961123175);
        estacion2.setLongitud(-64.2640911211195);

        List<Estacion> estacionList = new ArrayList<>();
        estacionList.add(estacion1);
        estacionList.add(estacion2);

        when(estacionRepository.findAll()).thenReturn(estacionList);

        List<Estacion> resultado = estacionServiceImpl.getAll();

        assertEquals(estacionList.size(), resultado.size());
    }

    @Test
    public void testDelete() {
        Estacion estacion = new Estacion();
        estacion.setId(50);
        estacion.setNombre("Arguello");
        estacion.setLatitud(-31.452961123175);
        estacion.setLongitud(-64.2540911211195);

        doNothing().when(estacionRepository).deleteById(estacion.getId());

        when(estacionRepository.findById(50)).thenReturn(Optional.of(estacion));

        estacionServiceImpl.delete(estacion.getId());

        try {
            estacionServiceImpl.getById(estacion.getId());
        } catch (Exception e) {
            assertTrue(e instanceof ChangeSetPersister.NotFoundException);
        }
    }

    @Test
    public void testAdd() {
        Estacion estacion = new Estacion();
        estacion.setId(50);
        estacion.setNombre("Arguello");
        estacion.setLatitud(-31.452961123175);
        estacion.setLongitud(-64.2540911211195);

        when(estacionRepository.save(any(Estacion.class))).thenReturn(estacion);

        Estacion estacionAgregada = estacionServiceImpl.add(estacion);

        assertEquals(estacion, estacionAgregada);
    }

    @Test
    public void testUpdate() {
        Estacion estacion = new Estacion();
        estacion.setId(50);
        estacion.setNombre("Arguello");
        estacion.setLatitud(-31.452961123175);
        estacion.setLongitud(-64.2540911211195);

        Optional<Estacion> optionalEstacion = Optional.of(estacion);

        when(estacionRepository.findById(50)).thenReturn(optionalEstacion);

        Estacion estacionActualizada = new Estacion();
        estacionActualizada.setId(50);
        estacionActualizada.setNombre("Malagueno");
        estacionActualizada.setLatitud(-31.452961123175);
        estacionActualizada.setLongitud(-64.2540911211195);

        estacionServiceImpl.update(50, estacionActualizada);

        Estacion resultado = estacionServiceImpl.getById(50);

        assertEquals(resultado.getNombre(), "Malagueno");
    }

    @Test
    public void testGetEstacionMasCercana() {
        Estacion estacion1 = new Estacion();
        estacion1.setId(50);
        estacion1.setNombre("Arguello");
        estacion1.setLatitud(-31.452961123175);
        estacion1.setLongitud(-64.2540911211195);

        Estacion estacion2 = new Estacion();
        estacion2.setId(51);
        estacion2.setNombre("Malagueno");
        estacion2.setLatitud(-31.482961123175);
        estacion2.setLongitud(-64.2840911211195);

        Estacion estacion3 = new Estacion();
        estacion3.setId(52);
        estacion3.setNombre("VillaAllende");
        estacion3.setLatitud(-31.522961123175);
        estacion3.setLongitud(-64.3240911211195);

        List<Estacion> estacionList = new ArrayList<>(Arrays.asList(estacion1, estacion2, estacion3));
        when(estacionRepository.findAll()).thenReturn(estacionList);

        double latitud = -31.482961120000;
        double longitud = -64.2840911210000;

        Estacion Estacionresultado = estacionServiceImpl.getEstacionMasCercana(latitud, longitud);

        assertEquals(estacion2, Estacionresultado);
    }
}
