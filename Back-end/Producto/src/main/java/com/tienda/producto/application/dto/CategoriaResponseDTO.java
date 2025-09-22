package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de categorías.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private String icono;
    private Boolean activa;
    private Boolean destacada;
    private Integer orden;
    private String slug;
    private String metaTitulo;
    private String metaDescripcion;
    private String palabrasClave;
    private String nombreCompleto;
    private Boolean esCategoriaRaiz;
    private Boolean esSubcategoria;
    private Integer nivel;
    private String rutaCompleta;
    private Integer totalProductos;
    private Integer totalProductosActivos;
    private Integer totalSubcategorias;
    private Integer totalSubcategoriasActivas;
    private Boolean esVisible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relaciones
    private CategoriaResponseDTO categoriaPadre;
    private List<CategoriaResponseDTO> subcategorias;
    private List<ProductoResponseDTO> productos;
}
