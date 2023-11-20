package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;
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
    private TarifaRepository tarifasRepository;
    private RestTemplate restTemplate;
    private EstacionService estacionService;
    private ExchangeService exchangeService;

    public AlquilerServiceImpl(AlquilerRepository repository, RestTemplate restTemplate, TarifaRepository tarifasRepository, EstacionService estacionService, ExchangeService exchangeService) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.tarifasRepository = tarifasRepository;
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
        Optional<Alquiler> optionalAlquileres = repository.findById(id);
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
        return tarifasRepository.findAll();
    }

    public double calcularMontoTotal(Alquiler alquiler, double distanciaEnKm) {
        List<Tarifa> tarifaList = obtenerTarifas();

        double costoMinutosFraccionados = 0;



    // Obtener el día de la semana de la fecha de retiro
        int diaSemana = alquiler.getFechaHoraRetiro().getDayOfWeek().getValue();
        int diaMes = alquiler.getFechaHoraRetiro().getDayOfMonth();
        int mes = alquiler.getFechaHoraRetiro().getMonth().getValue();
        int anio = alquiler.getFechaHoraRetiro().getYear();

        Optional<Tarifa> tarifaOpt = tarifasRepository.findByParams(diaMes, mes, anio);

        double montoFijoAlquiler;

        // Otras operaciones con montoFijoAlquiler...

        if (tarifaOpt.isPresent()) {

            Tarifa tarifa = tarifaOpt.get();

            montoFijoAlquiler = tarifa.getMontoFijoAlquiler();

            // Calcular la duración del alquiler en minutos
            long duracionEnMinutos = calcularDuracionEnMinutos(alquiler.getFechaHoraRetiro(), alquiler.getFechaHoraDevolucion());

            int cantHoras = (int) (duracionEnMinutos / 60);
            
            int excedenteMinutos = (int) (duracionEnMinutos % 60);
            
            if (excedenteMinutos <= 30) {

                costoMinutosFraccionados = Math.min(duracionEnMinutos, 30) * tarifa.getMontoMinutoFraccion();

            } else {
                cantHoras += 1;
            }

            // Calcular el costo por hora completa a partir del minuto 31
            double costoHorasCompletas = Math.ceil(cantHoras * tarifa.getMontoHora());

            // Calcular el costo total sumando todas las partes
            double costoTotal = montoFijoAlquiler + costoMinutosFraccionados + costoHorasCompletas;


            return costoTotal;

        } else {
            // No hay tarifa específica para el día, mes y año
            // Se puede aplicar la lógica basada en el día de la semana
            Optional<Tarifa> tarifaDiaOpt = tarifasRepository.findByDay(diaSemana);

            if (tarifaDiaOpt.isPresent()) {
                // Existe una tarifa para el día de la semana
                Tarifa tarifaDia = tarifaDiaOpt.get();
                montoFijoAlquiler = tarifaDia.getMontoFijoAlquiler();

                // Calcular la duración del alquiler en minutos
                long duracionEnMinutos = calcularDuracionEnMinutos(alquiler.getFechaHoraRetiro(), alquiler.getFechaHoraDevolucion());

                int cantHoras = (int) (duracionEnMinutos / 60);
                int excedenteMinutos = (int) (duracionEnMinutos % 60);

                if (excedenteMinutos <= 30) {
                    costoMinutosFraccionados = Math.min(duracionEnMinutos, 30) * tarifaDia.getMontoMinutoFraccion();
                } else {
                    cantHoras += 1;
                }

                // Calcular el costo por hora completa a partir del minuto 31
                double costoHorasCompletas = Math.ceil(cantHoras * tarifaDia.getMontoHora());

                // Calcular el costo total sumando todas las partes
                double costoTotal = montoFijoAlquiler + costoMinutosFraccionados + costoHorasCompletas;

                return costoTotal;
            } else {
                // Manejar el caso en que no haya tarifa para el día de la semana
                throw new NoSuchElementException("No hay tarifa definida para el día de la semana: " + diaSemana);
            }
        }
    }


    private long calcularDuracionEnMinutos(LocalDateTime fechaHoraRetiro, LocalDateTime fechaHoraDevolucion) {

        Duration duration = Duration.between(fechaHoraRetiro, fechaHoraDevolucion);

        // Devuelve la diferencia en minutos
        return duration.toMinutes();
    }

    public Alquiler finalizarAlquiler(AlquilerDto alquilerDto, String moneda) {
        log.info("Iniciando finalizarAlquiler para el alquiler con ID() ", alquilerDto.getId());
        // Obtener la información del alquiler
        Alquiler alquiler = getById(alquilerDto.getId());
        log.info("Alquiler: {}", alquiler);
        if (alquiler != null && alquiler.getFechaHoraDevolucion() == null) {

            // Establecer la fecha y hora de devolución
            alquiler.setFechaHoraDevolucion(LocalDateTime.now());

            // Calcular la distancia en kilómetros entre las estaciones de retiro y devolución
            double distanciaEnKm = estacionService.calcularDistancia(alquilerDto.getEstacionRetiro().getLatitud(), alquilerDto.getEstacionRetiro().getLongitud(), alquilerDto.getEstacionDevolucion().getLatitud(), alquilerDto.getEstacionDevolucion().getLongitud());
            log.info("Distancia entre las estaciones: {} km", distanciaEnKm);

            // Calcular el monto total del alquiler
            double montoTotal = calcularMontoTotal(alquiler, distanciaEnKm);
            log.info("Monto total del alquiler: {}", montoTotal);

            String cotizacionApiUrl = "http://34.82.105.125:8080/convertir";

            // Obtener la cotización
            double montoConvertido = exchangeService.obtenerCotizacion(cotizacionApiUrl, moneda, montoTotal);

            // Calcular el monto final
            double montoFinal = montoConvertido * montoTotal;

            alquiler.finalizar(montoFinal);

            // Guardar la instancia actualizada en la base de datos
            return update(alquiler.getId(), alquiler);

        } else {
            log.error("Alquiler no encontrado o ya devuelto");
            throw new NoSuchElementException("Alquiler no encontrado o ya devuelto");
        }
    }
}
