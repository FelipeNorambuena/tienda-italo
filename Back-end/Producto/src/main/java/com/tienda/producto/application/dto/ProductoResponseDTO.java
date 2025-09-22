package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de productos.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String descripcionLarga;
    private BigDecimal precio;
    private BigDecimal precioOferta;
    private BigDecimal precioFinal;
    private BigDecimal descuento;
    private BigDecimal porcentajeDescuento;
    private Integer stock;
    private Integer stockMinimo;
    private Boolean activo;
    private Boolean destacado;
    private Boolean nuevo;
    private String sku;
    private String ean;
    private String isbn;
    private Double peso;
    private Double largo;
    private Double ancho;
    private Double alto;
    private String color;
    private String material;
    private String talla;
    private Integer vendidos;
    private Integer visualizaciones;
    private Double calificacionPromedio;
    private Integer totalCalificaciones;
    private String palabrasClave;
    private String metaTitulo;
    private String metaDescripcion;
    private String slug;
    private String nombreCompleto;
    private Boolean estaEnOferta;
    private Boolean tieneStock;
    private Boolean necesitaReposicion;
    private String estadoStock;
    private Boolean esDisponible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relaciones
    private CategoriaResponseDTO categoria;
    private MarcaResponseDTO marca;
    private List<AtributoProductoResponseDTO> atributos;
    private List<ImagenProductoResponseDTO> imagenes;
    private List<ProductoRelacionadoResponseDTO> productosRelacionados;
}
