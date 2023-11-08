package com.bda.trabajoPracticoIntegrador.Controller;

import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;
import com.bda.trabajoPracticoIntegrador.Service.Interface.AlquileresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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

    /*
    public void crearAlquileres(Alquileres alquileres) {
        // Creación de una instancia de RestTemplate

        try {
            // Creación de la instancia de RequestTemplate
            RestTemplate template = new RestTemplate();
            // Creación de la entidad a enviar
            HttpEntity<Alquileres> entity = new HttpEntity<>(alquileres);
            // respuesta de la petición tendrá en su cuerpo a un objeto del tipo Persona.
                    ResponseEntity<Alquileres> res = template.postForEntity(
                    "http://localhost:8084/api/estaciones", entity, Alquileres.class
            );
            // Se comprueba si el código de repuesta es de la familia 200
            if (res.getStatusCode().is2xxSuccessful()) {
                log.debug("Persona creada correctamente: {}", res.getBody());
            } else {
                log.warn("Respuesta no exitosa: {}", res.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            // La repuesta no es exitosa.
            log.error("Error en la petición", ex);
        }
    }

    @PostMapping
    public ResponseEntity<Alquileres> add(@RequestBody AlquilerDto alquileresDto, Alquileres alquileres) {

        Alquileres nuevo = new Alquileres();

        nuevo.setEstacionDevolucion(alquileres.getEstacionDevolucion());
        nuevo.setEstacionRetiro(alquileres.getEstacionRetiro());

        nuevo.setMonto(alquileres.getMonto());
        nuevo.setEstado(alquileres.getEstado());
        nuevo.setIdCliente(alquileres.getIdCliente());
        nuevo.setIdTarifa(alquileres.getIdTarifa());

        return ResponseEntity.ok(service.add(nuevo));

    }

     */

    @PostMapping
    public ResponseEntity<Alquileres> add(@RequestBody AlquilerDto alquileresDto, Alquileres alquileres) {
        RestTemplate restTemplate = new RestTemplate();

        // URL del servicio para obtener un recurso
        String estacionRetiroUrl = "http://localhost:8084/api/estaciones/{id}";

        int recursoId = alquileres.getId(); // Ajusta esto según tu DTO
        ResponseEntity<String> response = restTemplate.getForEntity(estacionRetiroUrl, String.class, recursoId);

        if (response.getStatusCode().is2xxSuccessful()) {
            // La solicitud fue exitosa
            String responseBody = response.getBody();
            // Haz lo que necesites con el cuerpo de la respuesta
        } else {
            // La solicitud no fue exitosa, maneja el error según tus requerimientos
        }

        // Continúa con el resto de la lógica de tu método
        Alquileres nuevo = new Alquileres();
        // ...
        nuevo.setEstacionDevolucion(alquileres.getEstacionDevolucion());
        nuevo.setEstacionRetiro(alquileres.getEstacionRetiro());

        nuevo.setMonto(alquileres.getMonto());
        nuevo.setEstado(1);
        nuevo.setIdCliente("123");
        nuevo.setIdTarifa(alquileres.getIdTarifa());

        return ResponseEntity.ok(service.add(nuevo));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
