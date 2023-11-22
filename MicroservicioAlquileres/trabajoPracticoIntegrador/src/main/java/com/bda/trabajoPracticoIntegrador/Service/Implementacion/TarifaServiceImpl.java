package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import com.bda.trabajoPracticoIntegrador.Entity.Tarifa;
import com.bda.trabajoPracticoIntegrador.Repository.TarifaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TarifaServiceImpl {

    private TarifaRepository tarifaRepository;

    public TarifaServiceImpl(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public List<Tarifa> obtenerTarifas() {
        return tarifaRepository.findAll();
    }

    public Tarifa findByParams(int diaMes, int mes, int anio) {
        return tarifaRepository.findByParams(diaMes, mes, anio).orElse(null);
    }

    public Tarifa findByDay(int diaSemana) {
        return tarifaRepository.findByDay(diaSemana).orElseThrow();
    }

}
