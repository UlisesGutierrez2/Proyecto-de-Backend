package com.bda.trabajoPracticoIntegrador.Service.Implementacion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;

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
            // Log para registrar la solicitud al microservicio de cotizaciones
            System.out.println(cotizacionApiUrl);

            // Realizar la solicitud POST
            ResponseEntity<String> response = restTemplate.exchange(cotizacionApiUrl, HttpMethod.POST, entity, String.class);

            // Log para registrar la respuesta del microservicio
            System.out.println(response);

            System.out.println(response.getStatusCode().toString());

            // Manejar la respuesta del microservicio de cotizaciones según sea necesario
            if (response.getStatusCode().is2xxSuccessful()) {
                // Extraer y manejar la respuesta del microservicio
                String cotizacionResponse = response.getBody();

                // Log para registrar la respuesta detallada del microservicio
                System.out.println(cotizacionResponse);

                // Parsear la respuesta del microservicio para obtener el monto convertido
                double montoConvertido = parsearRespuestaDelMicroservicio(cotizacionResponse, monedaDestino);

                return montoConvertido;
                // el api ya hace este calculo
                // double montoFinal = montoConvertido * montoTotal;

            } else {
                // Log para registrar el fallo de la solicitud al microservicio de cotizaciones
                System.out.println(response.getStatusCode());

                // Manejar el caso en que la solicitud al microservicio de cotizaciones falla
                throw new HttpClientErrorException(response.getStatusCode());
            }
        } catch (Exception e) {
            // Log para registrar cualquier excepción ocurrida durante la ejecución del bloque try
            System.out.println(e);

            // Puedes manejar la excepción según tus necesidades
            throw e;
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