package com.tienda.producto.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración de propiedades JWT para el microservicio de productos.
 * 
 * @author Tienda Italo Team
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {
    
    private String secret = "miSecretKeySuperSecretoParaJWTQueDebeSerMuyLargoYComplejo123456789";
    private long expiration = 86400000; // 24 horas en milisegundos
    private long refreshExpiration = 604800000; // 7 días en milisegundos
}
