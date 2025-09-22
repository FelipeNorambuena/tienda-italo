package com.login.user.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.login.user.domain.entity.DireccionUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para responses de direcciones de usuario.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de dirección de usuario")
public class DireccionUsuarioResponseDTO {

    @Schema(description = "ID único de la dirección", example = "1")
    private Long id;

    @Schema(description = "Tipo de dirección", example = "ENVIO")
    private DireccionUsuario.TipoDireccion tipoDireccion;

    @Schema(description = "Nombre de contacto", example = "Juan Pérez")
    private String nombreContacto;

    @Schema(description = "Calle", example = "Carrera 15")
    private String calle;

    @Schema(description = "Número", example = "23-45")
    private String numero;

    @Schema(description = "Piso", example = "3")
    private String piso;

    @Schema(description = "Departamento", example = "A")
    private String departamento;

    @Schema(description = "Ciudad", example = "Bogotá")
    private String ciudad;

    @Schema(description = "Estado o provincia", example = "Cundinamarca")
    private String estadoProvincia;

    @Schema(description = "Código postal", example = "110221")
    private String codigoPostal;

    @Schema(description = "País", example = "Colombia")
    private String pais;

    @Schema(description = "Es dirección principal", example = "true")
    private Boolean esPrincipal;

    @Schema(description = "Estado activo de la dirección", example = "true")
    private Boolean activo;

    @Schema(description = "Dirección completa formateada")
    private String direccionCompleta;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de creación", example = "2024-01-01 09:00:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha de última actualización", example = "2024-01-15 14:20:30")
    private LocalDateTime updatedAt;
}
