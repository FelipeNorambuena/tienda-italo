# ⚡ Quick Start - Tienda Italo

> **Guía de instalación rápida para ejecutar Tienda Italo en 5 minutos**

## 🎯 Objetivo

Tener la tienda funcionando localmente con:
- ✅ Microservicio de usuarios operativo
- ✅ Base de datos configurada
- ✅ API REST disponible
- ✅ Documentación Swagger accesible

## 📋 Prerrequisitos Mínimos

| Herramienta | Versión | Download |
|-------------|---------|----------|
| ☕ **Java** | 17+ | [OpenJDK](https://openjdk.java.net/) |
| 🗄️ **MySQL** | 8.0+ | [MySQL](https://dev.mysql.com/downloads/) |

## 🚀 Instalación Express (5 minutos)

### Paso 1: Preparar Base de Datos (1 min)

```sql
-- Conectar a MySQL como root y ejecutar:
CREATE DATABASE tienda_usuarios;
CREATE USER 'tienda_user'@'localhost' IDENTIFIED BY 'TiendaPass2024!';
GRANT ALL PRIVILEGES ON tienda_usuarios.* TO 'tienda_user'@'localhost';
FLUSH PRIVILEGES;
```

### Paso 2: Clonar y Configurar (1 min)

```bash
# Clonar repositorio
git clone <repository-url>
cd tienda-italo

# Verificar configuración (opcional)
cat Back-end/user/src/main/resources/application.properties
```

### Paso 3: Ejecutar Microservicio de Usuarios (2 mins)

```bash
# Navegar al directorio
cd Back-end/user

# Ejecutar (usando Maven Wrapper)
./mvnw spring-boot:run

# En Windows:
# mvnw.cmd spring-boot:run
```

### Paso 4: Verificar Instalación (1 min)

Abrir en el navegador:

1. **🏥 Health Check**: http://localhost:8081/api/users/health
   ```json
   {
     "status": "UP",
     "service": "user-service",
     "timestamp": "2024-01-15T10:30:45"
   }
   ```

2. **📚 Swagger UI**: http://localhost:8081/swagger-ui.html
   - Interfaz interactiva de la API
   - Todos los endpoints documentados

3. **🧪 Test API**: 
   ```bash
   curl http://localhost:8081/api/users/health
   ```

## ✅ Verificación Completa

### Test 1: Registrar Usuario

```bash
curl -X POST http://localhost:8081/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "nombre": "Test",
    "apellido": "User",
    "password": "Test123!"
  }'
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "email": "test@example.com",
  "nombre": "Test",
  "apellido": "User",
  "nombreCompleto": "Test User",
  "activo": true,
  "emailVerificado": false
}
```

### Test 2: Login

```bash
curl -X POST http://localhost:8081/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123!"
  }'
```

**Respuesta esperada:**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "usuario": {
    "id": 1,
    "email": "test@example.com",
    "nombre": "Test"
  }
}
```

## 🌐 URLs Importantes

| Servicio | URL | Descripción |
|----------|-----|-------------|
| 🏥 **Health** | http://localhost:8081/api/users/health | Estado del servicio |
| 📚 **Swagger** | http://localhost:8081/swagger-ui.html | Documentación API |
| 🔐 **API Base** | http://localhost:8081/api/users | Base de la API |
| 📊 **Actuator** | http://localhost:8081/actuator | Métricas (requiere admin) |

## 🎮 Playground - Pruebas Rápidas

### Usando Swagger UI (Recomendado)

1. Ir a http://localhost:8081/swagger-ui.html
2. Expandir **"AuthController"**
3. Probar **POST /register** con estos datos:
   ```json
   {
     "email": "playground@test.com",
     "nombre": "Play",
     "apellido": "Ground",
     "password": "Play123!"
   }
   ```
4. Probar **POST /login** con las mismas credenciales
5. Copiar el `accessToken` de la respuesta
6. Usar el botón **"Authorize"** en Swagger para autenticarse
7. Probar endpoints protegidos como **GET /profile**

### Usuario Administrador por Defecto

```json
{
  "email": "admin@tiendaitalo.com",
  "password": "Admin123!"
}
```

**Permisos del admin:**
- ✅ Gestión completa de usuarios
- ✅ Estadísticas del sistema
- ✅ Activar/desactivar cuentas
- ✅ Asignar roles

## 🛠️ Troubleshooting Rápido

### Error: "Connection refused"

```bash
# Verificar que MySQL esté corriendo
sudo systemctl status mysql

# En Windows/Mac, verificar en Services/Activity Monitor
```

### Error: "Access denied for user"

```sql
-- Re-ejecutar la configuración de usuario
DROP USER 'tienda_user'@'localhost';
CREATE USER 'tienda_user'@'localhost' IDENTIFIED BY 'TiendaPass2024!';
GRANT ALL PRIVILEGES ON tienda_usuarios.* TO 'tienda_user'@'localhost';
FLUSH PRIVILEGES;
```

### Error: "Port 8081 already in use"

```bash
# Cambiar puerto en application.properties
echo "server.port=8082" >> Back-end/user/src/main/resources/application.properties

# O matar proceso que usa el puerto
# Linux/Mac:
lsof -ti:8081 | xargs kill -9

# Windows:
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

### Error: "Cannot resolve dependency"

```bash
# Limpiar y reinstalar dependencias
cd Back-end/user
./mvnw clean install
```

## 📊 Base de Datos - Vista Rápida

Después de ejecutar, la BD tendrá:

```sql
-- Ver usuarios creados
SELECT id, email, nombre, activo, email_verificado FROM usuarios;

-- Ver roles disponibles
SELECT * FROM roles;

-- Ver relaciones usuario-rol
SELECT u.email, r.nombre as rol 
FROM usuarios u 
JOIN usuario_roles ur ON u.id = ur.usuario_id 
JOIN roles r ON ur.rol_id = r.id;
```

## 🚀 Próximos Pasos

Una vez que tengas el microservicio de usuarios funcionando:

1. **📦 Microservicio de Productos**
   ```bash
   cd ../Producto
   ./mvnw spring-boot:run
   ```

2. **🌐 Gateway/Frontend**
   ```bash
   cd ../Gateway
   ./mvnw spring-boot:run
   ```

3. **🎨 Frontend Bootstrap**
   - Acceder a http://localhost:8080
   - Interfaz completa de la tienda

## 💡 Tips de Desarrollo

### Logs en Tiempo Real

```bash
# Ver logs detallados
tail -f logs/user-service.log

# Solo errores
grep ERROR logs/user-service.log
```

### Hot Reload (Desarrollo)

```xml
<!-- En pom.xml, agregar: -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### Variables de Entorno

```bash
# Para desarrollo local
export SPRING_PROFILES_ACTIVE=dev
export LOG_LEVEL=DEBUG

# Ejecutar con perfil específico
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

## 🎯 Checklist de Verificación

- [ ] ✅ Java 17+ instalado
- [ ] ✅ MySQL 8.0+ funcionando
- [ ] ✅ Base de datos `tienda_usuarios` creada
- [ ] ✅ Usuario `tienda_user` configurado
- [ ] ✅ Microservicio iniciado sin errores
- [ ] ✅ Health check responde OK
- [ ] ✅ Swagger UI accesible
- [ ] ✅ Registro de usuario funciona
- [ ] ✅ Login retorna JWT tokens
- [ ] ✅ Endpoints protegidos requieren autenticación

## 📞 Ayuda Rápida

**¿Algo no funciona?**

1. 🔍 Revisar logs en consola
2. 📋 Verificar checklist anterior
3. 🗄️ Confirmar que MySQL esté accesible
4. 🌐 Probar health check endpoint
5. 📧 Contactar: dev@tienda-italo.com

---

<div align="center">

🎉 **¡Felicitaciones! Tienes Tienda Italo funcionando** 🎉

**[📚 Ver documentación completa](./README.md)** | **[🛠️ Guía del desarrollador](./docs/DEVELOPER_GUIDE.md)**

</div>
