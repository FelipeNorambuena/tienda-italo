package com.login.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propiedades de configuración para JWT.
 * Mapea las propiedades personalizadas del application.properties.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {

    /**
     * Clave secreta para firmar los tokens JWT
     */
    private String secret = "TiendaItaloSecretKey2024ForUserAuthentication";

    /**
     * Tiempo de expiración del access token en milisegundos (por defecto 24 horas)
     */
    private Long expiration = 86400000L;

    /**
     * Tiempo de expiración del refresh token en milisegundos (por defecto 7 días)
     */
    private Long refreshExpiration = 604800000L;
}
