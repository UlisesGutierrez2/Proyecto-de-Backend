package com.bda.trabajoPracticoIntegrador.Repository;

import com.bda.trabajoPracticoIntegrador.Entity.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Integer> {
}
