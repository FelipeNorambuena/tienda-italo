package com.Carrito.compras.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para un item del carrito.
 * Representa un producto específico dentro del carrito con sus detalles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un producto en el carrito")
public class ItemCarritoResponseDTO {
    
    @Schema(description = "ID único del item", example = "1")
    private Long id;
    
    @Schema(description = "ID del producto", example = "456")
    private Long productoId;
    
    @Schema(description = "Nombre del producto", example = "Laptop HP Pavilion")
    private String nombreProducto;
    
    @Schema(description = "Precio unitario en CLP", example = "599990.00")
    private BigDecimal precioUnitario;
    
    @Schema(description = "Cantidad del producto", example = "2")
    private Integer cantidad;
    
    @Schema(description = "Subtotal del item en CLP", example = "1199980.00")
    private BigDecimal subtotal;
    
    @Schema(description = "Fecha de creación del item", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;
    
    @Schema(description = "Fecha de última actualización", example = "2024-01-15T11:45:00")
    private LocalDateTime fechaActualizacion;
}
