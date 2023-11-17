package com.bda.trabajoPracticoIntegrador;

import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;
import com.bda.trabajoPracticoIntegrador.Entity.Tarifas;
import com.bda.trabajoPracticoIntegrador.Repository.AlquileresRepository;
import com.bda.trabajoPracticoIntegrador.Repository.TarifaRepository;
import com.bda.trabajoPracticoIntegrador.Service.Implementacion.AlquileresImpl;
import jakarta.validation.constraints.AssertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AliquileresServiceTests {

    @InjectMocks
    private AlquileresImpl alquileresImpl;

    @Mock
    private TarifaRepository tarifaRepository;

    @Mock
    private AlquileresRepository alquileresRepository;

    @Test
    public void testGetById() {
        Alquileres alquiler = new Alquileres();
        alquiler.setId(10);
        alquiler.setIdCliente("1");
        alquiler.setEstado(1);

        when(alquileresRepository.findById(10)).thenReturn(Optional.of(alquiler));

        Alquileres resultado = alquileresImpl.getById(10);

        assertEquals(alquiler.getId(), resultado.getId());
        assertEquals(alquiler.getIdCliente(), resultado.getIdCliente());
    }

    @Test
    public void testGetAll() {
        Alquileres alquiler1 = new Alquileres();
        alquiler1.setId(10);
        alquiler1.setIdCliente("1");
        alquiler1.setEstado(1);

        Alquileres alquiler2 = new Alquileres();
        alquiler2.setId(11);
        alquiler2.setIdCliente("2");
        alquiler2.setEstado(1);

        List<Alquileres> alquileresList = new ArrayList<>();
        alquileresList.add(alquiler1);
        alquileresList.add(alquiler2);

        when(alquileresRepository.findAll()).thenReturn(alquileresList);

        List<Alquileres> resultado = alquileresImpl.getAll();

        assertEquals(alquileresList.size(), resultado.size());
    }

    @Test
    public void testDelete() {
        Alquileres alquiler = new Alquileres();
        alquiler.setId(10);
        alquiler.setIdCliente("1");
        alquiler.setEstado(1);

        doNothing().when(alquileresRepository).deleteById(alquiler.getId());

        when(alquileresRepository.findById(10)).thenReturn(Optional.of(alquiler));

        alquileresImpl.delete(alquiler.getId());

        try {
            alquileresImpl.getById(alquiler.getId());
        } catch (Exception e) {
            assertTrue(e instanceof ChangeSetPersister.NotFoundException);
        }
    }

    @Test
    public void testUpdate() {
        Alquileres alquiler = new Alquileres();
        alquiler.setId(10);
        alquiler.setIdCliente("1");
        alquiler.setEstado(1);

        Optional<Alquileres> optionalAlquiler = Optional.of(alquiler);

        when(alquileresRepository.findById(10)).thenReturn(optionalAlquiler);

        Alquileres alquilerActualizado = new Alquileres();
        alquiler.setId(10);
        alquiler.setIdCliente("2");
        alquiler.setEstado(1);

        alquileresImpl.update(10, alquilerActualizado);

        Alquileres resultado = alquileresImpl.getById(10);

        assertEquals(resultado.getIdCliente(), "2");
    }

    @Test
    public void testGetAllTarifas() {
        Tarifas tarifa1 = new Tarifas();
        tarifa1.setId(50);
        tarifa1.setAnio(2023);
        tarifa1.setDiaMes(9);
        tarifa1.setMes(9);

        Tarifas tarifa2 = new Tarifas();
        tarifa2.setId(50);
        tarifa2.setAnio(2023);
        tarifa2.setDiaMes(9);
        tarifa2.setMes(9);

        List<Tarifas> tarifasList = new ArrayList<>(Arrays.asList(tarifa1, tarifa2));

        when(tarifaRepository.findAll()).thenReturn(tarifasList);

        List<Tarifas> resultado = alquileresImpl.obtenerTarifas();

        assertEquals(tarifasList.size(), resultado.size());

    }

}
