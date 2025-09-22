package com.login.user.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtro de autenticación JWT que intercepta las peticiones HTTP
 * y valida los tokens de acceso.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String authHeader = request.getHeader("Authorization");
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.isValidToken(token) && jwtUtil.isAccessToken(token)) {
                    String username = jwtUtil.getUsernameFromToken(token);
                    List<String> roles = jwtUtil.getRolesFromToken(token);
                    
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    
                    UserDetails userDetails = User.builder()
                            .username(username)
                            .password("") // No necesitamos la contraseña aquí
                            .authorities(authorities)
                            .build();
                    
                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, authorities);
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("Usuario autenticado: {} con roles: {}", username, roles);
                } else {
                    log.warn("Token JWT inválido o expirado");
                }
            }
        } catch (Exception e) {
            log.error("Error en el filtro de autenticación JWT: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // Rutas que no requieren autenticación JWT
        return path.startsWith("/api/users/auth/") ||
               path.startsWith("/api/users/public/") ||
               path.equals("/api/users/register") ||
               path.equals("/api/users/login") ||
               path.equals("/api/users/forgot-password") ||
               path.equals("/api/users/reset-password") ||
               path.equals("/api/users/verify-email") ||
               path.startsWith("/api-docs") ||
               path.startsWith("/swagger-ui") ||
               path.equals("/api/users/health") ||
               path.startsWith("/actuator/");
    }
}
