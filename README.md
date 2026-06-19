# JavaSpringBootTiendaOrca
 - El mismo proyecto monolito pero utilizando SpringBoot para conocimientos.
 - Dashboard, Chatbot, LLMs y MCP.
 - Ollama, Python
 - [![Java,Springboot, ollama](<img src="https://youtube.com" alt="YouTube" width="40" height="40">
)](https://youtu.be/86ETKryHBPw)
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
## 📖 Documentación del Sistema

Para conocer en detalle las reglas de negocio del ERP (Artículos, Clientes Premium, Estados de Pedidos) y la integración del asistente de IA local, consulta la [Especificación de la Arquitectura del ERP](architecture-erp.md).

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

## 🏛️ Arquitectura del Sistema

La aplicación sigue una arquitectura monolítica basada en Spring Boot, organizada por capas para facilitar el mantenimiento y la evolución del proyecto.

### Capas principales

* **Controladores (Controller)**

  * Gestionan las peticiones HTTP.
  * Preparan los datos para las vistas Thymeleaf.
  * Coordinan la interacción entre usuario y lógica de negocio.

* **Servicios (Service)**

  * Implementan las reglas de negocio.
  * Validan operaciones y cálculos.
  * Orquestan el acceso a los repositorios.

* **Repositorios (Repository)**

  * Acceso a datos mediante Spring Data JPA.
  * Consultas derivadas y personalizadas.
  * Abstracción de la persistencia.

* **Entidades (Entity)**

  * Representan el modelo de datos de la aplicación.
  * Mapeadas mediante Hibernate/JPA.

### Flujo general

```text
Usuario
   ↓
Controlador
   ↓
Servicio
   ↓
Repositorio
   ↓
MySQL
```

## 🔐 Seguridad

La aplicación utiliza Spring Security para el control de acceso y autenticación de usuarios.

### Características

* Inicio de sesión mediante formulario.
* Gestión de usuarios almacenados en base de datos.
* Contraseñas cifradas mediante BCrypt.
* Protección de rutas privadas.
* Control de acceso basado en roles.

## 🤖 Inteligencia Artificial Local (Experimental)

El proyecto incorpora una línea de investigación basada en modelos de lenguaje locales ejecutados mediante Ollama.

### Objetivos

* Asistente para consulta de reglas de negocio.
* Ayuda contextual dentro del ERP.
* Generación de respuestas sobre pedidos, clientes y artículos.
* Exploración de integración con MCP (Model Context Protocol).

### Tecnologías utilizadas

* Spring AI
* Ollama
* Llama 3.2
* MCP (experimental)

### Estado actual

| Componente | Estado           |
| ---------- | ---------------- |
| Chat local | En desarrollo    |
| Spring AI  | Integrado        |
| Ollama     | Operativo        |
| MCP Server | Experimental     |
| MCP Tools  | En investigación |

## 📈 Dashboard ERP

La aplicación incluye un módulo de Dashboard destinado a ofrecer una visión rápida del estado del negocio.

### Indicadores disponibles

* Total de artículos registrados.
* Pedidos pendientes.
* Pedidos enviados.
* Pedidos realizados durante el día.
* Ingresos acumulados.
* Ingresos diarios.

### Objetivo futuro

Incorporar métricas avanzadas mediante gráficos dinámicos:

* Ventas de los últimos 7 días.
* Evolución de ingresos.
* Rendimiento comercial.
* Indicadores de actividad.

Este módulo tiene carácter informativo y no modifica datos del sistema.


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

