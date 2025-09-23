package com.Carrito.compras.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para el carrito de compras.
 * Contiene toda la información del carrito y sus items.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa del carrito de compras")
public class CarritoResponseDTO {
    
    @Schema(description = "ID único del carrito", example = "1")
    private Long id;
    
    @Schema(description = "ID del usuario propietario del carrito", example = "123")
    private Long usuarioId;
    
    @Schema(description = "Lista de productos en el carrito")
    private List<ItemCarritoResponseDTO> items;
    
    @Schema(description = "Total del carrito en CLP", example = "1199980.00")
    private BigDecimal total;
    
    @Schema(description = "Indica si el carrito está activo", example = "true")
    private Boolean activo;
    
    @Schema(description = "Fecha de creación del carrito", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;
    
    @Schema(description = "Fecha de última actualización", example = "2024-01-15T11:45:00")
    private LocalDateTime fechaActualizacion;
    
    @Schema(description = "Cantidad total de items en el carrito", example = "3")
    private Integer cantidadTotalItems;
}
