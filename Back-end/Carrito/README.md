# 🛒 Tienda Italo - Carrito de Compras API

API REST para gestión del carrito de compras de Tienda Italo, desarrollada con Spring Boot siguiendo principios de Clean Architecture y mejores prácticas de desarrollo.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Requisitos](#-requisitos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Documentación de la API](#-documentación-de-la-api)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Testing](#-testing)
- [Despliegue](#-despliegue)
- [Contribución](#-contribución)

## ✨ Características

- ✅ **Gestión completa del carrito de compras**
- ✅ **Agregar, actualizar y remover productos**
- ✅ **Cálculo automático de totales en CLP**
- ✅ **Integración con WhatsApp para finalizar pedidos**
- ✅ **Autenticación JWT con roles (CLIENTE, GESTOR, ADMIN)**
- ✅ **Validaciones robustas de datos**
- ✅ **Documentación automática con Swagger/OpenAPI**
- ✅ **Tests unitarios y de integración**
- ✅ **Migraciones de base de datos con Flyway**
- ✅ **Arquitectura limpia y escalable**

## 🛠️ Tecnologías

### Backend
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **Hibernate**
- **MySQL 8.0**
- **Flyway** (Migraciones)
- **MapStruct** (Mapeo de objetos)
- **Lombok** (Reducción de boilerplate)
- **Swagger/OpenAPI** (Documentación)

### Testing
- **JUnit 5**
- **Mockito**
- **Spring Boot Test**
- **TestContainers** (Para tests de integración)

### Herramientas
- **Maven** (Gestión de dependencias)
- **Git** (Control de versiones)

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/Carrito/compras/
│   │   ├── application/           # Capa de aplicación (Casos de uso)
│   │   │   └── service/
│   │   │       ├── CarritoService.java
│   │   │       └── impl/
│   │   │           └── CarritoServiceImpl.java
│   │   ├── config/               # Configuración
│   │   │   ├── OpenApiConfig.java
│   │   │   └── SecurityConfig.java
│   │   ├── domain/               # Capa de dominio
│   │   │   ├── entity/
│   │   │   │   ├── Carrito.java
│   │   │   │   └── ItemCarrito.java
│   │   │   └── repository/
│   │   │       ├── CarritoRepositoryInterface.java
│   │   │       └── ItemCarritoRepositoryInterface.java
│   │   ├── infrastructure/       # Capa de infraestructura
│   │   │   ├── repository/
│   │   │   │   ├── CarritoJpaRepository.java
│   │   │   │   ├── ItemCarritoJpaRepository.java
│   │   │   │   └── impl/
│   │   │   │       ├── CarritoRepositoryImpl.java
│   │   │   │       └── ItemCarritoRepositoryImpl.java
│   │   │   └── security/
│   │   │       ├── JwtTokenService.java
│   │   │       └── JwtAuthenticationFilter.java
│   │   ├── web/                  # Capa de presentación
│   │   │   ├── controller/
│   │   │   │   └── CarritoController.java
│   │   │   ├── dto/
│   │   │   │   ├── AgregarItemRequestDTO.java
│   │   │   │   ├── CarritoResponseDTO.java
│   │   │   │   ├── ItemCarritoResponseDTO.java
│   │   │   │   └── WhatsAppPedidoDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── CarritoBusinessException.java
│   │   │   └── mapper/
│   │   │       └── CarritoMapper.java
│   │   └── ComprasApplication.java
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       ├── application-prod.properties
│       └── db/migration/
│           └── V1__init_carrito.sql
└── test/
    └── java/com/Carrito/compras/
        ├── application/service/impl/
        │   └── CarritoServiceImplTest.java
        └── web/controller/
            └── CarritoControllerIntegrationTest.java
```

## 📋 Requisitos

### Software Requerido
- **Java 17** o superior
- **Maven 3.6+**
- **MySQL 8.0** o superior
- **Git**

### Variables de Entorno
```bash
# Base de datos
DB_HOST=localhost
DB_PORT=3306
DB_NAME=tienda_carrito
DB_USERNAME=root
DB_PASSWORD=

# JWT
JWT_SECRET=miClaveSecretaSuperSeguraParaJWT2024TiendaItalo
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# WhatsApp
WHATSAPP_NUMBER=+56974161396
```

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tiendaitalo/carrito-api.git
cd carrito-api
```

### 2. Configurar Base de Datos
```bash
# Crear base de datos MySQL
mysql -u root -p
CREATE DATABASE tienda_carrito;
```

### 3. Configurar Variables de Entorno
```bash
# Copiar archivo de configuración
cp src/main/resources/application-dev.properties.example src/main/resources/application-dev.properties

# Editar configuración
nano src/main/resources/application-dev.properties
```

### 4. Instalar Dependencias
```bash
mvn clean install
```

### 5. Ejecutar Migraciones
```bash
# Las migraciones se ejecutan automáticamente al iniciar la aplicación
mvn spring-boot:run
```

### 6. Verificar Instalación
```bash
# Verificar que la API esté funcionando
curl http://localhost:8083/api/carrito/actuator/health
```

## 📚 Documentación de la API

### Swagger UI
Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva en:
- **Desarrollo**: http://localhost:8083/api/carrito/swagger-ui.html
- **Producción**: https://api.tiendaitalo.com/api/carrito/swagger-ui.html

### Endpoints Principales

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| `GET` | `/carrito/usuario/{usuarioId}` | Obtener carrito de usuario | No |
| `POST` | `/carrito/usuario/{usuarioId}` | Crear nuevo carrito | No |
| `POST` | `/carrito/usuario/{usuarioId}/items` | Agregar producto al carrito | No |
| `PUT` | `/carrito/usuario/{usuarioId}/items/{itemId}` | Actualizar cantidad | No |
| `DELETE` | `/carrito/usuario/{usuarioId}/items/{itemId}` | Remover producto | No |
| `DELETE` | `/carrito/usuario/{usuarioId}` | Limpiar carrito | No |
| `GET` | `/carrito/usuario/{usuarioId}/pedido-whatsapp` | Generar pedido WhatsApp | No |
| `POST` | `/carrito/usuario/{usuarioId}/finalizar-compra` | Finalizar compra | No |

### Autenticación JWT

Para endpoints protegidos, incluye el token JWT en el header:
```http
Authorization: Bearer <tu-token-jwt>
```

**Roles disponibles:**
- `CLIENTE`: Acceso básico a carrito
- `GESTOR`: Gestión avanzada de carritos  
- `ADMIN`: Acceso completo a todas las funcionalidades

## 💡 Ejemplos de Uso

### 1. Obtener Carrito de Usuario
```bash
curl -X GET "http://localhost:8083/api/carrito/carrito/usuario/123" \
     -H "Content-Type: application/json"
```

**Respuesta:**
```json
{
  "id": 1,
  "usuarioId": 123,
  "items": [
    {
      "id": 1,
      "productoId": 456,
      "nombreProducto": "Laptop HP Pavilion",
      "precioUnitario": 599990.00,
      "cantidad": 1,
      "subtotal": 599990.00
    }
  ],
  "total": 599990.00,
  "activo": true,
  "cantidadTotalItems": 1
}
```

### 2. Agregar Producto al Carrito
```bash
curl -X POST "http://localhost:8083/api/carrito/carrito/usuario/123/items" \
     -H "Content-Type: application/json" \
     -d '{
       "productoId": 456,
       "nombreProducto": "Laptop HP Pavilion",
       "precioUnitario": 599990.00,
       "cantidad": 1
     }'
```

### 3. Finalizar Compra
```bash
curl -X POST "http://localhost:8083/api/carrito/carrito/usuario/123/finalizar-compra" \
     -H "Content-Type: application/json"
```

**Respuesta:**
```
https://wa.me/56974161396?text=Hola!%20Quiero%20realizar%20el%20siguiente%20pedido:%0A%0A- Laptop HP Pavilion x1: $599.990%0A%0ATotal: $599.990%0A%0A¿Podrías confirmar disponibilidad y forma de pago?
```

## 🧪 Testing

### Ejecutar Tests Unitarios
```bash
mvn test
```

### Ejecutar Tests de Integración
```bash
mvn verify
```

### Ejecutar Tests con Cobertura
```bash
mvn test jacoco:report
```

### Tests Disponibles
- **CarritoServiceImplTest**: Tests unitarios para la lógica de negocio
- **CarritoControllerIntegrationTest**: Tests de integración para endpoints REST

## 🚀 Despliegue

### Desarrollo
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Producción
```bash
# Compilar aplicación
mvn clean package -Pprod

# Ejecutar JAR
java -jar target/compras-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker (Opcional)
```bash
# Construir imagen
docker build -t tiendaitalo/carrito-api .

# Ejecutar contenedor
docker run -p 8083:8083 \
  -e DB_HOST=host.docker.internal \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=password \
  tiendaitalo/carrito-api
```

## 🔧 Configuración Avanzada

### Perfiles de Spring
- **dev**: Desarrollo local con logging detallado
- **prod**: Producción con optimizaciones

### Propiedades de Configuración
```properties
# Puerto del servidor
server.port=8083

# Context path
server.servlet.context-path=/api/carrito

# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/tienda_carrito?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# JWT
jwt.secret=miClaveSecretaSuperSeguraParaJWT2024TiendaItalo
jwt.expiration=86400000
```

## 📊 Monitoreo y Logs

### Actuator Endpoints
- **Health**: `/actuator/health`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

### Logs
Los logs se configuran según el perfil activo:
- **Desarrollo**: Logs detallados con SQL queries
- **Producción**: Logs mínimos para rendimiento

## 🤝 Contribución

### Cómo Contribuir
1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

### Estándares de Código
- Seguir principios de Clean Architecture
- Cobertura de tests mínima del 80%
- Documentación actualizada
- Código en español (comentarios y documentación)

### Reportar Issues
Usa el sistema de issues de GitHub para reportar bugs o solicitar features.

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 📞 Contacto

**Tienda Italo - Equipo de Desarrollo**
- Email: desarrollo@tiendaitalo.com
- Website: https://tiendaitalo.com
- GitHub: https://github.com/tiendaitalo

---

## 🎯 Roadmap

### Próximas Funcionalidades
- [ ] Integración con sistema de pagos
- [ ] Notificaciones por email
- [ ] Dashboard de administración
- [ ] API de reportes y analytics
- [ ] Cache con Redis
- [ ] Rate limiting
- [ ] Métricas con Prometheus

### Mejoras Técnicas
- [ ] Migración a Spring Boot 3.6
- [ ] Implementación de GraphQL
- [ ] Tests de carga con JMeter
- [ ] CI/CD con GitHub Actions
- [ ] Containerización con Kubernetes

---

**¡Gracias por usar la API del Carrito de Tienda Italo! 🛒✨**
