package com.login.user.integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.login.user.application.dto.LoginRequestDTO;
import com.login.user.application.dto.UsuarioRequestDTO;
import com.login.user.application.service.UsuarioService;
import com.login.user.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para seguridad JWT.
 * Verifica el flujo completo de autenticación y autorización.
 * 
 * @author Tienda Italo Team
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Tests de Integración - JWT Security")
class JwtSecurityIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Debería permitir acceso a endpoints públicos sin token")
    void deberiaPermitirAccesoEndpointsPublicosSinToken() throws Exception {
        // Health check
        mockMvc.perform(get("/api/users/health"))
                .andExpect(status().isOk());

        // Check email
        mockMvc.perform(get("/api/users/check-email")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk());

        // Swagger docs (si están habilitados)
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debería rechazar acceso a endpoints protegidos sin token")
    void deberiaRechazarAccesoEndpointsProtegidosSinToken() throws Exception {
        // Profile endpoint
        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isUnauthorized());

        // Admin endpoints
        mockMvc.perform(get("/api/users/users"))
                .andExpect(status().isUnauthorized());

        // Statistics endpoint
        mockMvc.perform(get("/api/users/statistics"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debería permitir acceso con token JWT válido")
    void deberiaPermitirAccesoConTokenValido() throws Exception {
        // Given - crear usuario y obtener token
        UsuarioRequestDTO registerRequest = UsuarioRequestDTO.builder()
                .email("jwt-test@example.com")
                .nombre("JWT")
                .apellido("Test")
                .password("JwtTest123!")
                .build();

        usuarioService.registrarUsuario(registerRequest);

        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("jwt-test@example.com")
                .password("JwtTest123!")
                .build();

        var loginResponse = usuarioService.autenticarUsuario(loginRequest);
        String accessToken = loginResponse.getAccessToken();

        // When & Then - acceder a endpoint protegido
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("jwt-test@example.com"));
    }

    @Test
    @DisplayName("Debería rechazar token JWT inválido")
    void deberiaRechazarTokenInvalido() throws Exception {
        // When & Then - token malformado
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());

        // Token expirado simulado
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.expired.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debería rechazar token sin prefijo Bearer")
    void deberiaRechazarTokenSinPrefijo() throws Exception {
        // Given
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities("ROLE_CLIENTE")
                .build();

        String token = jwtUtil.generateAccessToken(userDetails);

        // When & Then - token sin Bearer
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debería manejar header Authorization vacío")
    void deberiaManejarHeaderAuthorizationVacio() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", ""))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debería rechazar acceso a endpoints de admin sin rol")
    void deberiaRechazarAccesoAdminSinRol() throws Exception {
        // Given - usuario con rol CLIENTE
        UsuarioRequestDTO registerRequest = UsuarioRequestDTO.builder()
                .email("cliente-test@example.com")
                .nombre("Cliente")
                .apellido("Test")
                .password("ClienteTest123!")
                .build();

        usuarioService.registrarUsuario(registerRequest);

        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("cliente-test@example.com")
                .password("ClienteTest123!")
                .build();

        var loginResponse = usuarioService.autenticarUsuario(loginRequest);
        String accessToken = loginResponse.getAccessToken();

        // When & Then - intentar acceder a endpoint de admin
        mockMvc.perform(get("/api/users/users")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users/statistics")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debería permitir acceso a admin con rol correcto")
    void deberiaPermitirAccesoAdminConRol() throws Exception {
        // Given - usar usuario admin de test-data.sql
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("admin@test.com")
                .password("Admin123!")
                .build();

        var loginResponse = usuarioService.autenticarUsuario(loginRequest);
        String accessToken = loginResponse.getAccessToken();

        // When & Then
        mockMvc.perform(get("/api/users/users")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/users/statistics")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Debería validar tiempo de expiración del token")
    void deberiaValidarTiempoExpiracionToken() throws Exception {
        // Given
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities("ROLE_CLIENTE")
                .build();

        String token = jwtUtil.generateAccessToken(userDetails);

        // Verificar que el token es válido inicialmente
        assert jwtUtil.isValidToken(token);
        assert !jwtUtil.isTokenExpired(token);

        // Verificar que es un token de acceso
        assert jwtUtil.isAccessToken(token);
        assert !jwtUtil.isRefreshToken(token);
    }

    @Test
    @DisplayName("Debería manejar tokens refresh correctamente")
    void deberiaManejarTokensRefreshCorrectamente() throws Exception {
        // Given
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities("ROLE_CLIENTE")
                .build();

        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // Then
        assert jwtUtil.isValidToken(refreshToken);
        assert jwtUtil.isRefreshToken(refreshToken);
        assert !jwtUtil.isAccessToken(refreshToken);

        // Verificar que no se puede usar refresh token para endpoints protegidos
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debería extraer información correcta del token")
    void deberiaExtraerInformacionCorrectaDelToken() throws Exception {
        // Given
        UserDetails userDetails = User.builder()
                .username("extract-test@example.com")
                .password("password")
                .authorities("ROLE_CLIENTE", "ROLE_ADMIN")
                .build();

        String token = jwtUtil.generateAccessToken(userDetails);

        // When & Then
        String username = jwtUtil.getUsernameFromToken(token);
        List<String> roles = jwtUtil.getRolesFromToken(token);
        String tokenType = jwtUtil.getTokenTypeFromToken(token);

        assert username.equals("extract-test@example.com");
        assert roles.contains("ROLE_CLIENTE");
        assert roles.contains("ROLE_ADMIN");
        assert tokenType.equals("ACCESS");
    }

    @Test
    @DisplayName("Debería manejar CORS correctamente")
    void deberiaManejarCorsCorrectamente() throws Exception {
        // When & Then - verificar headers CORS
        mockMvc.perform(get("/api/users/health")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        // Preflight request
        mockMvc.perform(options("/api/users/profile")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().exists("Access-Control-Allow-Headers"));
    }

    @Test
    @DisplayName("Debería limpiar contexto de seguridad en error")
    void deberiaLimpiarContextoSeguridadEnError() throws Exception {
        // Given - token con formato correcto pero firma inválida
        String tokenInvalido = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjk5OTk5OTk5OTl9.invalid_signature";

        // When & Then
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + tokenInvalido))
                .andExpect(status().isUnauthorized());

        // Verificar que el contexto no mantiene información de usuario inválido
        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isUnauthorized());
    }
}
