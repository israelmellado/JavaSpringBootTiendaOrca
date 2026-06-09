# JavaSpringBootTiendaOrca
 - El mismo proyecto monolito pero utilizando SpringBoot para conocimientos.
## 📦 Proyecto: Tienda Virtual ORCA

- Tienda online para gestión de pedidos de artículos de papelería y oficina.

## 🎯 Objetivo
- Desarrollar una aplicación web completa para la gestión de una tienda virtual:

   - Catálogo de artículos con stock y precios
   - Gestión de pedidos por clientes
   - Descuentos para clientes premium (30%)
   - Cálculo automático de totales
   - Control de stock

## 🛠️ Tecnologías

   - Backend: Spring Boot 3.x (Java 17+)
   - Base de datos: MySQL 8.x
   - ORM: Hibernate / Spring Data JPA
   - Frontend: Thymeleaf + Bootstrap 5
   - Seguridad: Spring Security
   - Build: Maven

## 📂 Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── tienda/
│   │       ├── controlador/    # Controladores web
│   │       ├── modelo/        # Entidades JPA
│   │       ├── repositorio/    # Repositorios JPA
│   │       └── servicio/       # Servicios (opcional)
│   └── resources/
│       ├── static/           # CSS, JS, imágenes
│       └── templates/        # Plantillas Thymeleaf

```

## ✨ Funcionalidades
- Gestión de Clientes

   - Registro de clientes
   - Campo esPremium para descuento del 30%
   - Datos: nombre, email, dirección, NIF

- Catálogo de Artículos

   - Código identificador único
   - Descripción
   - Precio de venta
   - Gastos de envío
   - Tiempo de preparación (minutos)
   - Stock disponible
   - Fecha de baja (lógico)

- Gestión de Pedidos

   - Estado: PENDIENTE → ENVIAR → CANCELADO
   - Líneas de pedido con cantidad
   - Cálculo automático de subtotales
   - Descuento premium aplicado
   - Totales: productos, descuento, envío, final
   - Tiempo total de preparación

## 📊 Modelo de Datos
- Cliente

|Campo |Tipo |Notas
|---|---|---
|id |Integer |PK, auto|
|nombre | String|
|email |String|Único
|dirección |String|
|nif |String |
|esPremium|Boolean |Descuento 30%

- Articulo

|Campo |Tipo |Notas
|---|---|---
|id_articulo| Integer PK, auto|
|codigo_id |String Único|
|descripcion | String|
|precio_unitario | Decimal|
|gastos_envio | Decimal|
|tiempo_preparacion_min | Integer|
|stock | Integer|
|fecha_baja| Datetime| Baja lógica

- CabeceraPedido

|Campo |Tipo |Notas
|---|---|---
|id_pedido |Integer| PK, auto|
|num_pedido |Integer|
|id_cliente| Integer FK|
|fecha_hora |Datetime|
|estado |String |PENDIENTE/ENVIAR/CANCELADO|
|total_productos |Decimal|
|descuento_aplicado |Decimal|
|subtotal_con_descuento |Decimal|
|envio_total |Decimal|
|total_final |Decimal|

- LineaPedido

|Campo |Tipo |Notas
|---|---|---
|id_linea |Integer| PK, auto|
|id_pedido |Integer| FK|
|id_articulo| Integer |FK|
|cantidad| Integer|
|subtotal| Decimal|
|gastos_envio_linea |Decimal|


  ✅ Mejores Prácticas

   1. Arquitectura limpia: Separación → entidad
   2. Null safety: Verificar valores nulos antes de operar
   3. Transacciones: Uso de repositorios Spring Data
   4. Validación: Control de stock antes de añadir línea
   5. Baja lógica: Campo fecha_baja en lugar de delete
   6. Cálculos en BD o memoria: Totales calculados en controlador

## 🙏 Agradecimientos
## 📞 Contacto
- Autor: [Israel M.P.]
- Email: israel_melli@hotmail.com
- GitHub: israelmellado

