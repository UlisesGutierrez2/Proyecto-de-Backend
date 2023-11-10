package com.bda.trabajoPracticoIntegrador.Controller;

import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;
import com.bda.trabajoPracticoIntegrador.Service.Interface.AlquileresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.bda.trabajoPracticoIntegrador.Dtos.AlquilerDto;

import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
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
        if(alquileres != null) {
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
            alquilerDto.setFechaHoraRetiro(alquileres.getFechaHoraRetiro());
            alquilerDto.setFechaHoraDevolucion(alquileres.getFechaHoraDevolucion());
            alquilerDto.setMonto(alquileres.getMonto());

            return ResponseEntity.ok(alquilerDto);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private EstacionDto getEstacionFromOtherService(int estacionId) {
        String url = "http://localhost:8084/api/estaciones/" + estacionId;
        return restTemplate.getForObject(url, EstacionDto.class);
    }

    @PostMapping
    public ResponseEntity<AlquilerDto> addAlquiler(@RequestBody AlquilerDto alquiler) {
        Alquileres alquileres = new Alquileres();

        int estacionRetiroId = alquiler.getEstacionRetiro().getId();
        int estacionDevolucionId = alquiler.getEstacionDevolucion().getId();
        EstacionDto estacionRetiroDto = getEstacionFromOtherService(estacionRetiroId);
        EstacionDto estacionDevolucionDto = getEstacionFromOtherService(estacionDevolucionId);

        if (estacionRetiroId == 0 || estacionDevolucionId == 0) {
            // Manejo de estaciones no válidas, por ejemplo, puedes devolver un error 400 (Bad Request)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        alquileres.setIdCliente(alquiler.getIdCliente());
        alquileres.setEstado(alquiler.getEstado());

        alquiler.setEstacionRetiro(estacionRetiroDto);
        alquiler.setEstacionDevolucion(estacionDevolucionDto);

        Alquileres nuevoAlquiler = service.add(alquileres);

        return ResponseEntity.ok(alquiler);
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
}
