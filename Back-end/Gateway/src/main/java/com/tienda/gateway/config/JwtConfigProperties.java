package com.tienda.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración de propiedades JWT para el Gateway.
 * 
 * @author Tienda Italo Team
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfigProperties {
    
    private String secret;
    private long expiration;
    private long refreshExpiration;
}
