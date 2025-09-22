# 🛒 Tienda Italo - E-commerce Platform

Una plataforma de e-commerce completa desarrollada con arquitectura de microservicios, Spring Boot, Bootstrap y base de datos relacional, optimizada para el mercado chileno.

## 🏗️ Arquitectura del Sistema

### Microservicios Backend
- **Gateway** - API Gateway con Spring Cloud Gateway
- **User** - Microservicio de gestión de usuarios y autenticación
- **Producto** - Microservicio de catálogo de productos y gestión de inventario

### Frontend
- **Frontend** - Interfaz de usuario con Bootstrap y Thymeleaf

## 🚀 Tecnologías Utilizadas

### Backend
- **Java 17** - Lenguaje de programación
- **Spring Boot 3.2.0** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Spring Cloud Gateway** - API Gateway
- **MySQL 8.0** - Base de datos principal
- **H2** - Base de datos para testing
- **Flyway** - Migraciones de base de datos
- **MapStruct** - Mapeo de entidades a DTOs
- **JWT** - Autenticación basada en tokens
- **Swagger/OpenAPI** - Documentación de API
- **Lombok** - Reducción de boilerplate
- **Maven** - Gestión de dependencias

### Frontend
- **Bootstrap 5** - Framework CSS
- **Thymeleaf** - Motor de plantillas
- **JavaScript** - Interactividad
- **HTML5/CSS3** - Estructura y estilos

### Testing
- **JUnit 5** - Testing unitario
- **Mockito** - Mocking
- **Spring Boot Test** - Testing de integración
- **Testcontainers** - Testing con contenedores
- **Jacoco** - Cobertura de código

## 📁 Estructura del Proyecto

```
Tienda Italo/
├── Back-end/
│   ├── Gateway/                 # API Gateway
│   ├── user/                   # Microservicio de usuarios
│   └── Producto/               # Microservicio de productos
├── Front-end/                  # Interfaz de usuario
├── docs/                      # Documentación
├── README.md                  # Este archivo
└── QUICK_START.md            # Guía de inicio rápido
```

## 🎯 Funcionalidades Implementadas

### Microservicio de Usuarios ✅
- ✅ Registro y autenticación de usuarios
- ✅ Gestión de perfiles y direcciones
- ✅ Recuperación de contraseñas
- ✅ Roles y permisos (ADMIN, CLIENTE)
- ✅ JWT para autenticación
- ✅ Validaciones de seguridad
- ✅ Tests unitarios e integración

### Microservicio de Productos 🚧
- ✅ Entidades de dominio (Producto, Categoría, Marca)
- ✅ Repositorios JPA con consultas avanzadas
- ✅ DTOs para transferencia de datos
- ✅ Mappers MapStruct para conversiones
- ✅ Servicios de aplicación (interfaces)
- 🚧 Implementaciones de servicios
- 🚧 Controladores REST
- 🚧 Tests unitarios e integración

### API Gateway 🚧
- 🚧 Configuración de rutas
- 🚧 Filtros de seguridad
- 🚧 Load balancing

### Frontend 🚧
- 🚧 Interfaz de usuario con Bootstrap
- 🚧 Catálogo de productos
- 🚧 Carrito de compras
- 🚧 Proceso de checkout

## 🛠️ Instalación y Configuración

### Prerrequisitos
- Java 17 o superior
- Maven 3.6 o superior
- MySQL 8.0 o superior
- Git

### Configuración de Base de Datos
1. Crear base de datos MySQL:
```sql
CREATE DATABASE tienda_usuario;
CREATE DATABASE tienda_producto;
CREATE DATABASE tienda_gateway;
```

2. Configurar credenciales en `application.properties` de cada microservicio

### Ejecución
1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/tienda-italo.git
cd tienda-italo
```

2. Ejecutar migraciones de base de datos:
```bash
cd Back-end/user
mvn flyway:migrate

cd ../Producto
mvn flyway:migrate
```

3. Ejecutar los microservicios:
```bash
# Terminal 1 - User Service
cd Back-end/user
mvn spring-boot:run

# Terminal 2 - Product Service
cd Back-end/Producto
mvn spring-boot:run

# Terminal 3 - Gateway
cd Back-end/Gateway
mvn spring-boot:run
```

## 📚 Documentación de API

### User Service
- **Base URL**: `http://localhost:8081/api`
- **Swagger UI**: `http://localhost:8081/swagger-ui.html`

### Product Service
- **Base URL**: `http://localhost:8082/api`
- **Swagger UI**: `http://localhost:8082/swagger-ui.html`

### Gateway
- **Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

## 🧪 Testing

### Ejecutar Tests Unitarios
```bash
cd Back-end/user
mvn test

cd Back-end/Producto
mvn test
```

### Ejecutar Tests de Integración
```bash
cd Back-end/user
mvn verify

cd Back-end/Producto
mvn verify
```

### Cobertura de Código
```bash
cd Back-end/user
mvn jacoco:report

cd Back-end/Producto
mvn jacoco:report
```

## 🏗️ Arquitectura Clean

El proyecto sigue los principios de Clean Architecture:

- **Domain Layer**: Entidades de negocio y reglas de dominio
- **Application Layer**: Casos de uso y servicios de aplicación
- **Infrastructure Layer**: Implementaciones técnicas (JPA, Security, etc.)
- **Web Layer**: Controladores REST y DTOs

## 🔒 Seguridad

- Autenticación JWT
- Encriptación de contraseñas con BCrypt
- Validación de datos de entrada
- CORS configurado
- CSRF deshabilitado para APIs REST
- Rate limiting (pendiente)

## 🌍 Adaptación al Mercado Chileno

- Moneda en pesos chilenos (CLP)
- Formatos de fecha y hora en español
- Textos y mensajes en español
- Consideraciones legales chilenas
- Ejemplos de productos locales

## 📈 Roadmap

### Fase 1 - Core Backend ✅
- [x] Microservicio de usuarios
- [x] Microservicio de productos (en progreso)
- [x] API Gateway (en progreso)

### Fase 2 - Frontend 🚧
- [ ] Interfaz de usuario con Bootstrap
- [ ] Catálogo de productos
- [ ] Carrito de compras
- [ ] Proceso de checkout

### Fase 3 - Funcionalidades Avanzadas 📋
- [ ] Sistema de pedidos
- [ ] Gestión de inventario
- [ ] Reportes y analytics
- [ ] Integración con pasarelas de pago

### Fase 4 - Optimización 📋
- [ ] Caché con Redis
- [ ] Monitoreo con Actuator
- [ ] Logs centralizados
- [ ] CI/CD con GitHub Actions

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👥 Equipo

- **Felipe** - Desarrollador Principal
- **Tienda Italo Team** - Equipo de desarrollo

## 📞 Contacto

- **Email**: contacto@tiendaitalo.cl
- **Website**: https://tiendaitalo.cl
- **GitHub**: https://github.com/tu-usuario/tienda-italo

---

**Desarrollado con ❤️ para el mercado chileno**