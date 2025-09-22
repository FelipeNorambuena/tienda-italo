package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO para estadísticas de productos.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEstadisticasDTO {

    private Long totalProductos;
    private Long totalProductosActivos;
    private Long totalProductosInactivos;
    private Long totalProductosDestacados;
    private Long totalProductosNuevos;
    private Long totalProductosEnOferta;
    private Long totalProductosSinStock;
    private Long totalProductosConStockBajo;
    private Long totalCategorias;
    private Long totalMarcas;
    private BigDecimal precioPromedio;
    private BigDecimal precioMinimo;
    private BigDecimal precioMaximo;
    private Double calificacionPromedio;
    private Integer totalVendidos;
    private Integer totalVisualizaciones;
    private Map<String, Long> productosPorCategoria;
    private Map<String, Long> productosPorMarca;
    private Map<String, Long> productosPorColor;
    private Map<String, Long> productosPorMaterial;
    private Map<String, Long> productosPorTalla;
    private List<ProductoResponseDTO> productosMasVendidos;
    private List<ProductoResponseDTO> productosMasVistos;
    private List<ProductoResponseDTO> productosMejorCalificados;
    private List<ProductoResponseDTO> productosNuevos;
    private List<ProductoResponseDTO> productosEnOferta;
    private List<ProductoResponseDTO> productosConStockBajo;
    private List<ProductoResponseDTO> productosSinStock;
    private LocalDateTime fechaGeneracion;
}
