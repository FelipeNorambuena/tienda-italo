package com.login.user.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para requests de creación y actualización de usuarios.
 * Incluye validaciones comprehensive para garantizar la calidad de los datos.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear o actualizar un usuario")
public class UsuarioRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    @Schema(description = "Email del usuario", example = "usuario@tiendaitalo.com", required = true)
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    @Schema(description = "Nombre del usuario", example = "Juan Carlos", required = true)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    @Schema(description = "Apellido del usuario", example = "Pérez García", required = true)
    private String apellido;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "La contraseña debe contener al menos: 1 minúscula, 1 mayúscula, 1 número y 1 carácter especial"
    )
    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres con mayúscula, minúscula, número y símbolo)", 
            example = "MiPassword123!", required = true)
    private String password;

    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]+$", message = "El teléfono debe tener un formato válido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Teléfono del usuario", example = "+57 300 123 4567")
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-05-15")
    private LocalDate fechaNacimiento;

    @Schema(description = "Estado activo del usuario", example = "true")
    private Boolean activo;

    @Schema(description = "Estado de verificación del email", example = "false")
    private Boolean emailVerificado;

    // Validaciones personalizadas adicionales

    /**
     * Valida que el usuario sea mayor de edad para ciertos casos
     */
    public boolean esMayorDeEdad() {
        if (fechaNacimiento == null) {
            return true; // Si no hay fecha, permitimos el registro
        }
        return fechaNacimiento.isBefore(LocalDate.now().minusYears(18));
    }

    /**
     * Obtiene el nombre completo
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Normaliza el email a minúsculas
     */
    public void normalizarDatos() {
        if (email != null) {
            this.email = email.toLowerCase().trim();
        }
        if (nombre != null) {
            this.nombre = nombre.trim();
        }
        if (apellido != null) {
            this.apellido = apellido.trim();
        }
        if (telefono != null) {
            this.telefono = telefono.trim();
        }
    }
}
