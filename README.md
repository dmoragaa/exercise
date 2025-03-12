# API de Usuarios
## Requisitos previos

- Java 17
- Gradle

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.3
- Spring Security 
- JWT
- Spring Data JPA
- Base de datos H2 (en memoria)
- SpringDoc (Swagger)
- JUnit, Mockito (tests)

## Cómo ejecutar la aplicación

**Paso 1: Clonar el repositorio**

```bash
git clone https://github.com/dmoragaa/exercise.git
cd exercise
```
**Paso 2: Crear archivo .env**

Copiar el archivo `.env.example` a un nuevo archivo llamado `.env` en la raíz del proyecto:

```bash
cp .env.example .env
```

Edita el archivo `.env` con valores reales:

```
JWT_SECRET=1234567
JWT_EXPIRATION_HOURS=24

DB_URL=jdbc:h2:mem:testdb
DB_USERNAME=usuario
DB_PASSWORD=contraseña

PASSWORD_REGEX=^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$
EMAIL_REGEX=^[^@]+@[^@]+\\.[a-zA-Z]{2,}$
```

**Paso 3: Ejecutar la aplicación con Gradle**

```bash
./gradlew bootRun
```

La aplicación estará disponible en `http://localhost:8080`

## Diagrama de solución

```
Cliente (HTTP Request)
      │
      ▼
[UserController]
      │
      ▼
[UserServiceImpl] → [JwtUtil]
       ↓↑
[UserRepository] → (Base de datos H2)
```

## Documentación Swagger (OpenAPI)

La documentación interactiva está disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

## Endpoints

### Crear usuario

- **POST** `/api/users`

Ejemplo de Request JSON:

```json
{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "Password1",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "countrycode": "57" //se hace fix de typo contry
        }
    ]
}
```

Ejemplo de Response JSON:

```json
{
    "id": "9b3a8d60-4a15-4c4b-bc92-53f65ad34cd2",
    "created": "2025-03-11T21:20:01.123",
    "modified": "2025-03-11T21:20:01.123",
    "last_login": "2025-03-11T21:20:01.123",
    "token": "token_ejemplo_jwt",
    "isActive": true,
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "countrycode": "57"
        }
    ]
}
```

## Documentación Swagger (OpenAPI)

La documentación está disponible en:

- URL: `http://localhost:8080/swagger-ui/index.html`

## Ejecutar pruebas unitarias

```bash
./gradlew test
```