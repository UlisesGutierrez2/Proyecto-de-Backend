package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;
import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;
import com.bda.trabajoPracticoIntegrador.Entity.Tarifas;
import com.bda.trabajoPracticoIntegrador.Repository.AlquileresRepository;
import com.bda.trabajoPracticoIntegrador.Repository.TarifaRepository;
import com.bda.trabajoPracticoIntegrador.Service.Interface.AlquileresService;
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
public class AlquileresImpl implements AlquileresService {

    private AlquileresRepository repository;
    private TarifaRepository tarifasRepository;
    private RestTemplate restTemplate;
    private EstacionService estacionService;

    public AlquileresImpl(AlquileresRepository repository, RestTemplate restTemplate, TarifaRepository tarifasRepository, EstacionService estacionService) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.tarifasRepository = tarifasRepository;
        this.estacionService = estacionService;
    }


    @Override
    public Alquileres update(int id, Alquileres alquileres) {
        Optional<Alquileres> optionalAlquileres = repository.findById(id);

        if (optionalAlquileres.isPresent()) {
            Alquileres alquilerEncontrado = optionalAlquileres.get();

            alquilerEncontrado.setMonto(alquileres.getMonto());

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
    public Alquileres getById(int id) {
        Optional<Alquileres> optionalAlquileres = repository.findById(id);
        return optionalAlquileres.orElse(null);
    }

    public Alquileres iniciarAlquiler(String idCliente, int estacionRetiroId) {
        // Verificar si el cliente está registrado en el sistemaelado y la estación tenga una bicicleta disponible)

        // Realizar las validaciones necesarias antes de iniciar  alquiler
        // (por ejemplo, asegurarse de que el cliente esté registr
        // Obtener la información de la estación de retiro

        // Crear una instancia de Alquileres y asignar los valores necesarios
        Alquileres nuevoAlquiler = new Alquileres();
        nuevoAlquiler.iniciar(idCliente, estacionRetiroId);

        // Guardar la instancia inc evoAlquiler);
        return repository.save(nuevoAlquiler);
    }

    @Override
    public List<Alquileres> getAll() {
        return repository.findAll();
    }

    public List<Tarifas> obtenerTarifas() {
        return tarifasRepository.findAll();
    }

    public double calcularMontoTotal(Alquileres alquiler, double distanciaEnKm) {
        List<Tarifas> tarifasList = obtenerTarifas();

    // Obtener el día de la semana de la fecha de retiro
        int diaSemana = alquiler.getFechaHoraRetiro().getDayOfWeek().getValue();
        DayOfWeek dayOfWeek = DayOfWeek.of(diaSemana);

    // Encontrar la tarifa correspondiente al día de la semana
        Optional<Tarifas> tarifaOpt = tarifasList.stream()
                .filter(tarifa -> tarifa.getDiaSemana() == diaSemana)
                .findFirst();

        if (tarifaOpt.isPresent()) {
            Tarifas tarifa = tarifaOpt.get();

            // Calcular la duración del alquiler en minutos
            long duracionEnMinutos = calcularDuracionEnMinutos(alquiler.getFechaHoraRetiro(), alquiler.getFechaHoraDevolucion());

            int cantHoras = (int) (duracionEnMinutos / 60);
            
            int excedenteMinutos = (int) (duracionEnMinutos % 60);
            
            if (excedenteMinutos <= 30) {

                double costoMinutosFraccionados = Math.min(duracionEnMinutos, 30) * tarifa.getMontoMinutoFraccion();

            } else {
                cantHoras += 1;
            }

            // Calcular el costo por hora completa a partir del minuto 31
            double costoHorasCompletas = Math.ceil(cantHoras * tarifa.getMontoHora());

            // Calcular el costo total sumando todas las partes
            double costoTotal = tarifa.getMontoFijoAlquiler() + costoMinutosFraccionados + costoHorasCompletas;

            // Aplicar descuento si es un día promocional
            if (esDiaPromocional()) {
                double porcentajeDescuento = obtenerDescuentoPorDiaPromocional(alquiler.getFechaHoraRetiro());
                costoTotal *= (1 - porcentajeDescuento);
            }

            return costoTotal;
        } else {
            // Manejar el caso en el que no se encuentre la tarifa para el día de la semana
            throw new NoSuchElementException("Tarifa no encontrada para el día de la semana: " + dayOfWeek);
        }
    }

    private double obtenerDescuentoPorDiaPromocional(LocalDateTime fechaHoraRetiro) {
        return 0.5;
    }

    private boolean esDiaPromocional() {
        return true;
    }

    private long calcularDuracionEnMinutos(LocalDateTime fechaHoraRetiro, LocalDateTime fechaHoraDevolucion) {

        Duration duration = Duration.between(fechaHoraRetiro, fechaHoraDevolucion);

        // Devuelve la diferencia en minutos
        return duration.toMinutes();
    }

    public Alquileres finalizarAlquiler(AlquilerDto alquilerDto, String moneda) {
        log.info("Iniciando finalizarAlquiler para el alquiler con ID() ", alquilerDto.getId());
        // Obtener la información del alquiler
        Alquileres alquiler = getById(alquilerDto.getId());
        log.info("Alquiler: {}", alquiler);
        if (alquiler != null && alquiler.getFechaHoraDevolucion() == null) {
 
            // Establecer la fecha y hora de devolución
            alquiler.setFechaHoraDevolucion(LocalDateTime.now());

            // Calcular la distancia en kilómetros entre las estaciones de retiro y devolución
            double distanciaEnKm = estacionService.calcularDistancia(alquilerDto.getEstacionRetiro().getLatitud(), alquilerDto.getEstacionRetiro().getLongitud(),alquilerDto.getEstacionDevolucion().getLatitud(), alquilerDto.getEstacionDevolucion().getLongitud());
            log.info("Distancia entre las estaciones: {} km", distanciaEnKm);


            // Calcular el monto total del alquiler
            double montoTotal = calcularMontoTotal(alquiler, distanciaEnKm);
            log.info("Monto total del alquiler: {}", montoTotal);

            // Aplicar descuento si es un día promocional
            if (esDiaPromocional()) {
                double porcentajeDescuento = obtenerDescuentoPorDiaPromocional(alquiler.getFechaHoraRetiro());
                montoTotal *= (1 - porcentajeDescuento);
                log.info("Descuento aplicado: {}%", porcentajeDescuento * 100);
            }

            // Llamada al microservicio externo de cotizaciones
            String cotizacionApiUrl = "http://34.82.105.125:8080/convertir";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Armar la solicitud
            String requestBody = "{\"moneda_destino\":\"" + moneda + "\",\"importe\":" + montoTotal + "}";

            // Configurar la solicitud HTTP
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            try {
                // Log para registrar la solicitud al microservicio de cotizaciones
                log.info("Realizando solicitud al microservicio de cotizaciones. URL: {}", cotizacionApiUrl);

                // Realizar la solicitud POST
                ResponseEntity<String> response = restTemplate.exchange(cotizacionApiUrl, HttpMethod.POST, entity, String.class);

                // Log para registrar la respuesta del microservicio
                log.info("Respuesta del microservicio: {}", response);

                // Manejar la respuesta del microservicio de cotizaciones según sea necesario
                if (response.getStatusCode().is2xxSuccessful()) {
                    // Extraer y manejar la respuesta del microservicio
                    String cotizacionResponse = response.getBody();

                    // Log para registrar la respuesta detallada del microservicio
                    log.debug("Respuesta detallada del microservicio: {}", cotizacionResponse);

                    // Parsear la respuesta del microservicio para obtener el monto convertido
                    double montoConvertido = parsearRespuestaDelMicroservicio(cotizacionResponse, moneda);

                    double montoFinal = montoConvertido * montoTotal;

                    // Actualizar el monto en el objeto de alquiler
                    alquiler.setMonto(montoFinal);

                    // Establecer la fecha y hora de devolución
                    alquiler.setFechaHoraDevolucion(LocalDateTime.now());

                    // Guardar la instancia actualizada en la base de datos
                    return update(alquiler.getId(), alquiler);
                } else {
                    // Log para registrar el fallo de la solicitud al microservicio de cotizaciones
                    log.error("La solicitud al microservicio de cotizaciones falló. Código de estado: {}", response.getStatusCode());

                    // Manejar el caso en que la solicitud al microservicio de cotizaciones falla
                    throw new HttpClientErrorException(response.getStatusCode());
                }
            } catch (Exception e) {
                // Log para registrar cualquier excepción ocurrida durante la ejecución del bloque try
                log.error("Error al realizar la solicitud al microservicio de cotizaciones", e);

                // Puedes manejar la excepción según tus necesidades
                throw e;
            }
        } else {
            log.error("Alquiler no encontrado o ya devuelto");
            throw new NoSuchElementException("Alquiler no encontrado o ya devuelto");
        }
    }


    private double parsearRespuestaDelMicroservicio(String cotizacionResponse, String monedaEsperada) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(cotizacionResponse);

            // Verifica si el JSON tiene las propiedades necesarias
            if (jsonNode.has("moneda") && jsonNode.has("importe")) {
                String moneda = jsonNode.get("moneda").asText();
                double importe = jsonNode.get("importe").asDouble();

                // Verifica si la moneda es la esperada
                if (monedaEsperada.equals(moneda)) {
                    return importe;
                } else {
                    throw new IllegalArgumentException("La respuesta del microservicio no es en " + monedaEsperada + ": " + cotizacionResponse);
                }
            } else {
                throw new IllegalArgumentException("La respuesta del microservicio no tiene las propiedades esperadas: " + cotizacionResponse);
            }
        } catch (Exception e) {
            // Maneja cualquier excepción durante el análisis
            throw new IllegalArgumentException("Error al analizar la respuesta del microservicio", e);
        }
    }

}

