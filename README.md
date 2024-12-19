# Proyecto-de-Backend
Proyecto de Final de Backend de Aplicaciones realizado en la cursada del 2023
Trabajo Práctico Integrador
 Backend de Aplicaciones 2023
 Estamos trabajando en la implementación de un sistema de alquiler de bicicletas para una
 cierta ciudad. De acuerdo a nuestro modelo, el sistema opera bajo los siguientes supuestos:
 ● Llamamoscliente a quién está registrado en el sistema para alquilar bicicletas.
 Únicamente ellos pueden alquilar bicicletas.
 ● Cadabicicleta que se alquila es retirada de una estación y devuelta en una estación
 distinta.
 ● Asumimosque siempre hay una bicicleta disponible en cada estación y que toda
 estación tiene lugar disponible para una devolución.
 ● Elprecio del alquiler se calcula en el momento de la devolución de la bicicleta y este
 precio se calcula bajo las siguientes reglas:
 ○ Hayuncosto fijo por realizar el alquiler y un costo por hora completa (Cuenta
 como hora completa a partir del minuto 31, antes de eso se tarifa fraccionado
 por minuto). Existe una tabla en la base de datos que se provee que indica
 cuáles son estos costos por cada día de la semana.
 ○ Secobraunmonto adicional por cada KM que separe la estación de retiro de
 la estación de devolución. La base de datos provista contiene el precio
 adicional por KM. El cálculo de esta distancia se explica en las aclaraciones
 finales
 ○ Paralos días promocionales configurados en el sistema, se aplica un
 porcentaje de descuento sobre el monto total del alquiler. Para que este
 descuento se aplique, se considera que el retiro de la bicicleta se hizo en el
 día promocional. La base de datos provista contiene los días que se
 consideran de descuento y el descuento aplicable.
 ● Almomentodela devolución, el cliente decide en qué moneda se le va a mostrar el
 importe adeudado. Por defecto el monto se muestra en Pesos Argentinos, pero
 puede expresarse en cualquier otra moneda soportada por el sistema.
 Se solicita desarrollar un backend, compuesto por microservicios, que exponga un API
 REST (con representación en JSON) para soportar, mínimamente, las siguientes
 funcionalidades:
 1. Consultar el listado de todas las estaciones disponibles en la ciudad
 2. Consultar los datos de la estación más cercana a una ubicación provista por el
 cliente.
 3. Iniciar el alquiler de una bicicleta desde una estación dada
 4. Finalizar un alquiler en curso, informando los datos del mismo y el costo expresado
 en la moneda que el cliente desee. La moneda puede ser elegida en el momento de
finalizar el alquiler y, en caso de no hacerlo, el monto se expresa en pesos
 argentinos.
 5. Agregar una nueva estación al sistema
 6. Obtener un listado de los alquileres realizados aplicando, por lo menos, un filtro
 Tener en cuenta que:
 ❖ Elbackend debe presentar un único punto de entrada (debe exponer todos los
 endpoints en el mismo puerto), para lo que deberá implementar un API Gateway
 ❖ Conesteenunciado se entrega una base de datos como base para utilizar en el
 mismo
 ❖ Lasllamadas a los distintos endpoints únicamente deben ser permitidas para
 clientes autenticados. Para esto, se consideran únicamente dos roles:
 ➢ Administrador
 ■ Puedeagregar nuevas estaciones
 ■ Puedeobtener listados sobre los alquileres realizados
 ➢ Cliente
 ■ Puederealizar consultas sobre las estaciones, realizar alquileres y
 devoluciones.
 ❖ Parael desarrollo de este trabajo práctico, será necesario un trabajo de
 investigación básico sobre manejo de Fecha/Hora en Java. La recomendación es
 investigar el uso de las clases del paquete java.time
 ❖ Seconsiderará, en la evaluación, el uso correcto de los códigos de respuesta HTTP
 ❖ Todoslos endpoints que se generen deben estar documentados utilizando Swagger
 ❖ Paracalcular la distancia entre dos estaciones, se considerará simplemente la
 distancia euclídea entre ambos puntos y cada grado corresponderá a 110000 m. Se
 aclara que este cálculo no es correcto, pero es suficiente para los fines de este
 Trabajo Práctico.
 ❖ Estetrabajo se acompaña por un foro, cuya finalidad exclusiva es evacuar dudas
 sobre este trabajo práctico. Las respuestas brindadas en el foro por los docentes de
 la cátedra tienen tanta validez como lo enunciado aquí.
