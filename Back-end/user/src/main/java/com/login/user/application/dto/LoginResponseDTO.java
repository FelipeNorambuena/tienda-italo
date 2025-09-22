package com.login.user.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para responses de autenticación exitosa.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación exitosa")
public class LoginResponseDTO {

    @Schema(description = "Token de acceso JWT", required = true)
    private String accessToken;

    @Schema(description = "Token de renovación", required = true)
    private String refreshToken;

    @Schema(description = "Tipo de token", example = "Bearer", required = true)
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Tiempo de expiración del token en segundos", example = "86400")
    private Long expiresIn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de expiración del token", example = "2024-01-16 10:30:45")
    private LocalDateTime expiresAt;

    @Schema(description = "Información del usuario autenticado")
    private UsuarioResponseDTO usuario;

    @Schema(description = "Roles del usuario autenticado")
    private List<String> roles;

    @Schema(description = "Permisos del usuario autenticado")
    private List<String> permisos;

    @Schema(description = "Indica si es el primer login del usuario", example = "false")
    private Boolean primerLogin;

    @Schema(description = "Mensaje de bienvenida personalizado")
    private String mensajeBienvenida;

    // Método auxiliar para construir mensaje de bienvenida
    public void construirMensajeBienvenida() {
        if (usuario != null) {
            String nombre = usuario.getNombre() != null ? usuario.getNombre() : "Usuario";
            this.mensajeBienvenida = "¡Bienvenido a Tienda Italo, " + nombre + "!";
        }
    }
}
