package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de imágenes de producto.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenProductoResponseDTO {

    private Long id;
    private String url;
    private String urlCompleta;
    private String nombreArchivo;
    private String nombreCompleto;
    private String tipoMime;
    private Long tamanioBytes;
    private String tamanioFormateado;
    private Integer ancho;
    private Integer alto;
    private String dimensiones;
    private String aspectRatio;
    private String alt;
    private String titulo;
    private Boolean esPrincipal;
    private Boolean activa;
    private Integer orden;
    private String miniatura;
    private String versionGrande;
    private String versionMediana;
    private String versionPequena;
    private Boolean esImagenValida;
    private Boolean esImagenGrande;
    private Boolean esImagenPequena;
    private Boolean esImagenCuadrada;
    private Boolean esImagenHorizontal;
    private Boolean esImagenVertical;
    private String orientacion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
