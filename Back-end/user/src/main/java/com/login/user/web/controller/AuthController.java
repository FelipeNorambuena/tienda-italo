package com.login.user.web.controller;

import com.login.user.application.dto.*;
import com.login.user.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para endpoints de autenticación.
 * Maneja registro, login, recuperación de contraseña y verificación de email.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints para autenticación y gestión de usuarios")
public class AuthController {

    private final UsuarioService usuarioService;

    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Registra un nuevo usuario en el sistema y envía email de verificación"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Email ya registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        
        log.info("Solicitud de registro para email: {}", usuarioRequest.getEmail());
        UsuarioResponseDTO usuario = usuarioService.registrarUsuario(usuarioRequest);
        return ResponseEntity.ok(usuario);
    }

    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario y retorna tokens de acceso"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "423", description = "Usuario bloqueado")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> iniciarSesion(
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        
        log.info("Solicitud de login para email: {}", loginRequest.getEmail());
        LoginResponseDTO response = usuarioService.autenticarUsuario(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Renovar token de acceso",
        description = "Genera un nuevo token de acceso usando un refresh token válido"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token renovado exitosamente",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> renovarToken(
            @Parameter(description = "Refresh token", required = true)
            @RequestBody Map<String, String> request) {
        
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        log.info("Solicitud de renovación de token");
        LoginResponseDTO response = usuarioService.renovarToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Solicitar recuperación de contraseña",
        description = "Envía un email con token para recuperar contraseña"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email de recuperación enviado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> solicitarRecuperacionPassword(
            @Parameter(description = "Email del usuario", required = true)
            @RequestBody Map<String, String> request) {
        
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        log.info("Solicitud de recuperación de contraseña para: {}", email);
        usuarioService.solicitarRecuperacionPassword(email);
        
        return ResponseEntity.ok(Map.of(
            "message", "Si el email existe, se ha enviado un enlace de recuperación"
        ));
    }

    @Operation(
        summary = "Restablecer contraseña",
        description = "Restablece la contraseña usando un token de recuperación"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña restablecida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> restablecerPassword(
            @Parameter(description = "Token de recuperación", required = true)
            @RequestParam String token,
            @Parameter(description = "Nueva contraseña", required = true)
            @RequestBody Map<String, String> request) {
        
        String nuevaPassword = request.get("password");
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        log.info("Solicitud de restablecimiento de contraseña");
        usuarioService.restablecerPassword(token, nuevaPassword);
        
        return ResponseEntity.ok(Map.of(
            "message", "Contraseña restablecida exitosamente"
        ));
    }

    @Operation(
        summary = "Verificar email",
        description = "Verifica el email de un usuario usando un token de verificación"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email verificado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verificarEmail(
            @Parameter(description = "Token de verificación", required = true)
            @RequestParam String token) {
        
        log.info("Solicitud de verificación de email");
        usuarioService.verificarEmail(token);
        
        return ResponseEntity.ok(Map.of(
            "message", "Email verificado exitosamente"
        ));
    }

    @Operation(
        summary = "Verificar disponibilidad de email",
        description = "Verifica si un email está disponible para registro"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de verificación")
    })
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> verificarEmailDisponible(
            @Parameter(description = "Email a verificar", required = true)
            @RequestParam @Email @NotBlank String email) {
        
        boolean disponible = usuarioService.emailDisponible(email);
        return ResponseEntity.ok(Map.of("available", disponible));
    }

    @Operation(
        summary = "Health check",
        description = "Endpoint de salud del servicio"
    )
    @ApiResponse(responseCode = "200", description = "Servicio funcionando correctamente")
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "user-service",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}
