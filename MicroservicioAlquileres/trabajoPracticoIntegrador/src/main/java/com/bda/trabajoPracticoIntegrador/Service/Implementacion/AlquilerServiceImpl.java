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
    private TarifaRepository tarifasRepository;
    private RestTemplate restTemplate;
    private EstacionService estacionService;

    public AlquilerServiceImpl(AlquilerRepository repository, RestTemplate restTemplate, TarifaRepository tarifasRepository, EstacionService estacionService) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.tarifasRepository = tarifasRepository;
        this.estacionService = estacionService;
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
                System.out.println(cantHoras);

                // Calcular el costo total sumando todas las partes
                System.out.println(montoFijoAlquiler);
                System.out.println(costoMinutosFraccionados);
                System.out.println(costoHorasCompletas);
                double costoTotal = montoFijoAlquiler + costoMinutosFraccionados + costoHorasCompletas;


                return costoTotal;
            } else {
                // Manejar el caso en que no haya tarifa para el día de la semana
                throw new NoSuchElementException("No hay tarifa definida para el día de la semana: " + diaSemana);
            }
        }
    }


    private long calcularDuracionEnMinutos(LocalDateTime fechaHoraRetiro, LocalDateTime fechaHoraDevolucion) {

        Duration duration = Duration.between(fechaHoraDevolucion, fechaHoraRetiro);

        System.out.println(duration);

        // Devuelve la diferencia en minutos
        return duration.toMinutes();
    }

    public Alquiler finalizarAlquiler(int id, String moneda) {
        log.info("Iniciando finalizarAlquiler para el alquiler con ID() ");
        // Obtener la información del alquiler
        Alquiler alquiler = getById(id);

        EstacionDto estacionRetiro = estacionService.obtenerEstacionDto(alquiler.getEstacionRetiro());
        EstacionDto estacionDevolucion = estacionService.obtenerEstacionDto(alquiler.getEstacionDevolucion());
        log.info("Alquiler: {}", alquiler);
        if (alquiler != null && alquiler.getFechaHoraDevolucion() == null) {

            alquiler.setFechaHoraDevolucion(LocalDateTime.now());

            // Calcular la distancia en kilómetros entre las estaciones de retiro y devolución
            double distanciaEnKm = estacionService.calcularDistancia(estacionRetiro.getLatitud(), estacionRetiro.getLongitud(),estacionDevolucion.getLatitud(), estacionDevolucion.getLongitud());
            log.info("Distancia entre las estaciones: {} km", distanciaEnKm);


            // Calcular el monto total del alquiler
            double montoTotal = calcularMontoTotal(alquiler, distanciaEnKm);
            log.info("Monto total del alquiler: {}", montoTotal);
            /*
            // Aplicar descuento si es un día promocional
            // esto esta en la funcion de monto
            /*
            if (esDiaPromocional()) {
                double porcentajeDescuento = obtenerDescuentoPorDiaPromocional(alquiler.getFechaHoraRetiro());
                montoTotal *= (1 - porcentajeDescuento);
                log.info("Descuento aplicado: {}%", porcentajeDescuento * 100);
            }


             */

            // Llamada al microservicio externo de cotizaciones
            String cotizacionApiUrl = "http://34.82.105.125:8080/convertir";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Armar la solicitud
            String requestBody = "{\"moneda_destino\":\"" + "USD" + "\",\"importe\":" + montoTotal + "}";

            // Configurar la solicitud HTTP
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            try {
                // Log para registrar la solicitud al microservicio de cotizaciones
                log.info("Realizando solicitud al microservicio de cotizaciones. URL: {}", cotizacionApiUrl);

                // Realizar la solicitud POST
                ResponseEntity<String> response = restTemplate.exchange(cotizacionApiUrl, HttpMethod.POST, entity, String.class);

                // Log para registrar la respuesta del microservicio
                log.info("Respuesta del microservicio: {}", response);

                log.info(response.getStatusCode().toString());

                // Manejar la respuesta del microservicio de cotizaciones según sea necesario
                if (response.getStatusCode().is2xxSuccessful()) {
                    // Extraer y manejar la respuesta del microservicio
                    String cotizacionResponse = response.getBody();

                    // Log para registrar la respuesta detallada del microservicio
                    log.info("Respuesta detallada del microservicio: {}", cotizacionResponse);

                    // Parsear la respuesta del microservicio para obtener el monto convertido
                    double montoConvertido = parsearRespuestaDelMicroservicio(cotizacionResponse, moneda);

                    // el api ya hace este calculo
                    // double montoFinal = montoConvertido * montoTotal;

                    // Actualizar el monto en el objeto de alquiler
                    alquiler.setMonto(montoConvertido);


                    // Establece estado a finalizado
                    alquiler.setEstado(2);

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