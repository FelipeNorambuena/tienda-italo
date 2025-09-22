# 🛠️ Guía del Desarrollador - Tienda Italo

> **Guía completa para desarrolladores que trabajen en el proyecto Tienda Italo**

## 📋 Tabla de Contenidos

- [🚀 Setup Inicial](#-setup-inicial)
- [🏗️ Arquitectura y Patrones](#️-arquitectura-y-patrones)
- [📝 Estándares de Código](#-estándares-de-código)
- [🧪 Testing Guidelines](#-testing-guidelines)
- [🔒 Buenas Prácticas de Seguridad](#-buenas-prácticas-de-seguridad)
- [📚 API Design Guidelines](#-api-design-guidelines)
- [🗄️ Database Guidelines](#️-database-guidelines)
- [🚀 Deployment](#-deployment)

## 🚀 Setup Inicial

### Herramientas Requeridas

```bash
# Java Development Kit 17
java -version # Debería mostrar version 17+

# Maven
mvn -version # Debería mostrar version 3.8+

# MySQL
mysql --version # Debería mostrar version 8.0+

# Git
git --version

# IDE Recomendado: IntelliJ IDEA o VS Code
```

### Configuración del Entorno de Desarrollo

1. **Clonar repositorio**
   ```bash
   git clone <repository-url>
   cd tienda-italo
   ```

2. **Configurar IDE**
   
   **IntelliJ IDEA:**
   ```
   - Import Project → Maven
   - Habilitar annotation processing
   - Instalar plugins: Lombok, MapStruct
   - Configurar code style: Google Java Style
   ```

   **VS Code:**
   ```json
   // .vscode/settings.json
   {
     "java.configuration.updateBuildConfiguration": "automatic",
     "java.compile.nullAnalysis.mode": "automatic",
     "java.format.settings.url": "https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml"
   }
   ```

3. **Base de datos local**
   ```sql
   -- Crear databases
   CREATE DATABASE tienda_usuarios;
   CREATE DATABASE tienda_productos;
   
   -- Crear usuario
   CREATE USER 'tienda_dev'@'localhost' IDENTIFIED BY 'DevPass2024!';
   GRANT ALL PRIVILEGES ON tienda_usuarios.* TO 'tienda_dev'@'localhost';
   GRANT ALL PRIVILEGES ON tienda_productos.* TO 'tienda_dev'@'localhost';
   FLUSH PRIVILEGES;
   ```

4. **Variables de entorno**
   ```bash
   # .env (en cada microservicio)
   SPRING_PROFILES_ACTIVE=dev
   DB_HOST=localhost
   DB_PORT=3306
   DB_USERNAME=tienda_dev
   DB_PASSWORD=DevPass2024!
   JWT_SECRET=DevSecretKey2024
   ```

## 🏗️ Arquitectura y Patrones

### Clean Architecture

Cada microservicio sigue la arquitectura limpia con estas capas:

```
📁 Microservicio/
├── 🎯 domain/           # Reglas de negocio puras
│   ├── entity/          # Entidades con lógica de dominio
│   ├── repository/      # Interfaces (puertos)
│   └── service/         # Servicios de dominio
├── 🔧 application/      # Casos de uso
│   ├── dto/            # Contratos de entrada/salida
│   └── service/        # Orquestación de casos de uso
├── 🏗️ infrastructure/   # Detalles técnicos
│   ├── repository/     # Implementaciones JPA
│   ├── security/       # Configuración de seguridad
│   └── external/       # APIs externas
├── 🌐 web/             # Adaptadores de entrada
│   ├── controller/     # Controladores REST
│   └── mapper/         # Mappers DTO-Entity
└── ⚙️ config/          # Configuración de Spring
```

### Principios SOLID

#### Single Responsibility Principle (SRP)
```java
// ❌ Malo: Clase con múltiples responsabilidades
public class UserService {
    public void saveUser(User user) { /* guardar */ }
    public void sendEmail(String email) { /* enviar email */ }
    public void generateReport() { /* generar reporte */ }
}

// ✅ Bueno: Responsabilidades separadas
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    public void saveUser(User user) {
        userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail());
    }
}
```

#### Dependency Inversion Principle (DIP)
```java
// ✅ Bueno: Depender de abstracciones
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository; // Interface
    private final EmailService emailService; // Interface
    
    // Constructor injection
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, 
                             EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }
}
```

### Domain-Driven Design (DDD)

#### Entidades con Lógica de Negocio
```java
@Entity
public class Usuario {
    // ... campos ...
    
    // Métodos de negocio en la entidad
    public boolean estaBloqueado() {
        return bloqueadoHasta != null && bloqueadoHasta.isAfter(LocalDateTime.now());
    }
    
    public void incrementarIntentosFallidos() {
        this.intentosFallidosLogin++;
        if (this.intentosFallidosLogin >= 5) {
            this.bloqueadoHasta = LocalDateTime.now().plusHours(1);
        }
    }
    
    public boolean puedeAcceder() {
        return activo && emailVerificado && !estaBloqueado();
    }
}
```

#### Value Objects
```java
@Embeddable
public class Email {
    private String valor;
    
    public Email(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.valor = email.toLowerCase().trim();
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
```

## 📝 Estándares de Código

### Naming Conventions

```java
// Clases: PascalCase
public class UsuarioService { }

// Métodos y variables: camelCase
public void actualizarUsuario() { }
private String nombreCompleto;

// Constantes: UPPER_SNAKE_CASE
public static final String JWT_SECRET_KEY = "secret";

// Packages: lowercase con puntos
package com.login.user.domain.entity;
```

### Documentación JavaDoc

```java
/**
 * Servicio para gestión de usuarios del sistema.
 * Proporciona operaciones CRUD y funcionalidades de autenticación.
 * 
 * @author Felipe
 * @since 1.0
 * @see Usuario
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param usuarioRequest datos del usuario a registrar
     * @return usuario registrado con ID asignado
     * @throws IllegalArgumentException si el email ya está registrado
     * @throws ValidationException si los datos son inválidos
     */
    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO usuarioRequest) {
        // implementación...
    }
}
```

### Manejo de Excepciones

```java
// Jerarquía de excepciones del dominio
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}

public class UsuarioNotFoundException extends DomainException {
    public UsuarioNotFoundException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }
}

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("El email ya está registrado: " + email);
    }
}

// Uso en servicios
@Service
public class UsuarioServiceImpl {
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));
        return usuarioMapper.toResponseDTO(usuario);
    }
}
```

### Validaciones

```java
// DTOs con validaciones Bean Validation
public class UsuarioRequestDTO {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Size(max = 255, message = "Email demasiado largo")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "La contraseña debe contener al menos: 1 minúscula, 1 mayúscula, 1 número y 1 carácter especial"
    )
    private String password;
    
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;
}

// Validaciones personalizadas
@Component
public class UsuarioValidator {
    
    public void validarRegistro(UsuarioRequestDTO request) {
        if (esMenorDeEdad(request.getFechaNacimiento())) {
            throw new ValidationException("Debe ser mayor de edad para registrarse");
        }
        
        if (esEmailCorporativo(request.getEmail()) && !esEmailPermitido(request.getEmail())) {
            throw new ValidationException("Email corporativo no permitido");
        }
    }
    
    private boolean esMenorDeEdad(LocalDate fechaNacimiento) {
        return fechaNacimiento != null && 
               Period.between(fechaNacimiento, LocalDate.now()).getYears() < 18;
    }
}
```

## 🧪 Testing Guidelines

### Estructura de Tests

```
📁 src/test/java/
├── 🧪 unit/                     # Tests unitarios
│   ├── domain/
│   │   └── entity/
│   │       └── UsuarioTest.java
│   ├── application/
│   │   └── service/
│   │       └── UsuarioServiceTest.java
│   └── web/
│       └── mapper/
│           └── UsuarioMapperTest.java
├── 🔗 integration/              # Tests de integración
│   ├── repository/
│   │   └── UsuarioRepositoryTest.java
│   └── web/
│       └── controller/
│           └── AuthControllerTest.java
└── 🏗️ architecture/             # Tests de arquitectura
    └── ArchitectureTest.java
```

### Tests Unitarios

```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private UsuarioMapper usuarioMapper;
    
    @InjectMocks
    private UsuarioServiceImpl usuarioService;
    
    @Test
    @DisplayName("Debería registrar usuario exitosamente")
    void deberiaRegistrarUsuarioExitosamente() {
        // Given
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
            .email("test@example.com")
            .nombre("Test")
            .apellido("User")
            .password("Test123!")
            .build();
            
        Usuario usuario = Usuario.builder()
            .email("test@example.com")
            .nombre("Test")
            .apellido("User")
            .build();
            
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioMapper.toEntity(any())).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any())).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(any())).thenReturn(new UsuarioResponseDTO());
        
        // When
        UsuarioResponseDTO resultado = usuarioService.registrarUsuario(request);
        
        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).existsByEmail("test@example.com");
        verify(usuarioRepository).save(any(Usuario.class));
    }
    
    @Test
    @DisplayName("Debería lanzar excepción cuando email ya existe")
    void deberiaLanzarExcepcionCuandoEmailYaExiste() {
        // Given
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
            .email("existing@example.com")
            .build();
            
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> usuarioService.registrarUsuario(request))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessage("El email ya está registrado");
    }
}
```

### Tests de Integración

```java
@SpringBootTest
@Testcontainers
@Transactional
class UsuarioRepositoryTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("test_db")
        .withUsername("test")
        .withPassword("test");
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UsuarioRepositoryImpl usuarioRepository;
    
    @Test
    @DisplayName("Debería encontrar usuario por email")
    void deberiaEncontrarUsuarioPorEmail() {
        // Given
        Usuario usuario = Usuario.builder()
            .email("test@example.com")
            .nombre("Test")
            .apellido("User")
            .passwordHash("hashedPassword")
            .activo(true)
            .build();
        entityManager.persistAndFlush(usuario);
        
        // When
        Optional<Usuario> resultado = usuarioRepository.findByEmail("test@example.com");
        
        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("test@example.com");
    }
}
```

### Tests de Controladores

```java
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UsuarioService usuarioService;
    
    @Test
    @DisplayName("POST /register debería registrar usuario exitosamente")
    void deberiaRegistrarUsuarioExitosamente() throws Exception {
        // Given
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
            .email("test@example.com")
            .nombre("Test")
            .apellido("User")
            .password("Test123!")
            .build();
            
        UsuarioResponseDTO response = UsuarioResponseDTO.builder()
            .id(1L)
            .email("test@example.com")
            .nombre("Test")
            .apellido("User")
            .build();
            
        when(usuarioService.registrarUsuario(any())).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.nombre").value("Test"));
    }
}
```

## 🔒 Buenas Prácticas de Seguridad

### Autenticación JWT

```java
@Component
public class JwtUtil {
    
    private final JwtConfigProperties jwtConfig;
    
    // Usar clave segura con suficiente entropía
    private SecretKey getSigningKey() {
        // Mínimo 256 bits para HS256
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret debe tener al menos 256 bits");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    // Tokens con expiración corta
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", extractRoles(userDetails));
        claims.put("type", "ACCESS");
        
        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
            .signWith(getSigningKey())
            .compact();
    }
}
```

### Validación de Entrada

```java
@RestController
@Validated
public class AuthController {
    
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRequestDTO request) {
        
        // Sanitización adicional
        request.normalizarDatos();
        
        // Validaciones de negocio
        validarReglas Negocio(request);
        
        UsuarioResponseDTO usuario = usuarioService.registrarUsuario(request);
        return ResponseEntity.ok(usuario);
    }
    
    private void validarReglasNegocio(UsuarioRequestDTO request) {
        // Validar que no sea un email temporal/desechable
        if (esEmailTemporal(request.getEmail())) {
            throw new ValidationException("Emails temporales no permitidos");
        }
        
        // Validar fuerza de contraseña adicional
        if (esPasswordComun(request.getPassword())) {
            throw new ValidationException("Contraseña demasiado común");
        }
    }
}
```

### Encriptación de Contraseñas

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt con factor de costo alto para mayor seguridad
        return new BCryptPasswordEncoder(12);
    }
}

// Uso en servicio
@Service
public class UsuarioServiceImpl {
    
    public void cambiarPassword(Long id, String passwordActual, String passwordNueva) {
        Usuario usuario = buscarUsuarioPorId(id);
        
        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getPasswordHash())) {
            // Log del intento sospechoso
            log.warn("Intento de cambio de contraseña con password incorrecto para usuario: {}", id);
            throw new BadCredentialsException("Contraseña actual incorrecta");
        }
        
        // Verificar que la nueva contraseña sea diferente
        if (passwordEncoder.matches(passwordNueva, usuario.getPasswordHash())) {
            throw new ValidationException("La nueva contraseña debe ser diferente a la actual");
        }
        
        usuario.setPasswordHash(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
        
        log.info("Contraseña cambiada exitosamente para usuario: {}", id);
    }
}
```

## 📚 API Design Guidelines

### RESTful Design

```java
// ✅ Bueno: URLs descriptivas y consistentes
@RestController
@RequestMapping("/api/users")
public class UsuarioController {
    
    // Colección de recursos
    @GetMapping                              // GET /api/users
    @GetMapping("/{id}")                     // GET /api/users/123
    @PostMapping                             // POST /api/users
    @PutMapping("/{id}")                     // PUT /api/users/123
    @DeleteMapping("/{id}")                  // DELETE /api/users/123
    
    // Subrecursos
    @GetMapping("/{id}/addresses")           // GET /api/users/123/addresses
    @PostMapping("/{id}/addresses")          // POST /api/users/123/addresses
    
    // Acciones específicas
    @PostMapping("/{id}/activate")           // POST /api/users/123/activate
    @PostMapping("/{id}/reset-password")     // POST /api/users/123/reset-password
}
```

### Response Standards

```java
// Respuestas consistentes
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;
    
    // Métodos estáticos para construcción
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();
    }
}

// Uso en controladores
@RestController
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        
        LoginResponseDTO response = usuarioService.autenticarUsuario(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
```

### Paginación

```java
@GetMapping("/users")
public ResponseEntity<Page<UsuarioResponseDTO>> obtenerUsuarios(
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) 
        Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean activo) {
    
    Page<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuarios(search, activo, pageable);
    return ResponseEntity.ok(usuarios);
}

// Response incluye metadata de paginación
{
  "content": [...],
  "pageable": {
    "sort": {...},
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 150,
  "totalPages": 8,
  "first": true,
  "last": false
}
```

## 🗄️ Database Guidelines

### Naming Conventions

```sql
-- Tablas: snake_case, plural
CREATE TABLE usuarios (...);
CREATE TABLE productos (...);
CREATE TABLE usuario_roles (...);

-- Columnas: snake_case
id BIGINT PRIMARY KEY,
nombre_completo VARCHAR(200),
fecha_creacion TIMESTAMP,
activo BOOLEAN

-- Índices: idx_tabla_columna
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_activo ON usuarios(activo);

-- Foreign Keys: fk_tabla_referencia
CONSTRAINT fk_usuario_roles_usuario 
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
```

### Migraciones Flyway

```sql
-- V1__create_users_table.sql
-- Siempre incluir descripción clara
-- Usar transacciones para operaciones complejas

START TRANSACTION;

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_usuarios_email (email),
    INDEX idx_usuarios_created_at (created_at)
) ENGINE=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar datos iniciales si es necesario
INSERT INTO roles (nombre, descripcion) VALUES 
('ADMIN', 'Administrador del sistema'),
('CLIENTE', 'Cliente registrado');

COMMIT;
```

### JPA Best Practices

```java
@Entity
@Table(name = "usuarios", 
       indexes = {
           @Index(name = "idx_usuarios_email", columnList = "email"),
           @Index(name = "idx_usuarios_activo", columnList = "activo")
       })
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Usar columnDefinition para tipos específicos
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;
    
    // Optimistic locking
    @Version
    private Long version;
    
    // Auditoría automática
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Lazy loading por defecto para relaciones
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DireccionUsuario> direcciones = new ArrayList<>();
    
    // Evitar N+1 queries con @BatchSize
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_roles")
    @BatchSize(size = 10)
    private Set<Rol> roles = new HashSet<>();
}
```

### Query Optimization

```java
@Repository
public interface UsuarioRepositoryImpl extends JpaRepository<Usuario, Long> {
    
    // Fetch joins para evitar N+1
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<Usuario> findByIdWithRoles(@Param("id") Long id);
    
    // Proyecciones para consultas específicas
    @Query("SELECT new com.login.user.dto.UsuarioSummaryDTO(u.id, u.nombre, u.email) " +
           "FROM Usuario u WHERE u.activo = true")
    List<UsuarioSummaryDTO> findActiveUsersSummary();
    
    // Paginación con conteo optimizado
    @Query(value = "SELECT u FROM Usuario u WHERE u.nombre LIKE %:search%",
           countQuery = "SELECT COUNT(u) FROM Usuario u WHERE u.nombre LIKE %:search%")
    Page<Usuario> findByNombreContaining(@Param("search") String search, Pageable pageable);
}
```

## 🚀 Deployment

### Docker Configuration

```dockerfile
# Multi-stage build para optimizar tamaño
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copiar JAR desde stage anterior
COPY --from=build /app/target/*.jar app.jar

# Cambiar ownership
RUN chown -R appuser:appuser /app
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose para Desarrollo

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: tienda_usuarios
      MYSQL_USER: tienda_user
      MYSQL_PASSWORD: TiendaPass2024!
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  user-service:
    build: ./Back-end/user
    ports:
      - "8081:8081"
    depends_on:
      - mysql
    environment:
      SPRING_PROFILES_ACTIVE: dev
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: tienda_usuarios
      DB_USERNAME: tienda_user
      DB_PASSWORD: TiendaPass2024!
    volumes:
      - ./logs:/app/logs

volumes:
  mysql_data:
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
  labels:
    app: user-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: tienda-italo/user-service:latest
        ports:
        - containerPort: 8081
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: host
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8081
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8081
          initialDelaySeconds: 5
          periodSeconds: 5
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
---
apiVersion: v1
kind: Service
metadata:
  name: user-service
spec:
  selector:
    app: user-service
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8081
  type: LoadBalancer
```

## 📊 Monitoring y Observabilidad

### Logs Estructurados

```java
@Slf4j
@Service
public class UsuarioServiceImpl {
    
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO request) {
        // Usar MDC para contexto
        MDC.put("operacion", "registrar_usuario");
        MDC.put("email", request.getEmail());
        
        try {
            log.info("Iniciando registro de usuario");
            
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                log.warn("Intento de registro con email existente");
                throw new EmailAlreadyExistsException(request.getEmail());
            }
            
            Usuario usuario = procesarRegistro(request);
            
            log.info("Usuario registrado exitosamente con ID: {}", usuario.getId());
            return usuarioMapper.toResponseDTO(usuario);
            
        } catch (Exception e) {
            log.error("Error en registro de usuario", e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}
```

### Métricas Personalizadas

```java
@Component
public class UsuarioMetrics {
    
    private final Counter registrosCounter;
    private final Timer loginTimer;
    private final Gauge usuariosActivosGauge;
    
    public UsuarioMetrics(MeterRegistry meterRegistry, UsuarioRepository usuarioRepository) {
        this.registrosCounter = Counter.builder("usuarios.registros.total")
            .description("Total de registros de usuarios")
            .register(meterRegistry);
            
        this.loginTimer = Timer.builder("usuarios.login.duration")
            .description("Tiempo de procesamiento de login")
            .register(meterRegistry);
            
        this.usuariosActivosGauge = Gauge.builder("usuarios.activos.count")
            .description("Número de usuarios activos")
            .register(meterRegistry, this, UsuarioMetrics::countUsuariosActivos);
    }
    
    public void incrementarRegistros() {
        registrosCounter.increment();
    }
    
    public Timer.Sample iniciarTimerLogin() {
        return Timer.start(loginTimer);
    }
    
    private double countUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }
}
```

---

## 📞 Contacto

- **Mantenedor**: Felipe
- **Email**: dev@tienda-italo.com
- **Wiki**: [Documentación Interna](./wiki/)

---

<div align="center">

**[⬆️ Volver al inicio](#-guía-del-desarrollador---tienda-italo)**

*Desarrollado con ❤️ siguiendo las mejores prácticas de la industria*

</div>
