Instrucciones de ejecución (resumen)

Backend (Spring Boot):
- Usar Maven: `mvn spring-boot:run`
- Tests: `mvn test`
- Construir JAR: `mvn clean package`

Frontend (Angular):
- Instalar dependencias: `npm install`
- Ejecutar: `ng serve`
- Tests (Jest): configuración adicional necesaria (se proveerán archivos de configuración)

Docker (backend):
- `docker build -t bank-backend .`
- `docker run -p 8080:8080 bank-backend`
