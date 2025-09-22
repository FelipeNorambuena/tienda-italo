package com.login.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

/**
 * DTO para estadísticas de usuarios del sistema.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estadísticas del sistema de usuarios")
public class EstadisticasUsuarioDTO {

    @Schema(description = "Total de usuarios registrados", example = "1500")
    private Long totalUsuarios;

    @Schema(description = "Total de usuarios activos", example = "1450")
    private Long usuariosActivos;

    @Schema(description = "Total de usuarios inactivos", example = "50")
    private Long usuariosInactivos;

    @Schema(description = "Total de usuarios con email verificado", example = "1400")
    private Long usuariosVerificados;

    @Schema(description = "Total de usuarios con email no verificado", example = "100")
    private Long usuariosNoVerificados;

    @Schema(description = "Total de usuarios bloqueados actualmente", example = "5")
    private Long usuariosBloqueados;

    @Schema(description = "Distribución de usuarios por rol")
    private Map<String, Long> usuariosPorRol;

    @Schema(description = "Porcentaje de usuarios activos", example = "96.67")
    private Double porcentajeActivos;

    @Schema(description = "Porcentaje de usuarios verificados", example = "93.33")
    private Double porcentajeVerificados;

    @Schema(description = "Nuevos registros del último mes", example = "120")
    private Long nuevosRegistrosUltimoMes;

    @Schema(description = "Usuarios activos en los últimos 30 días", example = "800")
    private Long usuariosActivosUltimos30Dias;

    // Métodos auxiliares para calcular porcentajes
    public void calcularPorcentajes() {
        if (totalUsuarios > 0) {
            this.porcentajeActivos = (usuariosActivos.doubleValue() / totalUsuarios.doubleValue()) * 100;
            this.porcentajeVerificados = (usuariosVerificados.doubleValue() / totalUsuarios.doubleValue()) * 100;
        } else {
            this.porcentajeActivos = 0.0;
            this.porcentajeVerificados = 0.0;
        }
    }
}
