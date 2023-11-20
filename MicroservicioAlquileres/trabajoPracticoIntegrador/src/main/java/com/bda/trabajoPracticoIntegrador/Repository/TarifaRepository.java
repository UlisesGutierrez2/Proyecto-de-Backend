package com.bda.trabajoPracticoIntegrador.Repository;

import com.bda.trabajoPracticoIntegrador.Entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {
}
