package com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Controller;

import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Entity.Estacion;
import com.bda.trabajoPracticoIntegrador.Estaciones.trabajoPracticoIntegrador.Service.Interface.EstacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones")
public class EstacionController {

    private EstacionService service;

    public EstacionController(EstacionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Estacion>> getAll() {
        List<Estacion> values = service.getAll();
        return ResponseEntity.ok(values);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estacion> getById(@PathVariable int id) {
        Estacion estacion = service.getById(id);
        return ResponseEntity.ok(estacion);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Estacion> add(@RequestBody Estacion estacion) {
        service.add(estacion);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<Estacion> update(@PathVariable int id, @RequestBody Estacion estacion) {
        Estacion estacionActualizada = service.update(id, estacion);
        return ResponseEntity.ok(estacionActualizada);
    }

    @GetMapping("/ubicacion")
    public ResponseEntity<?> getByUbicacion(@RequestParam(value = "latitud") Double latitud,
                                            @RequestParam(value = "longitud") Double longitud) {
        if (latitud == null || longitud == null) {
            return ResponseEntity.badRequest().body("Las coordenadas son inválidas.");
        }

        Estacion estacion = service.getEstacionMasCercana(latitud, longitud);
        if (estacion == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(estacion);
    }
}
