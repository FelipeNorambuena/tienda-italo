package com.Carrito.compras.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API.
 * Incluye configuración de seguridad JWT y metadatos del proyecto.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🛒 Tienda Italo - Carrito de Compras API")
                        .description("""
                                API REST para gestión del carrito de compras de Tienda Italo.
                                
                                ## Funcionalidades principales:
                                - ✅ Gestión completa del carrito de compras
                                - ✅ Agregar, actualizar y remover productos
                                - ✅ Cálculo automático de totales
                                - ✅ Integración con WhatsApp para finalizar pedidos
                                - ✅ Autenticación JWT con roles
                                - ✅ Validaciones robustas de datos
                                
                                ## Moneda:
                                Todos los precios están en **Pesos Chilenos (CLP)**.
                                
                                ## Autenticación:
                                La API utiliza JWT (JSON Web Tokens) para autenticación.
                                Incluye el token en el header: `Authorization: Bearer <token>`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tienda Italo - Equipo de Desarrollo")
                                .email("desarrollo@tiendaitalo.com")
                                .url("https://tiendaitalo.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8083/api/carrito")
                                .description("🛠️ Servidor de desarrollo local"),
                        new Server()
                                .url("https://api.tiendaitalo.com/api/carrito")
                                .description("🚀 Servidor de producción")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("""
                        Autenticación JWT requerida para endpoints protegidos.
                        
                        **Cómo obtener un token:**
                        1. Autentícate en el sistema de usuarios
                        2. Recibe el token JWT en la respuesta
                        3. Incluye el token en el header: `Authorization: Bearer <tu-token>`
                        
                        **Roles disponibles:**
                        - `CLIENTE`: Acceso básico a carrito
                        - `GESTOR`: Gestión avanzada de carritos
                        - `ADMIN`: Acceso completo a todas las funcionalidades
                        """);
    }
}
