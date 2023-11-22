package com.bda.trabajoPracticoIntegrador;

import com.bda.trabajoPracticoIntegrador.Entity.Alquiler;
import com.bda.trabajoPracticoIntegrador.Entity.Tarifa;
import com.bda.trabajoPracticoIntegrador.Repository.AlquilerRepository;
import com.bda.trabajoPracticoIntegrador.Repository.TarifaRepository;
import com.bda.trabajoPracticoIntegrador.Service.Implementacion.AlquilerServiceImpl;
import com.bda.trabajoPracticoIntegrador.Service.Implementacion.ExchangeService;
import com.bda.trabajoPracticoIntegrador.Service.Implementacion.TarifaServiceImpl;
import com.bda.trabajoPracticoIntegrador.Service.Interface.EstacionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TrabajoPracticoIntegradorApplicationTests {
    @InjectMocks
    private AlquilerServiceImpl alquilerServiceImpl;

    @Mock
    private TarifaRepository tarifaRepository;

    @InjectMocks
    private TarifaServiceImpl tarifaService;

    @Mock
    private AlquilerRepository alquilerRepository;


    @Test
    public void testCalcularMontoTotalDescuento() {
        // Configuración de datos de prueba
        LocalDateTime fechaHoraRetiro = LocalDateTime.of(2023, 10, 13, 13, 12);
        LocalDateTime fechaHoraDevolucion = fechaHoraRetiro.plusMinutes(155);

        Alquiler alquiler = new Alquiler();
        alquiler.setFechaHoraDevolucion(fechaHoraDevolucion);
        alquiler.setFechaHoraRetiro(fechaHoraRetiro);

        double distanciaEnKm = 50.0; // Distancia en kilómetros para la prueba

        // Configuración del repositorio de tarifas mock
        Tarifa tarifa = new Tarifa();

        tarifa.setId(8);
        tarifa.setTipoTarifa(2);
        tarifa.setDefinicion('C');
        tarifa.setDiaSemana(null);
        tarifa.setDiaMes(13);
        tarifa.setMes(10);
        tarifa.setAnio(23);
        tarifa.setMontoFijoAlquiler(200);
        tarifa.setMontoMinutoFraccion(4);
        tarifa.setMontoKm(75);
        tarifa.setMontoHora(175);

        // Llamada al método a probar
        double resultado = alquiler.calcularMontoTotal(tarifa, distanciaEnKm);

        // Verificación de resultados esperados
        assertEquals(4475, resultado, 0.001);
    }

    @Test
    public void testCalcularMontoTotalComun() {
        // Configuración de datos de prueba
        LocalDateTime fechaHoraRetiro = LocalDateTime.of(2023, 10, 20, 13, 12);
        LocalDateTime fechaHoraDevolucion = fechaHoraRetiro.plusMinutes(155);

        Alquiler alquiler = new Alquiler();
        alquiler.setFechaHoraRetiro(fechaHoraRetiro);
        alquiler.setFechaHoraDevolucion(fechaHoraDevolucion);

        double distanciaEnKm = 50.0; // Distancia en kilómetros para la prueba

        // Configuración del repositorio de tarifas mock
        Tarifa tarifa = new Tarifa(/*Configura los valores de tarifa necesarios para la prueba*/);

        tarifa.setId(5); // ID de la tarifa 8 en tu base de datos
        tarifa.setTipoTarifa(1);
        tarifa.setDefinicion('S');
        tarifa.setDiaSemana(5);
        tarifa.setDiaMes(null);
        tarifa.setMes(null);
        tarifa.setAnio(null);
        tarifa.setMontoFijoAlquiler(320);
        tarifa.setMontoMinutoFraccion((int) 6.75);
        tarifa.setMontoKm(90);
        tarifa.setMontoHora(270);

        // Llamada al método a probar
        double resultado = alquiler.calcularMontoTotal(tarifa, distanciaEnKm);

        // Verificación de resultados esperados
        assertEquals(5630, resultado, 0.001);
    }


    @Test
    public void testGetById() {
        Alquiler alquiler = new Alquiler();
        alquiler.setId(10);
        alquiler.setIdCliente("1");
        alquiler.setEstado(1);

        when(alquilerRepository.findById(10)).thenReturn(Optional.of(alquiler));

        Alquiler resultado = alquilerServiceImpl.getById(10);

        assertEquals(alquiler.getId(), resultado.getId());
        assertEquals(alquiler.getIdCliente(), resultado.getIdCliente());
    }

    @Test
    public void testGetAll() {
        Alquiler alquiler1 = new Alquiler();
        alquiler1.setId(10);
        alquiler1.setIdCliente("1");
        alquiler1.setEstado(1);

        Alquiler alquiler2 = new Alquiler();
        alquiler2.setId(11);
        alquiler2.setIdCliente("2");
        alquiler2.setEstado(1);

        List<Alquiler> alquilerList = new ArrayList<>();
        alquilerList.add(alquiler1);
        alquilerList.add(alquiler2);

        when(alquilerRepository.findAll()).thenReturn(alquilerList);

        List<Alquiler> resultado = alquilerServiceImpl.getAll();

        assertEquals(alquilerList.size(), resultado.size());
    }

    @Test
    public void testDelete() {
        Alquiler alquiler = new Alquiler();
        alquiler.setId(10);
        alquiler.setIdCliente("1");
        alquiler.setEstado(1);

        doNothing().when(alquilerRepository).deleteById(alquiler.getId());

        when(alquilerRepository.findById(10)).thenReturn(Optional.of(alquiler));

        alquilerServiceImpl.delete(alquiler.getId());

        try {
            alquilerServiceImpl.getById(alquiler.getId());
        } catch (Exception e) {
            assertTrue(e instanceof ChangeSetPersister.NotFoundException);
        }
    }

    @Test
    public void testUpdate() {
        Alquiler alquiler = new Alquiler();
        alquiler.setId(10);
        alquiler.setIdCliente("1");
        alquiler.setEstado(1);

        Optional<Alquiler> optionalAlquiler = Optional.of(alquiler);

        when(alquilerRepository.findById(10)).thenReturn(optionalAlquiler);

        Alquiler alquilerActualizado = new Alquiler();
        alquiler.setId(10);
        alquiler.setIdCliente("2");
        alquiler.setEstado(1);

        alquilerServiceImpl.update(10, alquilerActualizado);

        Alquiler resultado = alquilerServiceImpl.getById(10);

        assertEquals(resultado.getIdCliente(), "2");
    }

    @Test
    public void testGetAllTarifas() {
        Tarifa tarifa1 = new Tarifa();
        tarifa1.setId(50);
        tarifa1.setAnio(2023);
        tarifa1.setDiaMes(9);
        tarifa1.setMes(9);

        Tarifa tarifa2 = new Tarifa();
        tarifa2.setId(50);
        tarifa2.setAnio(2023);
        tarifa2.setDiaMes(9);
        tarifa2.setMes(9);

        List<Tarifa> tarifaList = new ArrayList<>(Arrays.asList(tarifa1, tarifa2));

        when(tarifaRepository.findAll()).thenReturn(tarifaList);

        List<Tarifa> resultado = tarifaService.obtenerTarifas();

        assertEquals(tarifaList.size(), resultado.size());

    }


}


