package com.bda.trabajoPracticoIntegrador.Repository;

import com.bda.trabajoPracticoIntegrador.Dtos.EstacionDto;
import com.bda.trabajoPracticoIntegrador.Entity.Alquileres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlquileresRepository extends JpaRepository<Alquileres, Integer> {
}
