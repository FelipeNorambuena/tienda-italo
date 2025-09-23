package com.tienda.producto.web.controller;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.application.service.MarcaService;
// TEMPORALMENTE COMENTADO - Anotaciones de Swagger
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador REST para gestión de marcas.
 * 
 * @author Tienda Italo Team
 */
@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
@Slf4j
// TEMPORALMENTE COMENTADO - @Tag(name = "Marcas", description = "API para gestión de marcas")
public class MarcaController {

    private final MarcaService marcaService;

    // Operaciones CRUD básicas
    @PostMapping
    @Operation(summary = "Crear marca", description = "Crea una nueva marca en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Marca creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto - nombre/slug ya existe")
    })
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> crearMarca(
            @Valid @RequestBody MarcaRequestDTO requestDTO) {
        log.info("Creando nueva marca: {}", requestDTO.getNombre());
        
        try {
            MarcaResponseDTO marca = marcaService.crearMarca(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success(marca, "Marca creada exitosamente"));
        } catch (Exception e) {
            log.error("Error al crear marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("Error al crear marca: " + e.getMessage(), 400));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar marca por ID", description = "Obtiene una marca por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Marca encontrada"),
        @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> buscarMarcaPorId(
            @Parameter(description = "ID de la marca") @PathVariable Long id) {
        log.debug("Buscando marca por ID: {}", id);
        
        Optional<MarcaResponseDTO> marca = marcaService.buscarMarcaPorId(id);
        if (marca.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(marca.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        }
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar marca por nombre", description = "Obtiene una marca por su nombre")
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> buscarMarcaPorNombre(
            @Parameter(description = "Nombre de la marca") @PathVariable String nombre) {
        log.debug("Buscando marca por nombre: {}", nombre);
        
        Optional<MarcaResponseDTO> marca = marcaService.buscarMarcaPorNombre(nombre);
        if (marca.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(marca.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        }
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Buscar marca por slug", description = "Obtiene una marca por su slug")
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> buscarMarcaPorSlug(
            @Parameter(description = "Slug de la marca") @PathVariable String slug) {
        log.debug("Buscando marca por slug: {}", slug);
        
        Optional<MarcaResponseDTO> marca = marcaService.buscarMarcaPorSlug(slug);
        if (marca.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(marca.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca", description = "Actualiza una marca existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Marca actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> actualizarMarca(
            @Parameter(description = "ID de la marca") @PathVariable Long id,
            @Valid @RequestBody MarcaRequestDTO requestDTO) {
        log.info("Actualizando marca con ID: {}", id);
        
        try {
            MarcaResponseDTO marca = marcaService.actualizarMarca(id, requestDTO);
            return ResponseEntity.ok(ApiResponseDTO.success(marca, "Marca actualizada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al actualizar marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        } catch (Exception e) {
            log.error("Error al actualizar marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("Error al actualizar marca: " + e.getMessage(), 400));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca", description = "Elimina una marca del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Marca eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<Void>> eliminarMarca(
            @Parameter(description = "ID de la marca") @PathVariable Long id) {
        log.info("Eliminando marca con ID: {}", id);
        
        try {
            marcaService.eliminarMarca(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDTO.success(null, "Marca eliminada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al eliminar marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        }
    }

    // Operaciones de listado
    @GetMapping
    @Operation(summary = "Listar marcas", description = "Obtiene una lista de marcas con paginación")
    public ResponseEntity<ApiResponseDTO<Page<MarcaResponseDTO>>> listarMarcas(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando marcas paginadas: {}", pageable);
        
        Page<MarcaResponseDTO> marcas = marcaService.listarMarcas(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(marcas));
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar marcas activas", description = "Obtiene una lista de marcas activas")
    public ResponseEntity<ApiResponseDTO<Page<MarcaResponseDTO>>> listarMarcasActivas(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando marcas activas paginadas: {}", pageable);
        
        Page<MarcaResponseDTO> marcas = marcaService.listarMarcasActivas(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(marcas));
    }

    @GetMapping("/inactivas")
    @Operation(summary = "Listar marcas inactivas", description = "Obtiene una lista de marcas inactivas")
    public ResponseEntity<ApiResponseDTO<Page<MarcaResponseDTO>>> listarMarcasInactivas(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando marcas inactivas paginadas: {}", pageable);
        
        Page<MarcaResponseDTO> marcas = marcaService.listarMarcasInactivas(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(marcas));
    }

    // Operaciones de búsqueda
    @GetMapping("/buscar")
    @Operation(summary = "Buscar marcas", description = "Busca marcas por texto")
    public ResponseEntity<ApiResponseDTO<Page<MarcaResponseDTO>>> buscarMarcas(
            @Parameter(description = "Texto de búsqueda") @RequestParam String texto,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Buscando marcas por texto: {}", texto);
        
        Page<MarcaResponseDTO> marcas = marcaService.buscarMarcasPorTexto(texto, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(marcas));
    }

    @GetMapping("/pais/{pais}")
    @Operation(summary = "Listar marcas por país", description = "Obtiene marcas de un país específico")
    public ResponseEntity<ApiResponseDTO<Page<MarcaResponseDTO>>> buscarMarcasPorPais(
            @Parameter(description = "País de la marca") @PathVariable String pais,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Buscando marcas por país: {}", pais);
        
        Page<MarcaResponseDTO> marcas = marcaService.buscarMarcasPorPais(pais, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(marcas));
    }

    // Operaciones de estado
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar marca", description = "Activa una marca")
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> activarMarca(
            @Parameter(description = "ID de la marca") @PathVariable Long id) {
        log.info("Activando marca con ID: {}", id);
        
        try {
            MarcaResponseDTO marca = marcaService.activarMarca(id);
            return ResponseEntity.ok(ApiResponseDTO.success(marca, "Marca activada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al activar marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        }
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar marca", description = "Desactiva una marca")
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> desactivarMarca(
            @Parameter(description = "ID de la marca") @PathVariable Long id) {
        log.info("Desactivando marca con ID: {}", id);
        
        try {
            MarcaResponseDTO marca = marcaService.desactivarMarca(id);
            return ResponseEntity.ok(ApiResponseDTO.success(marca, "Marca desactivada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al desactivar marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        }
    }

    @PatchMapping("/{id}/destacada")
    @Operation(summary = "Marcar como destacada", description = "Marca una marca como destacada")
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> marcarComoDestacada(
            @Parameter(description = "ID de la marca") @PathVariable Long id) {
        log.info("Marcando marca como destacada con ID: {}", id);
        
        try {
            MarcaResponseDTO marca = marcaService.marcarComoDestacada(id);
            return ResponseEntity.ok(ApiResponseDTO.success(marca, "Marca marcada como destacada"));
        } catch (RuntimeException e) {
            log.error("Error al marcar marca como destacada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        }
    }

    @PatchMapping("/{id}/quitar-destacada")
    @Operation(summary = "Quitar destacada", description = "Quita el destacado de una marca")
    public ResponseEntity<ApiResponseDTO<MarcaResponseDTO>> quitarDestacada(
            @Parameter(description = "ID de la marca") @PathVariable Long id) {
        log.info("Quitando destacada de la marca con ID: {}", id);
        
        try {
            MarcaResponseDTO marca = marcaService.quitarDestacada(id);
            return ResponseEntity.ok(ApiResponseDTO.success(marca, "Destacada quitada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al quitar destacada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Marca no encontrada", 404));
        }
    }

    // Operaciones de conteo
    @GetMapping("/contar")
    @Operation(summary = "Contar marcas", description = "Obtiene el total de marcas")
    public ResponseEntity<ApiResponseDTO<Long>> contarMarcas() {
        log.debug("Contando marcas");
        
        long total = marcaService.contarMarcas();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/activas")
    @Operation(summary = "Contar marcas activas", description = "Obtiene el total de marcas activas")
    public ResponseEntity<ApiResponseDTO<Long>> contarMarcasActivas() {
        log.debug("Contando marcas activas");
        
        long total = marcaService.contarMarcasActivas();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/inactivas")
    @Operation(summary = "Contar marcas inactivas", description = "Obtiene el total de marcas inactivas")
    public ResponseEntity<ApiResponseDTO<Long>> contarMarcasInactivas() {
        log.debug("Contando marcas inactivas");
        
        long total = marcaService.contarMarcasInactivas();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/destacadas")
    @Operation(summary = "Contar marcas destacadas", description = "Obtiene el total de marcas destacadas")
    public ResponseEntity<ApiResponseDTO<Long>> contarMarcasDestacadas() {
        log.debug("Contando marcas destacadas");
        
        long total = marcaService.contarMarcasDestacadas();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/pais/{pais}")
    @Operation(summary = "Contar marcas por país", description = "Obtiene el total de marcas de un país")
    public ResponseEntity<ApiResponseDTO<Long>> contarMarcasPorPais(
            @Parameter(description = "País de la marca") @PathVariable String pais) {
        log.debug("Contando marcas por país: {}", pais);
        
        long total = marcaService.contarMarcasPorPais(pais);
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }
}
