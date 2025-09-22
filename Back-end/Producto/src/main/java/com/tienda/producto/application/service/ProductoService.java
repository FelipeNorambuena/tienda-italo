package com.tienda.producto.application.service;

import com.tienda.producto.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para gestión de productos.
 * 
 * @author Tienda Italo Team
 */
public interface ProductoService {

    // Operaciones CRUD básicas
    ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO);
    Optional<ProductoResponseDTO> buscarProductoPorId(Long id);
    Optional<ProductoResponseDTO> buscarProductoPorCodigo(String codigo);
    Optional<ProductoResponseDTO> buscarProductoPorSku(String sku);
    Optional<ProductoResponseDTO> buscarProductoPorSlug(String slug);
    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO);
    void eliminarProducto(Long id);
    boolean existeProductoPorId(Long id);
    boolean existeProductoPorCodigo(String codigo);
    boolean existeProductoPorSku(String sku);
    boolean existeProductoPorSlug(String slug);

    // Operaciones de listado
    List<ProductoResponseDTO> listarProductos();
    Page<ProductoResponseDTO> listarProductos(Pageable pageable);
    List<ProductoResponseDTO> listarProductosActivos();
    Page<ProductoResponseDTO> listarProductosActivos(Pageable pageable);
    List<ProductoResponseDTO> listarProductosInactivos();
    Page<ProductoResponseDTO> listarProductosInactivos(Pageable pageable);

    // Operaciones de búsqueda
    List<ProductoResponseDTO> buscarProductos(ProductoBusquedaDTO busquedaDTO);
    Page<ProductoResponseDTO> buscarProductos(ProductoBusquedaDTO busquedaDTO, Pageable pageable);
    List<ProductoResponseDTO> buscarProductosPorTexto(String texto);
    Page<ProductoResponseDTO> buscarProductosPorTexto(String texto, Pageable pageable);
    List<ProductoResponseDTO> buscarProductosPorCategoria(Long categoriaId);
    Page<ProductoResponseDTO> buscarProductosPorCategoria(Long categoriaId, Pageable pageable);
    List<ProductoResponseDTO> buscarProductosPorMarca(Long marcaId);
    Page<ProductoResponseDTO> buscarProductosPorMarca(Long marcaId, Pageable pageable);

    // Operaciones de filtrado
    List<ProductoResponseDTO> filtrarProductos(ProductoFiltroDTO filtroDTO);
    Page<ProductoResponseDTO> filtrarProductos(ProductoFiltroDTO filtroDTO, Pageable pageable);
    List<ProductoResponseDTO> filtrarProductosPorPrecio(java.math.BigDecimal precioMin, java.math.BigDecimal precioMax);
    Page<ProductoResponseDTO> filtrarProductosPorPrecio(java.math.BigDecimal precioMin, java.math.BigDecimal precioMax, Pageable pageable);
    List<ProductoResponseDTO> filtrarProductosEnOferta();
    Page<ProductoResponseDTO> filtrarProductosEnOferta(Pageable pageable);
    List<ProductoResponseDTO> filtrarProductosDestacados();
    Page<ProductoResponseDTO> filtrarProductosDestacados(Pageable pageable);
    List<ProductoResponseDTO> filtrarProductosNuevos();
    Page<ProductoResponseDTO> filtrarProductosNuevos(Pageable pageable);

    // Operaciones de ordenamiento
    List<ProductoResponseDTO> listarProductosOrdenadosPorNombre();
    Page<ProductoResponseDTO> listarProductosOrdenadosPorNombre(Pageable pageable);
    List<ProductoResponseDTO> listarProductosOrdenadosPorPrecio();
    Page<ProductoResponseDTO> listarProductosOrdenadosPorPrecio(Pageable pageable);
    List<ProductoResponseDTO> listarProductosOrdenadosPorPrecioDesc();
    Page<ProductoResponseDTO> listarProductosOrdenadosPorPrecioDesc(Pageable pageable);
    List<ProductoResponseDTO> listarProductosOrdenadosPorFechaCreacion();
    Page<ProductoResponseDTO> listarProductosOrdenadosPorFechaCreacion(Pageable pageable);
    List<ProductoResponseDTO> listarProductosOrdenadosPorVendidos();
    Page<ProductoResponseDTO> listarProductosOrdenadosPorVendidos(Pageable pageable);
    List<ProductoResponseDTO> listarProductosOrdenadosPorVisualizaciones();
    Page<ProductoResponseDTO> listarProductosOrdenadosPorVisualizaciones(Pageable pageable);
    List<ProductoResponseDTO> listarProductosOrdenadosPorCalificacion();
    Page<ProductoResponseDTO> listarProductosOrdenadosPorCalificacion(Pageable pageable);

    // Operaciones de estado
    ProductoResponseDTO activarProducto(Long id);
    ProductoResponseDTO desactivarProducto(Long id);
    ProductoResponseDTO marcarComoDestacado(Long id);
    ProductoResponseDTO quitarDestacado(Long id);
    ProductoResponseDTO marcarComoNuevo(Long id);
    ProductoResponseDTO quitarNuevo(Long id);

    // Operaciones de stock
    ProductoResponseDTO actualizarStock(Long id, Integer nuevoStock);
    ProductoResponseDTO reducirStock(Long id, Integer cantidad);
    ProductoResponseDTO aumentarStock(Long id, Integer cantidad);
    List<ProductoResponseDTO> listarProductosConStockBajo();
    Page<ProductoResponseDTO> listarProductosConStockBajo(Pageable pageable);
    List<ProductoResponseDTO> listarProductosSinStock();
    Page<ProductoResponseDTO> listarProductosSinStock(Pageable pageable);
    List<ProductoResponseDTO> listarProductosConStock(Integer stockMin);
    Page<ProductoResponseDTO> listarProductosConStock(Integer stockMin, Pageable pageable);

    // Operaciones de precio
    ProductoResponseDTO actualizarPrecio(Long id, java.math.BigDecimal nuevoPrecio);
    ProductoResponseDTO actualizarPrecioOferta(Long id, java.math.BigDecimal nuevoPrecioOferta);
    ProductoResponseDTO quitarOferta(Long id);
    List<ProductoResponseDTO> listarProductosEnOferta();
    Page<ProductoResponseDTO> listarProductosEnOferta(Pageable pageable);
    List<ProductoResponseDTO> listarProductosPorRangoPrecio(java.math.BigDecimal precioMin, java.math.BigDecimal precioMax);
    Page<ProductoResponseDTO> listarProductosPorRangoPrecio(java.math.BigDecimal precioMin, java.math.BigDecimal precioMax, Pageable pageable);

    // Operaciones de atributos
    ProductoResponseDTO agregarAtributo(Long productoId, AtributoProductoRequestDTO atributoDTO);
    ProductoResponseDTO actualizarAtributo(Long productoId, Long atributoId, AtributoProductoRequestDTO atributoDTO);
    ProductoResponseDTO eliminarAtributo(Long productoId, Long atributoId);
    List<AtributoProductoResponseDTO> listarAtributos(Long productoId);

    // Operaciones de imágenes
    ProductoResponseDTO agregarImagen(Long productoId, ImagenProductoRequestDTO imagenDTO);
    ProductoResponseDTO actualizarImagen(Long productoId, Long imagenId, ImagenProductoRequestDTO imagenDTO);
    ProductoResponseDTO eliminarImagen(Long productoId, Long imagenId);
    ProductoResponseDTO marcarImagenComoPrincipal(Long productoId, Long imagenId);
    List<ImagenProductoResponseDTO> listarImagenes(Long productoId);
    ImagenProductoResponseDTO obtenerImagenPrincipal(Long productoId);
    List<ImagenProductoResponseDTO> listarImagenesSecundarias(Long productoId);

    // Operaciones de productos relacionados
    ProductoResponseDTO agregarProductoRelacionado(Long productoId, Long productoRelacionadoId, String tipoRelacion);
    ProductoResponseDTO eliminarProductoRelacionado(Long productoId, Long productoRelacionadoId);
    List<ProductoResponseDTO> listarProductosRelacionados(Long productoId);
    Page<ProductoResponseDTO> listarProductosRelacionados(Long productoId, Pageable pageable);

    // Operaciones de calificación
    ProductoResponseDTO calificarProducto(Long id, Double calificacion);
    ProductoResponseDTO actualizarCalificacion(Long id, Double nuevaCalificacion);
    List<ProductoResponseDTO> listarProductosMejorCalificados();
    Page<ProductoResponseDTO> listarProductosMejorCalificados(Pageable pageable);
    List<ProductoResponseDTO> listarProductosPorCalificacionMinima(Double calificacionMin);
    Page<ProductoResponseDTO> listarProductosPorCalificacionMinima(Double calificacionMin, Pageable pageable);

    // Operaciones de visualización
    ProductoResponseDTO incrementarVisualizaciones(Long id);
    List<ProductoResponseDTO> listarProductosMasVistos();
    Page<ProductoResponseDTO> listarProductosMasVistos(Pageable pageable);
    List<ProductoResponseDTO> listarProductosPorVisualizacionesMinimas(Integer visualizacionesMin);
    Page<ProductoResponseDTO> listarProductosPorVisualizacionesMinimas(Integer visualizacionesMin, Pageable pageable);

    // Operaciones de ventas
    ProductoResponseDTO incrementarVendidos(Long id, Integer cantidad);
    List<ProductoResponseDTO> listarProductosMasVendidos();
    Page<ProductoResponseDTO> listarProductosMasVendidos(Pageable pageable);
    List<ProductoResponseDTO> listarProductosPorVendidosMinimos(Integer vendidosMin);
    Page<ProductoResponseDTO> listarProductosPorVendidosMinimos(Integer vendidosMin, Pageable pageable);

    // Operaciones de estadísticas
    ProductoEstadisticasDTO obtenerEstadisticas();
    ProductoEstadisticasDTO obtenerEstadisticasPorCategoria(Long categoriaId);
    ProductoEstadisticasDTO obtenerEstadisticasPorMarca(Long marcaId);
    ProductoEstadisticasDTO obtenerEstadisticasPorRangoFechas(java.time.LocalDateTime fechaInicio, java.time.LocalDateTime fechaFin);

    // Operaciones de validación
    boolean validarCodigoUnico(String codigo, Long idExcluir);
    boolean validarSkuUnico(String sku, Long idExcluir);
    boolean validarEanUnico(String ean, Long idExcluir);
    boolean validarIsbnUnico(String isbn, Long idExcluir);
    boolean validarSlugUnico(String slug, Long idExcluir);

    // Operaciones de conteo
    long contarProductos();
    long contarProductosActivos();
    long contarProductosInactivos();
    long contarProductosDestacados();
    long contarProductosNuevos();
    long contarProductosEnOferta();
    long contarProductosSinStock();
    long contarProductosConStockBajo();
    long contarProductosPorCategoria(Long categoriaId);
    long contarProductosPorMarca(Long marcaId);
    long contarProductosPorPrecio(java.math.BigDecimal precioMin, java.math.BigDecimal precioMax);
    long contarProductosPorStock(Integer stockMin, Integer stockMax);
    long contarProductosPorCalificacion(Double calificacionMin);
    long contarProductosPorVendidos(Integer vendidosMin);
    long contarProductosPorVisualizaciones(Integer visualizacionesMin);
}
