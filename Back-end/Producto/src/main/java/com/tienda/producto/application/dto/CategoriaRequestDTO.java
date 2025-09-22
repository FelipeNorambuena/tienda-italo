package com.tienda.producto.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de categorías.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 200, message = "La imagen no puede exceder 200 caracteres")
    private String imagen;

    @Size(max = 200, message = "El icono no puede exceder 200 caracteres")
    private String icono;

    @Builder.Default
    private Boolean activa = true;

    @Builder.Default
    private Boolean destacada = false;

    @Builder.Default
    private Integer orden = 0;

    @Size(max = 200, message = "El slug no puede exceder 200 caracteres")
    private String slug;

    @Size(max = 200, message = "El meta título no puede exceder 200 caracteres")
    private String metaTitulo;

    @Size(max = 500, message = "La meta descripción no puede exceder 500 caracteres")
    private String metaDescripcion;

    @Size(max = 500, message = "Las palabras clave no pueden exceder 500 caracteres")
    private String palabrasClave;

    private Long categoriaPadreId;
}
