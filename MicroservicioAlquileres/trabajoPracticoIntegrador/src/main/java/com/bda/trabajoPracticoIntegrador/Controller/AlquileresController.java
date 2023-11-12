package com.bda.trabajoPracticoIntegrador.Controller;

import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;
import com.bda.trabajoPracticoIntegrador.Service.Interface.AlquileresService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
@Slf4j
public class AlquileresController {

    private AlquileresService service;
    private RestTemplate restTemplate;

    @Autowired
    public AlquileresController(AlquileresService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Alquileres>> getAll() {
        List<Alquileres> values = service.getAll();
        return ResponseEntity.ok(values);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlquilerDto> getById(@PathVariable int id) {
        Alquileres alquileres = service.getById(id);
        if (alquileres != null) {
            int estacionRetiroId = alquileres.getEstacionRetiro();
            int estacionDevolucionId = alquileres.getEstacionDevolucion();

            EstacionDto estacionRetiroDto = getEstacionFromOtherService(estacionRetiroId);
            EstacionDto estacionDevolucionDto = getEstacionFromOtherService(estacionDevolucionId);

            AlquilerDto alquilerDto = new AlquilerDto();
            alquilerDto.setId(alquileres.getId());
            alquilerDto.setIdCliente(alquileres.getIdCliente());
            alquilerDto.setEstado(alquileres.getEstado());
            alquilerDto.setEstacionRetiro(estacionRetiroDto);
            alquilerDto.setEstacionDevolucion(estacionDevolucionDto);
            alquilerDto.setFechaHoraRetiro(LocalDateTime.parse(String.valueOf(alquileres.getFechaHoraRetiro())));
            alquilerDto.setFechaHoraDevolucion(LocalDateTime.parse(String.valueOf(alquileres.getFechaHoraDevolucion())));
            alquilerDto.setMonto(alquileres.getMonto());

            return ResponseEntity.ok(alquilerDto);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private EstacionDto getEstacionFromOtherService(int estacionId) {
        String url = "http://localhost:8082/api/estaciones/" + estacionId;
        return restTemplate.getForObject(url, EstacionDto.class);
    }
/*
    // Método para convertir un String de fecha a Timestamp
    private LocalDateTime parseFecha(String fecha) {
        // Intentar varios patrones de formato
        String[] patrones = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ssXXX"};

        for (String patron : patrones) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patron);
                log.trace("Fecha antes del análisis: " + fecha);
                return LocalDateTime.parse(fecha, formatter);
            } catch (DateTimeParseException e) {
                // Continuar con el próximo patrón si falla
            }
        }

        // Si no se pudo analizar con ninguno de los patrones, lanzar una excepción
        log.error("Error al parsear la fecha: " + fecha);
        throw new DateTimeParseException("No se pudo analizar la fecha: " + fecha, fecha, 0);
    }


 */


/*

    @PostMapping
    public ResponseEntity<Alquileres> addAlquiler(@RequestBody AlquilerDto alquiler) {
        int estacionRetiroId = alquiler.getEstacionRetiro().getId();
        int estacionDevolucionId = alquiler.getEstacionDevolucion().getId();

        if (estacionRetiroId == 0 || estacionDevolucionId == 0) {
            // Manejo de estaciones no válidas, por ejemplo, puedes devolver un error 400 (Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        EstacionDto estacionRetiroDto = getEstacionFromOtherService(estacionRetiroId);
        EstacionDto estacionDevolucionDto = getEstacionFromOtherService(estacionDevolucionId);

        if(estacionDevolucionDto == null || estacionRetiroDto == (null)) {
            return ResponseEntity.notFound().build();
        }

        // Crear una instancia de Alquileres y asignar los valores directamente
        Alquileres nuevoAlquiler = new Alquileres();
        nuevoAlquiler.setIdCliente(alquiler.getIdCliente());
        nuevoAlquiler.setEstado(alquiler.getEstado());
        nuevoAlquiler.setEstacionRetiro(estacionRetiroDto.getId());
        nuevoAlquiler.setEstacionDevolucion(estacionDevolucionDto.getId());
        nuevoAlquiler.setFechaHoraRetiro(parseFecha(String.valueOf(alquiler.getFechaHoraRetiro())));
        nuevoAlquiler.setFechaHoraDevolucion(parseFecha(String.valueOf(alquiler.getFechaHoraDevolucion())));  nuevoAlquiler.setMonto(alquiler.getMonto());

        nuevoAlquiler.setIdTarifa(alquiler.getIdTarifa());

        // Guardar la instancia en la base de datos
        Alquileres resultado = service.add(nuevoAlquiler);

        return ResponseEntity.ok(resultado);
    }

 */

    @PostMapping("/iniciar")
    public ResponseEntity<Alquileres> iniciarAlquiler(@RequestParam String idCliente, @RequestParam int estacionRetiroId) {
        Alquileres alquiler = service.iniciarAlquiler(idCliente, estacionRetiroId);

        if (alquiler != null) {
            return ResponseEntity.ok(alquiler);
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Alquileres alquileres = service.getById(id);

        if (alquileres != null) {
            int estacionRetiroId = alquileres.getEstacionRetiro();
            int estacionDevolucionId = alquileres.getEstacionDevolucion();

            // Realiza la lógica para eliminar la relación con estaciones si es necesario

            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/alquileres/finalizar")
    public ResponseEntity<Alquileres> finalizarAlquiler(@RequestParam int id) {
        // Obtener la información del alquiler
        Alquileres alquiler = service.getById(id);

        if (alquiler != null) {
            // Llamada al microservicio externo de cotizaciones
            String cotizacionApiUrl = "http://34.82.105.125:8080/convertir";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Armo Solicitud
            String requestBody = "{\"moneda_destino\":\"USD\",\"importe\":1000}";

            // Configurar la solicitud HTTP
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Realizar la solicitud POST
            ResponseEntity<String> response = restTemplate.exchange(cotizacionApiUrl, HttpMethod.POST, entity, String.class);

            // Manejar la respuesta del microservicio de cotizaciones según sea necesario
            if (response.getStatusCode().is2xxSuccessful()) {
                // Extraer y manejar la respuesta del microservicio
                String cotizacionResponse = response.getBody();

                // Parsear la respuesta del microservicio para obtener el monto
                // Falta tratar la conversión
                double montoConvertido = parsearRespuestaDelMicroservicio(cotizacionResponse);

                // Actualizar el monto en el objeto de alquiler
                alquiler.setMonto(montoConvertido);

                // Lo que falta para finalizar el alquiler

                // Guardar la instancia actualizada en la base de datos
                service.update(alquiler.getId(), alquiler);

                return ResponseEntity.ok(alquiler);
            } else {
                // Manejar el caso en el que la solicitud al microservicio de cotizaciones falla
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private double parsearRespuestaDelMicroservicio(String cotizacionResponse) {
        // Aca falta la lógica de conversión
        return 1.5;
    }

}
