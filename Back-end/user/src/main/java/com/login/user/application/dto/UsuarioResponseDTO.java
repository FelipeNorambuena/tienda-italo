package com.login.user.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para responses de usuario.
 * No incluye información sensible como contraseñas.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de usuario para respuestas de API")
public class UsuarioResponseDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Email del usuario", example = "usuario@tiendaitalo.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan Carlos")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez García")
    private String apellido;

    @Schema(description = "Nombre completo del usuario", example = "Juan Carlos Pérez García")
    private String nombreCompleto;

    @Schema(description = "Teléfono del usuario", example = "+57 300 123 4567")
    private String telefono;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-05-15")
    private LocalDate fechaNacimiento;

    @Schema(description = "Estado activo del usuario", example = "true")
    private Boolean activo;

    @Schema(description = "Estado de verificación del email", example = "true")
    private Boolean emailVerificado;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha del último acceso", example = "2024-01-15 10:30:45")
    private LocalDateTime fechaUltimoAcceso;

    @Schema(description = "Número de intentos fallidos de login", example = "0")
    private Integer intentosFallidosLogin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha hasta la cual el usuario está bloqueado")
    private LocalDateTime bloqueadoHasta;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de creación del usuario", example = "2024-01-01 09:00:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de última actualización", example = "2024-01-15 14:20:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Roles asignados al usuario")
    private List<RolResponseDTO> roles;

    @Schema(description = "Direcciones del usuario")
    private List<DireccionUsuarioResponseDTO> direcciones;

    // Campos calculados

    @Schema(description = "Indica si el usuario está bloqueado", example = "false")
    private Boolean estaBloqueado;

    @Schema(description = "Indica si la cuenta está habilitada", example = "true")
    private Boolean cuentaHabilitada;

    @Schema(description = "Edad del usuario en años", example = "34")
    private Integer edad;

    // Métodos auxiliares para campos calculados

    public Boolean getEstaBloqueado() {
        return bloqueadoHasta != null && bloqueadoHasta.isAfter(LocalDateTime.now());
    }

    public Boolean getCuentaHabilitada() {
        return activo != null && activo && emailVerificado != null && emailVerificado && !getEstaBloqueado();
    }

    public Integer getEdad() {
        if (fechaNacimiento == null) {
            return null;
        }
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    public String getNombreCompleto() {
        if (nombre == null || apellido == null) {
            return null;
        }
        return nombre + " " + apellido;
    }
}
