package com.Carrito.compras.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para agregar un producto al carrito.
 * Incluye validaciones robustas para garantizar la integridad de los datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para agregar un producto al carrito")
public class AgregarItemRequestDTO {
    
    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser un número positivo")
    @Schema(description = "ID único del producto", example = "1", required = true)
    private Long productoId;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 1, max = 255, message = "El nombre del producto debe tener entre 1 y 255 caracteres")
    @Schema(description = "Nombre del producto", example = "Laptop HP Pavilion", required = true)
    private String nombreProducto;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio unitario debe tener máximo 8 dígitos enteros y 2 decimales")
    @Schema(description = "Precio unitario del producto en CLP", example = "599990.00", required = true)
    private BigDecimal precioUnitario;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Max(value = 999, message = "La cantidad no puede ser mayor a 999")
    @Schema(description = "Cantidad del producto", example = "2", required = true)
    private Integer cantidad;
}
