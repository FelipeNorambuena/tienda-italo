package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de productos relacionados.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRelacionadoResponseDTO {

    private Long id;
    private String tipoRelacion;
    private String tipoRelacionCompleto;
    private String descripcion;
    private String descripcionCompleta;
    private Boolean activa;
    private Integer orden;
    private Double peso;
    private String nivelRelacion;
    private String nombreRelacion;
    private Boolean esRelacionSimetrica;
    private Boolean esRelacionAsimetrica;
    private Boolean esRelacionFuerte;
    private Boolean esRelacionMedia;
    private Boolean esRelacionDebil;
    private Boolean esRelacionBidireccional;
    private Boolean esRelacionUnidireccional;
    private String tipoRelacionDescripcion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relaciones
    private ProductoResponseDTO productoOrigen;
    private ProductoResponseDTO productoRelacionado;
}
