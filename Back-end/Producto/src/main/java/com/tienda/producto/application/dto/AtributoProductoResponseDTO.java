package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de atributos de producto.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtributoProductoResponseDTO {

    private Long id;
    private String nombre;
    private String valor;
    private String unidad;
    private String valorCompleto;
    private String nombreCompleto;
    private Boolean esVisible;
    private Boolean esFiltrable;
    private Integer orden;
    private String tipo;
    private String[] opcionesLista;
    private Boolean tieneOpciones;
    private Boolean puedeFiltrarse;
    private Boolean esNumerico;
    private Boolean esTexto;
    private Boolean esBooleano;
    private Boolean esLista;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
