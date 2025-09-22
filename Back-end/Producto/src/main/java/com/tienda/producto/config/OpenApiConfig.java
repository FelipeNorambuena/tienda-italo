package com.tienda.producto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para el microservicio de productos.
 * 
 * @author Tienda Italo Team
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8082}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tienda Italo - Microservicio de Productos")
                        .description("API REST para gestión de productos, categorías y marcas del e-commerce Tienda Italo")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tienda Italo Team")
                                .email("contacto@tiendaitalo.cl")
                                .url("https://tiendaitalo.cl"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("https://api.tiendaitalo.cl")
                                .description("Servidor de producción")
                ));
    }
}
