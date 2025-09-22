package com.login.user.integration.controller;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para AuthController.
 * Prueba el flujo completo desde HTTP hasta base de datos.
 * 
 * @author Tienda Italo Team
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Tests de Integración - Auth Controller")
class AuthControllerIntegrationTest {

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
    @DisplayName("POST /register - Debería registrar usuario exitosamente")
    void deberiaRegistrarUsuarioExitosamente() throws Exception {
        // Given
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .email("test@example.com")
                .nombre("Test")
                .apellido("User")
                .password("Test123!")
                .telefono("+57 300 123 4567")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .build();

        // When & Then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nombre").value("Test"))
                .andExpect(jsonPath("$.apellido").value("User"))
                .andExpect(jsonPath("$.nombreCompleto").value("Test User"))
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$.emailVerificado").value(false))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("POST /register - Debería fallar con email duplicado")
    void deberiaFallarConEmailDuplicado() throws Exception {
        // Given - registrar usuario primero
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .email("duplicate@example.com")
                .nombre("First")
                .apellido("User")
                .password("Test123!")
                .build();

        usuarioService.registrarUsuario(request);

        // When & Then - intentar registrar con mismo email
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("El email ya está registrado"));
    }

    @Test
    @DisplayName("POST /register - Debería fallar con datos inválidos")
    void deberiaFallarConDatosInvalidos() throws Exception {
        // Given - request con email inválido
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .email("email-invalido")
                .nombre("")
                .apellido("User")
                .password("123") // contraseña muy corta
                .build();

        // When & Then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.validationErrors").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.nombre").exists())
                .andExpect(jsonPath("$.validationErrors.password").exists());
    }

    @Test
    @DisplayName("POST /login - Debería autenticar usuario exitosamente")
    void deberiaAutenticarUsuarioExitosamente() throws Exception {
        // Given - registrar usuario primero
        UsuarioRequestDTO registerRequest = UsuarioRequestDTO.builder()
                .email("login@example.com")
                .nombre("Login")
                .apellido("User")
                .password("Login123!")
                .build();

        usuarioService.registrarUsuario(registerRequest);

        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("login@example.com")
                .password("Login123!")
                .build();

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").exists())
                .andExpect(jsonPath("$.usuario.email").value("login@example.com"))
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    @DisplayName("POST /login - Debería fallar con credenciales inválidas")
    void deberiaFallarConCredencialesInvalidas() throws Exception {
        // Given
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("noexiste@example.com")
                .password("WrongPassword")
                .build();

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    @DisplayName("GET /check-email - Debería verificar disponibilidad de email")
    void deberiaVerificarDisponibilidadEmail() throws Exception {
        // Given - email disponible
        mockMvc.perform(get("/api/users/check-email")
                        .param("email", "disponible@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.available").value(true));

        // Given - registrar usuario
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .email("ocupado@example.com")
                .nombre("Test")
                .apellido("User")
                .password("Test123!")
                .build();

        usuarioService.registrarUsuario(request);

        // When & Then - email ocupado
        mockMvc.perform(get("/api/users/check-email")
                        .param("email", "ocupado@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    @DisplayName("GET /check-email - Debería fallar con email inválido")
    void deberiaFallarConEmailInvalido() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/check-email")
                        .param("email", "email-invalido"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /forgot-password - Debería solicitar recuperación exitosamente")
    void deberiaSolicitarRecuperacionExitosamente() throws Exception {
        // Given - registrar usuario primero
        UsuarioRequestDTO registerRequest = UsuarioRequestDTO.builder()
                .email("forgot@example.com")
                .nombre("Forgot")
                .apellido("User")
                .password("Forgot123!")
                .build();

        usuarioService.registrarUsuario(registerRequest);

        // When & Then
        mockMvc.perform(post("/api/users/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"forgot@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Si el email existe, se ha enviado un enlace de recuperación"));
    }

    @Test
    @DisplayName("POST /forgot-password - Debería responder igual para email inexistente")
    void deberiaResponderIgualParaEmailInexistente() throws Exception {
        // When & Then - por seguridad, misma respuesta
        mockMvc.perform(post("/api/users/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"noexiste@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Si el email existe, se ha enviado un enlace de recuperación"));
    }

    @Test
    @DisplayName("GET /health - Debería retornar estado del servicio")
    void deberiaRetornarEstadoServicio() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("user-service"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("POST /refresh-token - Debería renovar token exitosamente")
    void deberiaRenovarTokenExitosamente() throws Exception {
        // Given - registrar y autenticar usuario
        UsuarioRequestDTO registerRequest = UsuarioRequestDTO.builder()
                .email("refresh@example.com")
                .nombre("Refresh")
                .apellido("User")
                .password("Refresh123!")
                .build();

        usuarioService.registrarUsuario(registerRequest);

        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("refresh@example.com")
                .password("Refresh123!")
                .build();

        var loginResponse = usuarioService.autenticarUsuario(loginRequest);
        String refreshToken = loginResponse.getRefreshToken();

        // When & Then
        mockMvc.perform(post("/api/users/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("POST /refresh-token - Debería fallar con token inválido")
    void deberiaFallarConTokenInvalido() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/users/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"token-invalido\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Debería manejar Content-Type incorrecto")
    void deberiaManejarContentTypeIncorrecto() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid content"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Debería manejar JSON malformado")
    void deberiaManejarJsonMalformado() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"invalid\":}"))
                .andExpect(status().isBadRequest());
    }
}
