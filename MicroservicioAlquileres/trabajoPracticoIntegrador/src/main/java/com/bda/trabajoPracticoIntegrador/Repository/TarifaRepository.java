package com.bda.trabajoPracticoIntegrador.Repository;

import com.bda.trabajoPracticoIntegrador.Entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {

    @Query("SELECT t FROM Tarifa t WHERE t.diaMes = :param1 AND t.mes = :param2  AND t.anio = :param3")
    Optional<Tarifa> findByParams(@Param("param1") int diaMes, @Param("param2") int mes, @Param("param3") int anio);

    @Query("SELECT t FROM Tarifa t WHERE t.diaSemana = :param1")
    Optional<Tarifa> findByDay(@Param("param1") int diaSemana);

}
