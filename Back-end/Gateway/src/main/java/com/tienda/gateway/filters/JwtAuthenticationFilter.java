package com.tienda.gateway.filters;

import com.tienda.gateway.config.JwtConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Filtro de autenticación JWT para el Gateway.
 * 
 * @author Tienda Italo Team
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtConfigProperties jwtConfig;
    
    public JwtAuthenticationFilter(JwtConfigProperties jwtConfig) {
        super(Config.class);
        this.jwtConfig = jwtConfig;
    }
    
    // Rutas públicas que no requieren autenticación
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/users/auth/login",
            "/api/users/auth/register",
            "/api/users/auth/refresh",
            "/api/users/auth/forgot-password",
            "/api/users/auth/reset-password",
            "/api/productos/public/**",
            "/api/productos/search",
            "/api/productos/categories",
            "/api/productos/brands",
            "/health",
            "/actuator/**"
    );


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            
            log.debug("Procesando request para path: {}", path);
            
            // Verificar si la ruta es pública
            if (isPublicPath(path)) {
                log.debug("Ruta pública, permitiendo acceso: {}", path);
                return chain.filter(exchange);
            }
            
            // Extraer token JWT del header Authorization
            String token = extractTokenFromRequest(request);
            
            if (!StringUtils.hasText(token)) {
                log.warn("Token JWT no encontrado para path: {}", path);
                return unauthorizedResponse(exchange, "Token JWT requerido");
            }
            
            try {
                // Validar token JWT
                if (validateToken(token)) {
                    log.debug("Token JWT válido para path: {}", path);
                    
                    // Agregar información del usuario al request
                    ServerHttpRequest modifiedRequest = request.mutate()
                            .header("X-User-Id", getUserIdFromToken(token))
                            .header("X-User-Role", getUserRoleFromToken(token))
                            .build();
                    
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } else {
                    log.warn("Token JWT inválido para path: {}", path);
                    return unauthorizedResponse(exchange, "Token JWT inválido");
                }
            } catch (Exception e) {
                log.error("Error validando token JWT: {}", e.getMessage());
                return unauthorizedResponse(exchange, "Error validando token JWT");
            }
        };
    }
    
    /**
     * Verifica si la ruta es pública.
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(publicPath -> 
            path.startsWith(publicPath) || path.matches(publicPath.replace("**", ".*"))
        );
    }
    
    /**
     * Extrae el token JWT del header Authorization.
     */
    private String extractTokenFromRequest(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        return null;
    }
    
    /**
     * Valida el token JWT.
     */
    private boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims != null && !isTokenExpired(claims);
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si el token ha expirado.
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new java.util.Date());
    }
    
    /**
     * Obtiene el ID del usuario del token.
     */
    private String getUserIdFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extrayendo user ID del token: {}", e.getMessage());
            return "unknown";
        }
    }
    
    /**
     * Obtiene el rol del usuario del token.
     */
    private String getUserRoleFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.get("role", String.class);
        } catch (Exception e) {
            log.error("Error extrayendo rol del token: {}", e.getMessage());
            return "USER";
        }
    }
    
    /**
     * Retorna una respuesta de error 401.
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        
        String body = String.format("{\"error\": \"%s\", \"status\": 401}", message);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
    
    /**
     * Configuración del filtro.
     */
    public static class Config {
        // Configuración adicional si es necesaria
    }
}
