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

    /*
    @GetMapping
    public ResponseEntity<List<Estacion>> getAll() {
        List<Estacion> values = service.getAll();
        return ResponseEntity.ok(values);
    }

     */

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

    @GetMapping
    public ResponseEntity<?> getByUbicacionOrAll(@RequestParam(value = "latitud", required = false) Double latitud,
                                                 @RequestParam(value = "longitud", required = false) Double longitud) {
        if (latitud != null && longitud != null) {
            // Si se proporcionan latitud y longitud, llamar a getByUbicacion
            Estacion estacion = service.getEstacionMasCercana(latitud, longitud);
            if (estacion == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(estacion);
        } else {
            // Si no se proporcionan latitud y longitud, llamar a getAll
            List<Estacion> values = service.getAll();
            return ResponseEntity.ok(values);
        }
    }
}
