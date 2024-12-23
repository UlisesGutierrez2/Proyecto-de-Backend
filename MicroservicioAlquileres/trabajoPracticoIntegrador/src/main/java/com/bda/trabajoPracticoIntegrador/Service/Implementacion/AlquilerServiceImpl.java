package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;
import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquiler;
import com.bda.trabajoPracticoIntegrador.Entity.Tarifa;
import com.bda.trabajoPracticoIntegrador.Repository.AlquilerRepository;
import com.bda.trabajoPracticoIntegrador.Repository.TarifaRepository;
import com.bda.trabajoPracticoIntegrador.Service.Interface.AlquilerService;
import com.bda.trabajoPracticoIntegrador.Service.Interface.EstacionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class AlquilerServiceImpl implements AlquilerService {

    private AlquilerRepository repository;
    private TarifaServiceImpl tarifaService;
    private EstacionService estacionService;
    private ExchangeService exchangeService;

    public AlquilerServiceImpl(AlquilerRepository repository, TarifaServiceImpl tarifaService, EstacionService estacionService, ExchangeService exchangeService) {
        this.repository = repository;
        this.tarifaService = tarifaService;
        this.estacionService = estacionService;
        this.exchangeService = exchangeService;
    }


    @Override
    public Alquiler update(int id, Alquiler alquiler) {
        Optional<Alquiler> optionalAlquileres = repository.findById(id);

        if (optionalAlquileres.isPresent()) {
            Alquiler alquilerEncontrado = optionalAlquileres.get();

            alquilerEncontrado.setMonto(alquiler.getMonto());

            return repository.save(alquilerEncontrado);

        } else {
            throw new NoSuchElementException("Alquiler no encontrado");
        }
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public Alquiler getById(int id) {
        System.out.println("a");
        Optional<Alquiler> optionalAlquileres = repository.findById(id);
        System.out.println(optionalAlquileres);
        return optionalAlquileres.orElse(null);
    }

    public Alquiler iniciarAlquiler(String idCliente, int estacionRetiroId) {
        // Verificar si el cliente está registrado en el sistemaelado y la estación tenga una bicicleta disponible)

        // Realizar las validaciones necesarias antes de iniciar  alquiler
        // (por ejemplo, asegurarse de que el cliente esté registr
        // Obtener la información de la estación de retiro

        // Crear una instancia de Alquileres y asignar los valores necesarios
        Alquiler nuevoAlquiler = new Alquiler();
        nuevoAlquiler.iniciar(idCliente, estacionRetiroId);

        // Guardar la instancia inc evoAlquiler);
        return repository.save(nuevoAlquiler);
    }

    @Override
    public List<Alquiler> getAll() {
        return repository.findAll();
    }

    public List<Tarifa> obtenerTarifas() {
        return tarifaService.obtenerTarifas();
    }

    public Alquiler finalizarAlquiler(int id, String moneda, int estacionDevolucionId) {
        log.info("Iniciando finalizarAlquiler para el alquiler con ID() ", id);
        // Obtener la información del alquiler
        Alquiler alquiler = getById(id);

        EstacionDto estacionRetiro = estacionService.obtenerEstacionDto(alquiler.getEstacionRetiro());
        EstacionDto estacionDevolucion = estacionService.obtenerEstacionDto(estacionDevolucionId);

        log.info("Alquiler: {}", alquiler);
        if (alquiler != null && alquiler.getFechaHoraDevolucion() == null) {
             alquiler.setFechaHoraDevolucion(LocalDateTime.now());
            // Calcular la distancia en kilómetros entre las estaciones de retiro y devolución
            double distanciaEnKm = estacionService.calcularDistancia(estacionRetiro.getLatitud(), estacionRetiro.getLongitud(), estacionDevolucion.getLatitud(), estacionDevolucion.getLongitud());
            log.info("Distancia entre las estaciones: {} km", distanciaEnKm);

            // Obtener el día de la semana de la fecha de retiro
            int diaSemana = alquiler.getFechaHoraRetiro().getDayOfWeek().getValue();
            int diaMes = alquiler.getFechaHoraRetiro().getDayOfMonth();
            int mes = alquiler.getFechaHoraRetiro().getMonth().getValue();
            int anio = alquiler.getFechaHoraRetiro().getYear();

            Tarifa tarifa = tarifaService.findByParams(diaMes, mes, anio);
            if (tarifa == null) {
                tarifa = tarifaService.findByDay(diaSemana);
            }

            // Calcular el monto total del alquiler
            double montoTotal = alquiler.calcularMontoTotal(tarifa, distanciaEnKm);
            log.info("Monto total del alquiler: {}", montoTotal);

            String cotizacionApiUrl = "http://34.82.105.125:8080/convertir";

            // Obtener la cotización
            double montoConvertido = exchangeService.obtenerCotizacion(cotizacionApiUrl, moneda, montoTotal);

            alquiler.finalizar(montoTotal, estacionDevolucionId,tarifa);

            update(alquiler.getId(), alquiler);

            alquiler.setMonto(montoConvertido);

            // Guardar la instancia actualizada en la base de datos
            return alquiler;

        } else {
            log.error("Alquiler no encontrado o ya devuelto");
            throw new NoSuchElementException("Alquiler no encontrado o ya devuelto");
        }
    }
}
