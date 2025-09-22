package com.tienda.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de rutas del Gateway.
 * 
 * @author Tienda Italo Team
 */
@Configuration
public class GatewayConfig {

    /**
     * Configuración de rutas para los microservicios.
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Ruta para el microservicio de usuarios
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8081"))
                
                // Ruta para el microservicio de productos
                .route("product-service", r -> r
                        .path("/api/productos/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8082"))
                
                // Ruta para el microservicio de pedidos (futuro)
                .route("order-service", r -> r
                        .path("/api/pedidos/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8083"))
                
                // Ruta para el microservicio de pagos (futuro)
                .route("payment-service", r -> r
                        .path("/api/pagos/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8084"))
                
                // Ruta para el frontend
                .route("frontend", r -> r
                        .path("/", "/index.html", "/static/**", "/css/**", "/js/**", "/images/**")
                        .uri("http://localhost:8080"))
                
                .build();
    }
}
