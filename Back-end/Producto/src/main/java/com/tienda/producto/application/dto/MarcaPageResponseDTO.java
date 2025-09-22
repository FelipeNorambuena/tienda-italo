package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para respuestas paginadas de marcas.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarcaPageResponseDTO {

    private List<MarcaResponseDTO> marcas;
    private int pagina;
    private int tamanio;
    private long totalElementos;
    private int totalPaginas;
    private boolean primeraPagina;
    private boolean ultimaPagina;
    private boolean tieneSiguiente;
    private boolean tieneAnterior;
    private long numeroElementos;
    private long numeroElementosTotal;
}
