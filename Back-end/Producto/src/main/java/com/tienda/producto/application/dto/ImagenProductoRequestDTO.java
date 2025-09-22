package com.tienda.producto.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de imágenes de producto.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenProductoRequestDTO {

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 500, message = "La URL no puede exceder 500 caracteres")
    private String url;

    @Size(max = 200, message = "El nombre del archivo no puede exceder 200 caracteres")
    private String nombreArchivo;

    @Size(max = 100, message = "El tipo MIME no puede exceder 100 caracteres")
    private String tipoMime;

    @NotNull(message = "El tamaño en bytes es obligatorio")
    private Long tamanioBytes;

    @NotNull(message = "El ancho es obligatorio")
    private Integer ancho;

    @NotNull(message = "El alto es obligatorio")
    private Integer alto;

    @Size(max = 200, message = "El texto alternativo no puede exceder 200 caracteres")
    private String alt;

    @Size(max = 500, message = "El título no puede exceder 500 caracteres")
    private String titulo;

    @Builder.Default
    private Boolean esPrincipal = false;

    @Builder.Default
    private Boolean activa = true;

    @Builder.Default
    private Integer orden = 0;

    @Size(max = 200, message = "La miniatura no puede exceder 200 caracteres")
    private String miniatura;

    @Size(max = 200, message = "La versión grande no puede exceder 200 caracteres")
    private String versionGrande;

    @Size(max = 200, message = "La versión mediana no puede exceder 200 caracteres")
    private String versionMediana;

    @Size(max = 200, message = "La versión pequeña no puede exceder 200 caracteres")
    private String versionPequena;
}
