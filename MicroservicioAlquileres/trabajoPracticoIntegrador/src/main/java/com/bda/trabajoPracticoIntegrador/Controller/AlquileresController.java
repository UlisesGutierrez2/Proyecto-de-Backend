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

    @GetMapping("/{id}")
    public ResponseEntity<Alquileres> getById(@PathVariable int id) {
        Alquileres alquileres = service.getById(id);
        if(alquileres != null) {
            return ResponseEntity.ok(alquileres);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Alquileres addAlquiler(@RequestBody Alquileres alquiler) {
        

        return service.add(alquiler);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
