package com.login.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para requests de autenticación (login).
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de autenticación para login")
public class LoginRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    @Schema(description = "Email del usuario", example = "usuario@tiendaitalo.com", required = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 1, max = 100, message = "La contraseña no puede estar vacía")
    @Schema(description = "Contraseña del usuario", required = true)
    private String password;

    @Schema(description = "Recordar sesión por más tiempo", example = "false")
    private Boolean recordarme;

    // Método auxiliar para normalizar datos
    public void normalizarDatos() {
        if (email != null) {
            this.email = email.toLowerCase().trim();
        }
    }
}
