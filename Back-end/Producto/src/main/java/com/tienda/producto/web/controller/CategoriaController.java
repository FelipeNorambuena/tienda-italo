package com.tienda.producto.web.controller;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.application.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestión de categorías.
 * 
 * @author Tienda Italo Team
 */
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Categorías", description = "API para gestión de categorías")
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Operaciones CRUD básicas
    @PostMapping
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto - nombre/slug ya existe")
    })
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> crearCategoria(
            @Valid @RequestBody CategoriaRequestDTO requestDTO) {
        log.info("Creando nueva categoría: {}", requestDTO.getNombre());
        
        try {
            CategoriaResponseDTO categoria = categoriaService.crearCategoria(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success(categoria, "Categoría creada exitosamente"));
        } catch (Exception e) {
            log.error("Error al crear categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("Error al crear categoría: " + e.getMessage(), 400));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoría por ID", description = "Obtiene una categoría por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> buscarCategoriaPorId(
            @Parameter(description = "ID de la categoría") @PathVariable Long id) {
        log.debug("Buscando categoría por ID: {}", id);
        
        Optional<CategoriaResponseDTO> categoria = categoriaService.buscarCategoriaPorId(id);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(categoria.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        }
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar categoría por nombre", description = "Obtiene una categoría por su nombre")
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> buscarCategoriaPorNombre(
            @Parameter(description = "Nombre de la categoría") @PathVariable String nombre) {
        log.debug("Buscando categoría por nombre: {}", nombre);
        
        Optional<CategoriaResponseDTO> categoria = categoriaService.buscarCategoriaPorNombre(nombre);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(categoria.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        }
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Buscar categoría por slug", description = "Obtiene una categoría por su slug")
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> buscarCategoriaPorSlug(
            @Parameter(description = "Slug de la categoría") @PathVariable String slug) {
        log.debug("Buscando categoría por slug: {}", slug);
        
        Optional<CategoriaResponseDTO> categoria = categoriaService.buscarCategoriaPorSlug(slug);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(categoria.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> actualizarCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO requestDTO) {
        log.info("Actualizando categoría con ID: {}", id);
        
        try {
            CategoriaResponseDTO categoria = categoriaService.actualizarCategoria(id, requestDTO);
            return ResponseEntity.ok(ApiResponseDTO.success(categoria, "Categoría actualizada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al actualizar categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        } catch (Exception e) {
            log.error("Error al actualizar categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("Error al actualizar categoría: " + e.getMessage(), 400));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "409", description = "No se puede eliminar - tiene subcategorías")
    })
    public ResponseEntity<ApiResponseDTO<Void>> eliminarCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Long id) {
        log.info("Eliminando categoría con ID: {}", id);
        
        try {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDTO.success(null, "Categoría eliminada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al eliminar categoría: {}", e.getMessage());
            if (e.getMessage().contains("subcategorías")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponseDTO.error("No se puede eliminar una categoría que tiene subcategorías", 409));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        }
    }

    // Operaciones de listado
    @GetMapping
    @Operation(summary = "Listar categorías", description = "Obtiene una lista de categorías con paginación")
    public ResponseEntity<ApiResponseDTO<Page<CategoriaResponseDTO>>> listarCategorias(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando categorías paginadas: {}", pageable);
        
        Page<CategoriaResponseDTO> categorias = categoriaService.listarCategorias(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar categorías activas", description = "Obtiene una lista de categorías activas")
    public ResponseEntity<ApiResponseDTO<Page<CategoriaResponseDTO>>> listarCategoriasActivas(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando categorías activas paginadas: {}", pageable);
        
        Page<CategoriaResponseDTO> categorias = categoriaService.listarCategoriasActivas(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    @GetMapping("/inactivas")
    @Operation(summary = "Listar categorías inactivas", description = "Obtiene una lista de categorías inactivas")
    public ResponseEntity<ApiResponseDTO<Page<CategoriaResponseDTO>>> listarCategoriasInactivas(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando categorías inactivas paginadas: {}", pageable);
        
        Page<CategoriaResponseDTO> categorias = categoriaService.listarCategoriasInactivas(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    // Operaciones de jerarquía
    @GetMapping("/raiz")
    @Operation(summary = "Listar categorías raíz", description = "Obtiene categorías que no tienen padre")
    public ResponseEntity<ApiResponseDTO<Page<CategoriaResponseDTO>>> listarCategoriasRaiz(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando categorías raíz paginadas: {}", pageable);
        
        Page<CategoriaResponseDTO> categorias = categoriaService.listarCategoriasRaiz(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    @GetMapping("/raiz/activas")
    @Operation(summary = "Listar categorías raíz activas", description = "Obtiene categorías raíz activas")
    public ResponseEntity<ApiResponseDTO<Page<CategoriaResponseDTO>>> listarCategoriasRaizActivas(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando categorías raíz activas paginadas: {}", pageable);
        
        Page<CategoriaResponseDTO> categorias = categoriaService.listarCategoriasRaizActivas(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    @GetMapping("/{categoriaPadreId}/subcategorias")
    @Operation(summary = "Listar subcategorías", description = "Obtiene subcategorías de una categoría padre")
    public ResponseEntity<ApiResponseDTO<Page<CategoriaResponseDTO>>> listarSubcategorias(
            @Parameter(description = "ID de la categoría padre") @PathVariable Long categoriaPadreId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando subcategorías de: {}", categoriaPadreId);
        
        Page<CategoriaResponseDTO> categorias = categoriaService.listarSubcategorias(categoriaPadreId, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    @GetMapping("/{categoriaPadreId}/subcategorias/activas")
    @Operation(summary = "Listar subcategorías activas", description = "Obtiene subcategorías activas de una categoría padre")
    public ResponseEntity<ApiResponseDTO<Page<CategoriaResponseDTO>>> listarSubcategoriasActivas(
            @Parameter(description = "ID de la categoría padre") @PathVariable Long categoriaPadreId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando subcategorías activas de: {}", categoriaPadreId);
        
        Page<CategoriaResponseDTO> categorias = categoriaService.listarSubcategoriasActivas(categoriaPadreId, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    // Operaciones de búsqueda
    @GetMapping("/buscar")
    @Operation(summary = "Buscar categorías", description = "Busca categorías por texto")
    public ResponseEntity<ApiResponseDTO<Page<CategoriaResponseDTO>>> buscarCategorias(
            @Parameter(description = "Texto de búsqueda") @RequestParam String texto,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Buscando categorías por texto: {}", texto);
        
        Page<CategoriaResponseDTO> categorias = categoriaService.buscarCategoriasPorTexto(texto, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    // Operaciones de estado
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar categoría", description = "Activa una categoría")
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> activarCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Long id) {
        log.info("Activando categoría con ID: {}", id);
        
        try {
            CategoriaResponseDTO categoria = categoriaService.activarCategoria(id);
            return ResponseEntity.ok(ApiResponseDTO.success(categoria, "Categoría activada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al activar categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        }
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar categoría", description = "Desactiva una categoría")
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> desactivarCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Long id) {
        log.info("Desactivando categoría con ID: {}", id);
        
        try {
            CategoriaResponseDTO categoria = categoriaService.desactivarCategoria(id);
            return ResponseEntity.ok(ApiResponseDTO.success(categoria, "Categoría desactivada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al desactivar categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        }
    }

    @PatchMapping("/{id}/destacada")
    @Operation(summary = "Marcar como destacada", description = "Marca una categoría como destacada")
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> marcarComoDestacada(
            @Parameter(description = "ID de la categoría") @PathVariable Long id) {
        log.info("Marcando categoría como destacada con ID: {}", id);
        
        try {
            CategoriaResponseDTO categoria = categoriaService.marcarComoDestacada(id);
            return ResponseEntity.ok(ApiResponseDTO.success(categoria, "Categoría marcada como destacada"));
        } catch (RuntimeException e) {
            log.error("Error al marcar categoría como destacada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        }
    }

    @PatchMapping("/{id}/quitar-destacada")
    @Operation(summary = "Quitar destacada", description = "Quita el destacado de una categoría")
    public ResponseEntity<ApiResponseDTO<CategoriaResponseDTO>> quitarDestacada(
            @Parameter(description = "ID de la categoría") @PathVariable Long id) {
        log.info("Quitando destacada de la categoría con ID: {}", id);
        
        try {
            CategoriaResponseDTO categoria = categoriaService.quitarDestacada(id);
            return ResponseEntity.ok(ApiResponseDTO.success(categoria, "Destacada quitada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al quitar destacada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Categoría no encontrada", 404));
        }
    }

    // Operaciones de conteo
    @GetMapping("/contar")
    @Operation(summary = "Contar categorías", description = "Obtiene el total de categorías")
    public ResponseEntity<ApiResponseDTO<Long>> contarCategorias() {
        log.debug("Contando categorías");
        
        long total = categoriaService.contarCategorias();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/activas")
    @Operation(summary = "Contar categorías activas", description = "Obtiene el total de categorías activas")
    public ResponseEntity<ApiResponseDTO<Long>> contarCategoriasActivas() {
        log.debug("Contando categorías activas");
        
        long total = categoriaService.contarCategoriasActivas();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/inactivas")
    @Operation(summary = "Contar categorías inactivas", description = "Obtiene el total de categorías inactivas")
    public ResponseEntity<ApiResponseDTO<Long>> contarCategoriasInactivas() {
        log.debug("Contando categorías inactivas");
        
        long total = categoriaService.contarCategoriasInactivas();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/destacadas")
    @Operation(summary = "Contar categorías destacadas", description = "Obtiene el total de categorías destacadas")
    public ResponseEntity<ApiResponseDTO<Long>> contarCategoriasDestacadas() {
        log.debug("Contando categorías destacadas");
        
        long total = categoriaService.contarCategoriasDestacadas();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/raiz")
    @Operation(summary = "Contar categorías raíz", description = "Obtiene el total de categorías raíz")
    public ResponseEntity<ApiResponseDTO<Long>> contarCategoriasRaiz() {
        log.debug("Contando categorías raíz");
        
        long total = categoriaService.contarCategoriasRaiz();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/raiz/activas")
    @Operation(summary = "Contar categorías raíz activas", description = "Obtiene el total de categorías raíz activas")
    public ResponseEntity<ApiResponseDTO<Long>> contarCategoriasRaizActivas() {
        log.debug("Contando categorías raíz activas");
        
        long total = categoriaService.contarCategoriasRaizActivas();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/{categoriaPadreId}/contar/subcategorias")
    @Operation(summary = "Contar subcategorías", description = "Obtiene el total de subcategorías de una categoría padre")
    public ResponseEntity<ApiResponseDTO<Long>> contarSubcategorias(
            @Parameter(description = "ID de la categoría padre") @PathVariable Long categoriaPadreId) {
        log.debug("Contando subcategorías de: {}", categoriaPadreId);
        
        long total = categoriaService.contarSubcategorias(categoriaPadreId);
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/{categoriaPadreId}/contar/subcategorias/activas")
    @Operation(summary = "Contar subcategorías activas", description = "Obtiene el total de subcategorías activas de una categoría padre")
    public ResponseEntity<ApiResponseDTO<Long>> contarSubcategoriasActivas(
            @Parameter(description = "ID de la categoría padre") @PathVariable Long categoriaPadreId) {
        log.debug("Contando subcategorías activas de: {}", categoriaPadreId);
        
        long total = categoriaService.contarSubcategoriasActivas(categoriaPadreId);
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }
}
