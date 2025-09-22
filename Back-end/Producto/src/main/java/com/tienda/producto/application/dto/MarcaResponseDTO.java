package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de marcas.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarcaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String logo;
    private String sitioWeb;
    private String pais;
    private Boolean activa;
    private Boolean destacada;
    private Integer orden;
    private String slug;
    private String metaTitulo;
    private String metaDescripcion;
    private String palabrasClave;
    private String nombreCompleto;
    private String paisCompleto;
    private Boolean esVisible;
    private Boolean tieneSitioWeb;
    private Boolean tieneLogo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relaciones
    private List<ProductoResponseDTO> productos;
}
