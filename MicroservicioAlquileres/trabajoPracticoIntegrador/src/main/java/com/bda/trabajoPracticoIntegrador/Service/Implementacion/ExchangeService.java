package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeService {
    private final RestTemplate restTemplate;

    public ExchangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double obtenerCotizacion(String cotizacionApiUrl, String monedaDestino, double importe) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Armar la solicitud
        String requestBody = "{\"moneda_destino\":\"" + monedaDestino + "\",\"importe\":" + importe + "}";

        // Configurar la solicitud HTTP
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Realizar la solicitud POST
            ResponseEntity<String> response = restTemplate.exchange(cotizacionApiUrl, HttpMethod.POST, entity, String.class);

            // Verificar si la solicitud fue exitosa
            if (response.getStatusCode().is2xxSuccessful()) {
                // Extraer y manejar la respuesta del microservicio
                String cotizacionResponse = response.getBody();
                return parsearRespuestaDelMicroservicio(cotizacionResponse, monedaDestino);
            } else {
                // Manejar el caso en que la solicitud al microservicio de cotizaciones falla
                throw new HttpClientErrorException(response.getStatusCode());
            }
        } catch (Exception e) {
            // Manejar cualquier excepción ocurrida durante la ejecución
            throw new RuntimeException("Error al realizar la solicitud al microservicio de cotizaciones", e);
        }
    }

    private double parsearRespuestaDelMicroservicio(String cotizacionResponse, String monedaEsperada) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(cotizacionResponse);

            // Verificar si el JSON tiene las propiedades necesarias
            if (jsonNode.has("moneda") && jsonNode.has("importe")) {
                String moneda = jsonNode.get("moneda").asText();
                double importe = jsonNode.get("importe").asDouble();

                // Verificar si la moneda es la esperada
                if (monedaEsperada.equals(moneda)) {
                    return importe;
                } else {
                    throw new IllegalArgumentException("La respuesta del microservicio no es en " + monedaEsperada + ": " + cotizacionResponse);
                }
            } else {
                throw new IllegalArgumentException("La respuesta del microservicio no tiene las propiedades esperadas: " + cotizacionResponse);
            }
        } catch (Exception e) {
            // Manejar cualquier excepción durante el análisis
            throw new RuntimeException("Error al analizar la respuesta del microservicio", e);
        }
    }

}
