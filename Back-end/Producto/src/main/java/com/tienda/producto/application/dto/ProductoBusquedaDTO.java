package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para búsquedas de productos.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoBusquedaDTO {

    private String query;
    private List<Long> categoriaIds;
    private List<Long> marcaIds;
    private BigDecimal precioMin;
    private BigDecimal precioMax;
    private Boolean enOferta;
    private Boolean destacado;
    private Boolean nuevo;
    private Integer stockMin;
    private Integer stockMax;
    private Double calificacionMin;
    private String color;
    private String material;
    private String talla;
    private String ordenarPor;
    private String direccionOrden;
    private Integer pagina;
    private Integer tamanio;
}
