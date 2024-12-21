# Proyecto-de-Backend
Proyecto de Final de Backend de Aplicaciones realizado en la cursada del 2023
# Trabajo Práctico Integrador  
**Backend de Aplicaciones 2023**  

Estamos trabajando en la implementación de un sistema de alquiler de bicicletas para una cierta ciudad. De acuerdo a nuestro modelo, el sistema opera bajo los siguientes supuestos:  

## Supuestos del Sistema  
- **Clientes**: Solo quienes están registrados en el sistema pueden alquilar bicicletas.  
- **Estaciones**:  
  - Cada bicicleta que se alquila es retirada de una estación y devuelta en una estación distinta.  
  - Siempre hay una bicicleta disponible en cada estación y toda estación tiene espacio disponible para una devolución.  
- **Cálculo del precio del alquiler**:  
  1. Hay un costo fijo por realizar el alquiler y un costo por hora completa.  
     - Se considera hora completa a partir del minuto 31 (antes de eso, se tarifa por minuto).  
     - La base de datos incluye una tabla con los costos por cada día de la semana.  
  2. Se cobra un monto adicional por cada kilómetro que separa la estación de retiro de la estación de devolución.  
     - La base de datos incluye el precio adicional por kilómetro.  
     - El cálculo de esta distancia se detalla en las aclaraciones finales.  
  3. Para días promocionales configurados en el sistema, se aplica un porcentaje de descuento sobre el monto total del alquiler.  
     - El descuento se aplica si el retiro de la bicicleta se realizó en el día promocional.  
     - La base de datos incluye los días promocionales y los porcentajes de descuento aplicables.  

- **Moneda**: Al momento de la devolución, el cliente puede elegir la moneda en la que se mostrará el importe adeudado.  
  - Por defecto, se muestra en Pesos Argentinos.  
  - Puede expresarse en cualquier otra moneda soportada por el sistema.  

---

## Funcionalidades del Backend  
El backend debe exponer un API REST (en JSON) que soporte, al menos, las siguientes funcionalidades:  

1. **Consultar el listado de todas las estaciones disponibles en la ciudad.**  
2. **Consultar los datos de la estación más cercana** a una ubicación provista por el cliente.  
3. **Iniciar el alquiler de una bicicleta** desde una estación dada.  
4. **Finalizar un alquiler en curso**, informando:  
   - Los datos del alquiler.  
   - El costo expresado en la moneda deseada por el cliente (por defecto, en pesos argentinos).  
5. **Agregar una nueva estación** al sistema.  
6. **Obtener un listado de los alquileres realizados**, aplicando al menos un filtro.  

---

## Consideraciones Técnicas  
- El backend debe presentar un único punto de entrada (API Gateway) y exponer todos los endpoints en el mismo puerto.  
- Se entrega una base de datos inicial para utilizar como base.  
- Las llamadas a los endpoints solo deben ser permitidas para **clientes autenticados**, con los siguientes roles:  
  - **Administrador**:  
    - Puede agregar nuevas estaciones.  
    - Puede obtener listados sobre los alquileres realizados.  
  - **Cliente**:  
    - Puede realizar consultas sobre estaciones, alquileres y devoluciones.  

---

## Aclaraciones Adicionales  
1. Se requiere investigar el manejo de Fecha/Hora en Java utilizando el paquete `java.time`.  
2. El uso correcto de los códigos de respuesta HTTP será evaluado.  
3. Todos los endpoints deben estar documentados utilizando **Swagger**.  
4. Para calcular la distancia entre dos estaciones, se usará la distancia euclídea:  
   - Cada grado equivale a **110,000 m**.  
   - Este cálculo es aproximado, pero suficiente para este trabajo práctico.  
5. Existe un foro asociado a este trabajo práctico donde se evacuarán dudas.  
   - Las respuestas de los docentes en el foro tienen la misma validez que lo indicado en este documento.  

---

**Nota:** Este trabajo práctico busca fomentar buenas prácticas en el desarrollo de backends basados en microservicios. ¡Éxitos! 🚴‍♂️
