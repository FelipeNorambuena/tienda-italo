package com.tienda.producto.web.controller;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.application.service.ProductoService;
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

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Controlador REST para gestión de productos.
 * 
 * @author Tienda Italo Team
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    // Operaciones CRUD básicas
    @PostMapping
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto - código/SKU/EAN ya existe")
    })
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> crearProducto(
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        log.info("Creando nuevo producto: {}", requestDTO.getNombre());
        
        try {
            ProductoResponseDTO producto = productoService.crearProducto(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success(producto, "Producto creado exitosamente"));
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("Error al crear producto: " + e.getMessage(), 400));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID", description = "Obtiene un producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> buscarProductoPorId(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        log.debug("Buscando producto por ID: {}", id);
        
        Optional<ProductoResponseDTO> producto = productoService.buscarProductoPorId(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(producto.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar producto por código", description = "Obtiene un producto por su código")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> buscarProductoPorCodigo(
            @Parameter(description = "Código del producto") @PathVariable String codigo) {
        log.debug("Buscando producto por código: {}", codigo);
        
        Optional<ProductoResponseDTO> producto = productoService.buscarProductoPorCodigo(codigo);
        if (producto.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(producto.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Buscar producto por SKU", description = "Obtiene un producto por su SKU")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> buscarProductoPorSku(
            @Parameter(description = "SKU del producto") @PathVariable String sku) {
        log.debug("Buscando producto por SKU: {}", sku);
        
        Optional<ProductoResponseDTO> producto = productoService.buscarProductoPorSku(sku);
        if (producto.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(producto.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Buscar producto por slug", description = "Obtiene un producto por su slug")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> buscarProductoPorSlug(
            @Parameter(description = "Slug del producto") @PathVariable String slug) {
        log.debug("Buscando producto por slug: {}", slug);
        
        Optional<ProductoResponseDTO> producto = productoService.buscarProductoPorSlug(slug);
        if (producto.isPresent()) {
            return ResponseEntity.ok(ApiResponseDTO.success(producto.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> actualizarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        log.info("Actualizando producto con ID: {}", id);
        
        try {
            ProductoResponseDTO producto = productoService.actualizarProducto(id, requestDTO);
            return ResponseEntity.ok(ApiResponseDTO.success(producto, "Producto actualizado exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al actualizar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        } catch (Exception e) {
            log.error("Error al actualizar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("Error al actualizar producto: " + e.getMessage(), 400));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ApiResponseDTO<Void>> eliminarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        log.info("Eliminando producto con ID: {}", id);
        
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDTO.success(null, "Producto eliminado exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    // Operaciones de listado
    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene una lista de productos con paginación")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> listarProductos(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando productos paginados: {}", pageable);
        
        Page<ProductoResponseDTO> productos = productoService.listarProductos(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar productos activos", description = "Obtiene una lista de productos activos")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> listarProductosActivos(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando productos activos paginados: {}", pageable);
        
        Page<ProductoResponseDTO> productos = productoService.listarProductosActivos(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/inactivos")
    @Operation(summary = "Listar productos inactivos", description = "Obtiene una lista de productos inactivos")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> listarProductosInactivos(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando productos inactivos paginados: {}", pageable);
        
        Page<ProductoResponseDTO> productos = productoService.listarProductosInactivos(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    // Operaciones de búsqueda
    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos", description = "Busca productos por texto")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> buscarProductos(
            @Parameter(description = "Texto de búsqueda") @RequestParam String texto,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Buscando productos por texto: {}", texto);
        
        Page<ProductoResponseDTO> productos = productoService.buscarProductosPorTexto(texto, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar productos por categoría", description = "Obtiene productos de una categoría específica")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> buscarProductosPorCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Long categoriaId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Buscando productos por categoría: {}", categoriaId);
        
        Page<ProductoResponseDTO> productos = productoService.buscarProductosPorCategoria(categoriaId, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/marca/{marcaId}")
    @Operation(summary = "Listar productos por marca", description = "Obtiene productos de una marca específica")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> buscarProductosPorMarca(
            @Parameter(description = "ID de la marca") @PathVariable Long marcaId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Buscando productos por marca: {}", marcaId);
        
        Page<ProductoResponseDTO> productos = productoService.buscarProductosPorMarca(marcaId, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    // Operaciones de filtrado
    @GetMapping("/filtros")
    @Operation(summary = "Filtrar productos", description = "Filtra productos por múltiples criterios")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> filtrarProductos(
            @Parameter(description = "Filtros de búsqueda") ProductoFiltroDTO filtroDTO,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Filtrando productos con criterios: {}", filtroDTO);
        
        Page<ProductoResponseDTO> productos = productoService.filtrarProductos(filtroDTO, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/precio")
    @Operation(summary = "Filtrar productos por precio", description = "Filtra productos por rango de precio")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> filtrarProductosPorPrecio(
            @Parameter(description = "Precio mínimo") @RequestParam BigDecimal precioMin,
            @Parameter(description = "Precio máximo") @RequestParam BigDecimal precioMax,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Filtrando productos por precio: {} - {}", precioMin, precioMax);
        
        Page<ProductoResponseDTO> productos = productoService.filtrarProductosPorPrecio(precioMin, precioMax, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/ofertas")
    @Operation(summary = "Listar productos en oferta", description = "Obtiene productos que están en oferta")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> filtrarProductosEnOferta(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Filtrando productos en oferta");
        
        Page<ProductoResponseDTO> productos = productoService.filtrarProductosEnOferta(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/destacados")
    @Operation(summary = "Listar productos destacados", description = "Obtiene productos destacados")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> filtrarProductosDestacados(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Filtrando productos destacados");
        
        Page<ProductoResponseDTO> productos = productoService.filtrarProductosDestacados(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/nuevos")
    @Operation(summary = "Listar productos nuevos", description = "Obtiene productos nuevos")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> filtrarProductosNuevos(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Filtrando productos nuevos");
        
        Page<ProductoResponseDTO> productos = productoService.filtrarProductosNuevos(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    // Operaciones de estado
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar producto", description = "Activa un producto")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> activarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        log.info("Activando producto con ID: {}", id);
        
        try {
            ProductoResponseDTO producto = productoService.activarProducto(id);
            return ResponseEntity.ok(ApiResponseDTO.success(producto, "Producto activado exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al activar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar producto", description = "Desactiva un producto")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> desactivarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        log.info("Desactivando producto con ID: {}", id);
        
        try {
            ProductoResponseDTO producto = productoService.desactivarProducto(id);
            return ResponseEntity.ok(ApiResponseDTO.success(producto, "Producto desactivado exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al desactivar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @PatchMapping("/{id}/destacado")
    @Operation(summary = "Marcar como destacado", description = "Marca un producto como destacado")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> marcarComoDestacado(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        log.info("Marcando producto como destacado con ID: {}", id);
        
        try {
            ProductoResponseDTO producto = productoService.marcarComoDestacado(id);
            return ResponseEntity.ok(ApiResponseDTO.success(producto, "Producto marcado como destacado"));
        } catch (RuntimeException e) {
            log.error("Error al marcar producto como destacado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @PatchMapping("/{id}/quitar-destacado")
    @Operation(summary = "Quitar destacado", description = "Quita el destacado de un producto")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> quitarDestacado(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        log.info("Quitando destacado del producto con ID: {}", id);
        
        try {
            ProductoResponseDTO producto = productoService.quitarDestacado(id);
            return ResponseEntity.ok(ApiResponseDTO.success(producto, "Destacado quitado exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al quitar destacado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @PatchMapping("/{id}/nuevo")
    @Operation(summary = "Marcar como nuevo", description = "Marca un producto como nuevo")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> marcarComoNuevo(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        log.info("Marcando producto como nuevo con ID: {}", id);
        
        try {
            ProductoResponseDTO producto = productoService.marcarComoNuevo(id);
            return ResponseEntity.ok(ApiResponseDTO.success(producto, "Producto marcado como nuevo"));
        } catch (RuntimeException e) {
            log.error("Error al marcar producto como nuevo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @PatchMapping("/{id}/quitar-nuevo")
    @Operation(summary = "Quitar nuevo", description = "Quita el estado nuevo de un producto")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> quitarNuevo(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        log.info("Quitando nuevo del producto con ID: {}", id);
        
        try {
            ProductoResponseDTO producto = productoService.quitarNuevo(id);
            return ResponseEntity.ok(ApiResponseDTO.success(producto, "Estado nuevo quitado exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al quitar nuevo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    // Operaciones de stock
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock", description = "Actualiza el stock de un producto")
    public ResponseEntity<ApiResponseDTO<ProductoResponseDTO>> actualizarStock(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Parameter(description = "Nuevo stock") @RequestParam Integer nuevoStock) {
        log.info("Actualizando stock del producto {} a {}", id, nuevoStock);
        
        try {
            ProductoResponseDTO producto = productoService.actualizarStock(id, nuevoStock);
            return ResponseEntity.ok(ApiResponseDTO.success(producto, "Stock actualizado exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al actualizar stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Producto no encontrado", 404));
        }
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Listar productos con stock bajo", description = "Obtiene productos con stock bajo")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> listarProductosConStockBajo(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando productos con stock bajo");
        
        Page<ProductoResponseDTO> productos = productoService.listarProductosConStockBajo(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @GetMapping("/sin-stock")
    @Operation(summary = "Listar productos sin stock", description = "Obtiene productos sin stock")
    public ResponseEntity<ApiResponseDTO<Page<ProductoResponseDTO>>> listarProductosSinStock(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Listando productos sin stock");
        
        Page<ProductoResponseDTO> productos = productoService.listarProductosSinStock(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    // Operaciones de conteo
    @GetMapping("/contar")
    @Operation(summary = "Contar productos", description = "Obtiene el total de productos")
    public ResponseEntity<ApiResponseDTO<Long>> contarProductos() {
        log.debug("Contando productos");
        
        long total = productoService.contarProductos();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/activos")
    @Operation(summary = "Contar productos activos", description = "Obtiene el total de productos activos")
    public ResponseEntity<ApiResponseDTO<Long>> contarProductosActivos() {
        log.debug("Contando productos activos");
        
        long total = productoService.contarProductosActivos();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/inactivos")
    @Operation(summary = "Contar productos inactivos", description = "Obtiene el total de productos inactivos")
    public ResponseEntity<ApiResponseDTO<Long>> contarProductosInactivos() {
        log.debug("Contando productos inactivos");
        
        long total = productoService.contarProductosInactivos();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/destacados")
    @Operation(summary = "Contar productos destacados", description = "Obtiene el total de productos destacados")
    public ResponseEntity<ApiResponseDTO<Long>> contarProductosDestacados() {
        log.debug("Contando productos destacados");
        
        long total = productoService.contarProductosDestacados();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/nuevos")
    @Operation(summary = "Contar productos nuevos", description = "Obtiene el total de productos nuevos")
    public ResponseEntity<ApiResponseDTO<Long>> contarProductosNuevos() {
        log.debug("Contando productos nuevos");
        
        long total = productoService.contarProductosNuevos();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/ofertas")
    @Operation(summary = "Contar productos en oferta", description = "Obtiene el total de productos en oferta")
    public ResponseEntity<ApiResponseDTO<Long>> contarProductosEnOferta() {
        log.debug("Contando productos en oferta");
        
        long total = productoService.contarProductosEnOferta();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/sin-stock")
    @Operation(summary = "Contar productos sin stock", description = "Obtiene el total de productos sin stock")
    public ResponseEntity<ApiResponseDTO<Long>> contarProductosSinStock() {
        log.debug("Contando productos sin stock");
        
        long total = productoService.contarProductosSinStock();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }

    @GetMapping("/contar/stock-bajo")
    @Operation(summary = "Contar productos con stock bajo", description = "Obtiene el total de productos con stock bajo")
    public ResponseEntity<ApiResponseDTO<Long>> contarProductosConStockBajo() {
        log.debug("Contando productos con stock bajo");
        
        long total = productoService.contarProductosConStockBajo();
        return ResponseEntity.ok(ApiResponseDTO.success(total));
    }
}
