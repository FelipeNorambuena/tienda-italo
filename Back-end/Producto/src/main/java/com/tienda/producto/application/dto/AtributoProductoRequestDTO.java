package com.tienda.producto.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de atributos de producto.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtributoProductoRequestDTO {

    @NotBlank(message = "El nombre del atributo es obligatorio")
    @Size(max = 100, message = "El nombre del atributo no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "El valor del atributo no puede exceder 500 caracteres")
    private String valor;

    @Size(max = 50, message = "La unidad no puede exceder 50 caracteres")
    private String unidad;

    @Builder.Default
    private Boolean esVisible = true;

    @Builder.Default
    private Boolean esFiltrable = false;

    @Builder.Default
    private Integer orden = 0;

    @Size(max = 100, message = "El tipo no puede exceder 100 caracteres")
    private String tipo;

    @Size(max = 500, message = "Las opciones no pueden exceder 500 caracteres")
    private String opciones;
}
