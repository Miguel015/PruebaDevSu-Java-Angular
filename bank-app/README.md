## Banco - Prueba Técnica DevSu

Aplicación full‑stack que implementa la prueba de banco de DevSu:

- Backend Java 17 / Spring Boot 2.7 / JPA / MySQL.
- Frontend Angular (SPA) para gestionar clientes, cuentas, movimientos y reportes.
- Despliegue con Docker Compose (backend + base de datos).

---

## 1. Estructura del proyecto

```text
bank-app/
  backend/          # API REST Spring Boot
  frontend/         # Frontend Angular (full-app)
  docker-compose.yml
```

Backend principal:

- Entidades: Persona, Cliente, Cuenta, Movimiento.
- DTOs: ClienteDTO, CuentaDTO, MovimientoDTO, DTOs de reporte.
- Capas: controller, service, repository, exception.
- Manejo de errores con GlobalExceptionHandler.

Frontend principal (frontend/full-app):

- Componentes: Clientes, Cuentas, Movimientos, Reportes.
- Servicios: cliente.service, cuenta.service, movimiento.service, api.service.
- Estilos globales y componentes con tablas, formularios y validaciones.

---

## 2. Requisitos previos

- Docker y Docker Compose instalados.
- Node.js 16+ y npm (solo si se desea modificar/reconstruir el frontend).

---

## 3. Puesta en marcha rápida

### 3.1. Construir frontend

Desde la carpeta del frontend Angular:

```bash
cd frontend/full-app
set NODE_OPTIONS=--openssl-legacy-provider
npx ng build --configuration development
```

Esto genera los artefactos del frontend en dist/ que luego son servidos junto al backend.

### 3.2. Levantar backend + base de datos con Docker

Desde la raíz de bank-app:

```bash
cd bank-app
docker-compose up --build backend
```

o en segundo plano:

```bash
docker-compose up -d backend
```

Servicios resultantes:

- API backend: http://localhost:8080/api
- Base de datos MySQL: puerto y credenciales configurados en application.properties / docker-compose.yml.

---

## 4. Endpoints principales

Todos los endpoints están bajo el prefijo `/api`.

- `GET /clientes` – listar clientes.
- `POST /clientes` – crear cliente (solo nombre y estado; `clienteId` se genera automáticamente).
- `PUT /clientes/{id}` – actualizar cliente.
- `DELETE /clientes/{id}` – elimina cliente si no tiene cuentas asociadas.

- `GET /cuentas` – listar cuentas.
- `POST /cuentas` – crear cuenta (requiere clienteId existente; se valida que la nueva cuenta sea activa).
- `PUT /cuentas/{id}` – actualizar cuenta.
- `DELETE /cuentas/{id}` – eliminar cuenta.

- `GET /movimientos` – listar movimientos.
- `POST /movimientos` – crear movimiento (DEBITO/CREDITO) con reglas de negocio de saldo y tope diario.
- `PUT /movimientos/{id}` – actualizar movimiento respetando las mismas reglas.
- `DELETE /movimientos/{id}` – eliminar movimiento.

- `GET /reportes?clienteId={id}&fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD` – devuelve JSON con movimientos y un PDF en Base64 para el reporte.

Las respuestas de error de negocio se devuelven en el cuerpo como `{ "error": "mensaje" }` con HTTP 400, y 404 en caso de recursos no encontrados.

---

## 5. Frontend (Angular)

La SPA Angular consume todos los endpoints anteriores y expone las siguientes pantallas:

- **Clientes**: listado, creación/edición con validaciones (nombre mínimo 3 caracteres, estado activo) y mensajes claros al intentar eliminar clientes con cuentas.
- **Cuentas**: listado, búsqueda, creación y edición de cuentas; se exige que las nuevas cuentas se creen activas y con cliente asociado.
- **Movimientos**: registro y listado de movimientos por cuenta, con columna Tipo (Débito/Crédito), reglas de saldo y tope diario, y mensajes de error legibles.
- **Reportes**: selección de cliente y rango de fechas, generación de reporte JSON + visualización de PDF embebido (Base64) y opción de descarga.

Los estilos se encuentran en frontend/full-app/src/styles.css y dan una UI moderna con tablas, botones e iconos.

---

## 6. Pruebas unitarias backend

Las pruebas unitarias se encuentran en:

- backend/src/test/java/com/devsu/bankapp/service/ClienteServiceTest.java
- backend/src/test/java/com/devsu/bankapp/service/MovimientoServiceTest.java

Para ejecutarlas localmente:

```bash
cd backend
mvn test
```

En el Dockerfile de backend se utiliza `mvn -DskipTests package` para acelerar el build de la imagen, pero las pruebas pueden ejecutarse de forma independiente durante el desarrollo.

---

## 7. Notas de diseño y decisiones

- `clienteId` se genera en el backend (formato `CLI` + timestamp) y se reutiliza como identificador mínimo único.
- La entidad Cliente hereda de Persona para reutilizar campos comunes.
- No se permite eliminar un cliente con cuentas asociadas; el backend devuelve un error de negocio y el frontend muestra un mensaje claro.
- Los movimientos almacenan los débitos como valores negativos y los créditos como positivos, manteniendo un saldo consistente en la cuenta.

Este README resume el estado final del proyecto para facilitar su revisión y despliegue.