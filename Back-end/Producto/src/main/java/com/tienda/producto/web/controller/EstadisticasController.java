package com.tienda.producto.web.controller;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.application.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controlador REST para estadísticas de productos.
 * 
 * @author Tienda Italo Team
 */
@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Estadísticas", description = "API para estadísticas de productos")
public class EstadisticasController {

    private final ProductoService productoService;

    @GetMapping("/productos")
    @Operation(summary = "Obtener estadísticas de productos", description = "Obtiene estadísticas generales de productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseDTO<ProductoEstadisticasDTO>> obtenerEstadisticas() {
        log.debug("Obteniendo estadísticas de productos");
        
        try {
            ProductoEstadisticasDTO estadisticas = productoService.obtenerEstadisticas();
            return ResponseEntity.ok(ApiResponseDTO.success(estadisticas));
        } catch (Exception e) {
            log.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(ApiResponseDTO.error("Error al obtener estadísticas: " + e.getMessage(), 500));
        }
    }

    @GetMapping("/productos/categoria/{categoriaId}")
    @Operation(summary = "Obtener estadísticas por categoría", description = "Obtiene estadísticas de productos de una categoría específica")
    public ResponseEntity<ApiResponseDTO<ProductoEstadisticasDTO>> obtenerEstadisticasPorCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Long categoriaId) {
        log.debug("Obteniendo estadísticas por categoría: {}", categoriaId);
        
        try {
            ProductoEstadisticasDTO estadisticas = productoService.obtenerEstadisticasPorCategoria(categoriaId);
            return ResponseEntity.ok(ApiResponseDTO.success(estadisticas));
        } catch (Exception e) {
            log.error("Error al obtener estadísticas por categoría: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(ApiResponseDTO.error("Error al obtener estadísticas por categoría: " + e.getMessage(), 500));
        }
    }

    @GetMapping("/productos/marca/{marcaId}")
    @Operation(summary = "Obtener estadísticas por marca", description = "Obtiene estadísticas de productos de una marca específica")
    public ResponseEntity<ApiResponseDTO<ProductoEstadisticasDTO>> obtenerEstadisticasPorMarca(
            @Parameter(description = "ID de la marca") @PathVariable Long marcaId) {
        log.debug("Obteniendo estadísticas por marca: {}", marcaId);
        
        try {
            ProductoEstadisticasDTO estadisticas = productoService.obtenerEstadisticasPorMarca(marcaId);
            return ResponseEntity.ok(ApiResponseDTO.success(estadisticas));
        } catch (Exception e) {
            log.error("Error al obtener estadísticas por marca: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(ApiResponseDTO.error("Error al obtener estadísticas por marca: " + e.getMessage(), 500));
        }
    }

    @GetMapping("/productos/rango-fechas")
    @Operation(summary = "Obtener estadísticas por rango de fechas", description = "Obtiene estadísticas de productos en un rango de fechas")
    public ResponseEntity<ApiResponseDTO<ProductoEstadisticasDTO>> obtenerEstadisticasPorRangoFechas(
            @Parameter(description = "Fecha de inicio") @RequestParam LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin") @RequestParam LocalDateTime fechaFin) {
        log.debug("Obteniendo estadísticas por rango de fechas: {} - {}", fechaInicio, fechaFin);
        
        try {
            ProductoEstadisticasDTO estadisticas = productoService.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(estadisticas));
        } catch (Exception e) {
            log.error("Error al obtener estadísticas por rango de fechas: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(ApiResponseDTO.error("Error al obtener estadísticas por rango de fechas: " + e.getMessage(), 500));
        }
    }
}
