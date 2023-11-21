package com.bda.trabajoPracticoIntegrador.Controller;

import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquiler;
import com.bda.trabajoPracticoIntegrador.Request.IniciarAlquilerRequest;
import com.bda.trabajoPracticoIntegrador.Service.Interface.AlquilerService;
import com.bda.trabajoPracticoIntegrador.Service.Interface.EstacionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
@Slf4j
public class AlquilerController {

    private AlquilerService service;
    private RestTemplate restTemplate;
    private EstacionService estacionService;

    @Autowired
    public AlquilerController(AlquilerService service, RestTemplate restTemplate, EstacionService estacionService) {
        this.service = service;
        this.restTemplate = restTemplate;
        this.estacionService = estacionService;
    }

    @GetMapping
    public ResponseEntity<List<Alquiler>> getAll() {
        List<Alquiler> values = service.getAll();
        return ResponseEntity.ok(values);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlquilerDto> getById(@PathVariable int id) {
        Alquiler alquiler = service.getById(id);
        if (alquiler != null) {
            int estacionRetiroId = alquiler.getEstacionRetiro();
            int estacionDevolucionId = alquiler.getEstacionDevolucion();

            EstacionDto estacionRetiroDto = estacionService.obtenerEstacionDto(estacionRetiroId);
            EstacionDto estacionDevolucionDto = estacionService.obtenerEstacionDto(estacionDevolucionId);

            AlquilerDto alquilerDto = new AlquilerDto();
            alquilerDto.setId(alquiler.getId());
            alquilerDto.setIdCliente(alquiler.getIdCliente());
            alquilerDto.setEstado(alquiler.getEstado());
            alquilerDto.setEstacionRetiro(estacionRetiroDto);
            alquilerDto.setEstacionDevolucion(estacionDevolucionDto);
            alquilerDto.setFechaHoraRetiro(LocalDateTime.parse(String.valueOf(alquiler.getFechaHoraRetiro())));
            alquilerDto.setFechaHoraDevolucion(LocalDateTime.parse(String.valueOf(alquiler.getFechaHoraDevolucion())));
            alquilerDto.setMonto(alquiler.getMonto());

            return ResponseEntity.ok(alquilerDto);

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<Alquiler> iniciarAlquiler(@RequestBody IniciarAlquilerRequest request) {
        Alquiler alquiler = service.iniciarAlquiler(request.getIdCliente(), request.getEstacionRetiroId());

        if (alquiler != null) {
            return ResponseEntity.ok(alquiler);
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Alquiler alquiler = service.getById(id);

        if (alquiler != null) {
            int estacionRetiroId = alquiler.getEstacionRetiro();
            int estacionDevolucionId = alquiler.getEstacionDevolucion();

            // Realiza la lógica para eliminar la relación con estaciones si es necesario

            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Alquiler> finalizarAlquiler(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "ARS") String moneda,
            @RequestBody AlquilerDto alquilerDto) {
        try {
            // Verificar que el ID en la URL coincida con el ID en el cuerpo del AlquilerDto
            if (id != alquilerDto.getId()) {
                log.error("El ID en la URL no coincide con el ID en el cuerpo del AlquilerDto.");
                return ResponseEntity.badRequest().build();
            }

            // Verificar que el alquiler con el ID proporcionado existe
            Alquiler alquiler = service.getById(id);
            if (alquiler != null) {
                Alquiler alquilerFinalizado = service.finalizarAlquiler(alquilerDto, moneda);
                log.info("Alquiler finalizado con éxito.");
                return ResponseEntity.ok(alquilerFinalizado);
            } else {
                log.error("No se encontró el alquiler con el ID proporcionado.");
                return ResponseEntity.notFound().build();
            }
        } catch (HttpClientErrorException e) {
            log.error("Error al llamar al servicio externo.", e);
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (NumberFormatException e) {
            log.error("Error al convertir el ID a entero o al validar el ID en el cuerpo del AlquilerDto.", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error inesperado.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

