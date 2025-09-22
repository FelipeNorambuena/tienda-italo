package com.login.user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para documentación de API.
 * Define la estructura de la documentación y configuración de seguridad.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                    new Server()
                        .url("http://localhost:" + serverPort + contextPath)
                        .description("Servidor de desarrollo local"),
                    new Server()
                        .url("https://api.tienda-italo.com/users")
                        .description("Servidor de producción")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", 
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("Token JWT para autenticación. Formato: Bearer {token}")
                    )
                );
    }

    private Info apiInfo() {
        return new Info()
                .title("Tienda Italo - API de Usuarios")
                .description("""
                    API REST para la gestión completa de usuarios en Tienda Italo.
                    
                    ## Características principales:
                    
                    ### 🔐 Autenticación y Seguridad
                    - Registro de usuarios con validaciones robustas
                    - Autenticación JWT con tokens de acceso y renovación
                    - Sistema de roles y permisos (ADMIN, CLIENTE, GESTOR, VENDEDOR)
                    - Protección contra ataques de fuerza bruta
                    - Verificación de email y recuperación de contraseña
                    
                    ### 👤 Gestión de Perfiles
                    - CRUD completo de usuarios
                    - Gestión de direcciones múltiples
                    - Actualización de perfiles personales
                    - Cambio de contraseñas seguro
                    
                    ### 📊 Administración
                    - Panel administrativo con estadísticas
                    - Búsqueda y filtrado avanzado de usuarios
                    - Gestión de roles y permisos
                    - Activación/desactivación de cuentas
                    
                    ### 🛡️ Seguridad Implementada
                    - Contraseñas encriptadas con BCrypt
                    - Validación de tokens JWT
                    - Rate limiting en endpoints sensibles
                    - CORS configurado para producción
                    - Logs de auditoría de accesos
                    
                    ## Guía de Uso
                    
                    1. **Registro**: Use `/register` para crear una nueva cuenta
                    2. **Login**: Use `/login` para obtener tokens de acceso
                    3. **Autenticación**: Incluya el header `Authorization: Bearer {token}` en requests protegidos
                    4. **Renovación**: Use `/refresh-token` para obtener nuevos tokens sin relogeo
                    
                    ## Códigos de Estado HTTP
                    
                    - `200 OK`: Operación exitosa
                    - `201 Created`: Recurso creado exitosamente
                    - `400 Bad Request`: Datos de entrada inválidos
                    - `401 Unauthorized`: Autenticación requerida o token inválido
                    - `403 Forbidden`: Permisos insuficientes
                    - `404 Not Found`: Recurso no encontrado
                    - `409 Conflict`: Conflicto (ej. email ya registrado)
                    - `422 Unprocessable Entity`: Error de validación
                    - `423 Locked`: Usuario bloqueado
                    - `500 Internal Server Error`: Error interno del servidor
                    """)
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Equipo de Desarrollo Tienda Italo")
                    .email("dev@tienda-italo.com")
                    .url("https://tienda-italo.com"))
                .license(new License()
                    .name("Uso Interno - Tienda Italo")
                    .url("https://tienda-italo.com/licencia"));
    }
}
