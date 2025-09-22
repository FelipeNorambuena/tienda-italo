package com.tienda.producto.infrastructure.repository;

import com.tienda.producto.domain.entity.Producto;
import com.tienda.producto.domain.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del repositorio de Producto.
 * 
 * @author Tienda Italo Team
 */
@Repository
public interface ProductoRepositoryImpl extends JpaRepository<Producto, Long>, ProductoRepository {

    // Implementaciones básicas
    @Override
    default Producto save(Producto producto) {
        return save(producto);
    }

    @Override
    default Optional<Producto> findById(Long id) {
        return findById(id);
    }

    @Override
    default List<Producto> findAll() {
        return findAll();
    }

    @Override
    default Page<Producto> findAll(Pageable pageable) {
        return findAll(pageable);
    }

    @Override
    default void deleteById(Long id) {
        deleteById(id);
    }

    @Override
    default boolean existsById(Long id) {
        return existsById(id);
    }

    @Override
    default long count() {
        return count();
    }

    // Búsquedas por atributos específicos
    Optional<Producto> findByCodigo(String codigo);
    Optional<Producto> findBySku(String sku);
    Optional<Producto> findByEan(String ean);
    Optional<Producto> findByIsbn(String isbn);
    Optional<Producto> findBySlug(String slug);
    boolean existsByCodigo(String codigo);
    boolean existsBySku(String sku);
    boolean existsByEan(String ean);
    boolean existsByIsbn(String isbn);
    boolean existsBySlug(String slug);

    // Búsquedas por estado
    List<Producto> findByActivoTrue();
    Page<Producto> findByActivoTrue(Pageable pageable);
    List<Producto> findByActivoFalse();
    Page<Producto> findByActivoFalse(Pageable pageable);
    long countByActivoTrue();
    long countByActivoFalse();

    // Búsquedas por características
    List<Producto> findByDestacadoTrue();
    Page<Producto> findByDestacadoTrue(Pageable pageable);
    List<Producto> findByNuevoTrue();
    Page<Producto> findByNuevoTrue(Pageable pageable);
    List<Producto> findByDestacadoTrueAndActivoTrue();
    Page<Producto> findByDestacadoTrueAndActivoTrue(Pageable pageable);
    List<Producto> findByNuevoTrueAndActivoTrue();
    Page<Producto> findByNuevoTrueAndActivoTrue(Pageable pageable);

    // Búsquedas por stock
    List<Producto> findByStockGreaterThan(Integer stock);
    List<Producto> findByStockLessThanEqual(Integer stock);
    List<Producto> findByStockGreaterThanAndActivoTrue(Integer stock);
    List<Producto> findByStockLessThanEqualAndActivoTrue(Integer stock);
    List<Producto> findByStockMinimoGreaterThanEqual(Integer stockMinimo);
    List<Producto> findByStockMinimoGreaterThanEqualAndActivoTrue(Integer stockMinimo);

    // Búsquedas por precio
    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);
    Page<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax, Pageable pageable);
    List<Producto> findByPrecioBetweenAndActivoTrue(BigDecimal precioMin, BigDecimal precioMax);
    Page<Producto> findByPrecioBetweenAndActivoTrue(BigDecimal precioMin, BigDecimal precioMax, Pageable pageable);
    List<Producto> findByPrecioOfertaIsNotNull();
    Page<Producto> findByPrecioOfertaIsNotNull(Pageable pageable);
    List<Producto> findByPrecioOfertaIsNotNullAndActivoTrue();
    Page<Producto> findByPrecioOfertaIsNotNullAndActivoTrue(Pageable pageable);

    // Búsquedas por categoría
    List<Producto> findByCategoriaId(Long categoriaId);
    Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable);
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
    Page<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId, Pageable pageable);
    long countByCategoriaId(Long categoriaId);
    long countByCategoriaIdAndActivoTrue(Long categoriaId);

    // Búsquedas por marca
    List<Producto> findByMarcaId(Long marcaId);
    Page<Producto> findByMarcaId(Long marcaId, Pageable pageable);
    List<Producto> findByMarcaIdAndActivoTrue(Long marcaId);
    Page<Producto> findByMarcaIdAndActivoTrue(Long marcaId, Pageable pageable);
    long countByMarcaId(Long marcaId);
    long countByMarcaIdAndActivoTrue(Long marcaId);

    // Búsquedas por texto
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    Page<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);
    List<Producto> findByDescripcionContainingIgnoreCase(String descripcion);
    Page<Producto> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
    List<Producto> findByDescripcionContainingIgnoreCaseAndActivoTrue(String descripcion);
    Page<Producto> findByDescripcionContainingIgnoreCaseAndActivoTrue(String descripcion, Pageable pageable);
    List<Producto> findByPalabrasClaveContainingIgnoreCase(String palabrasClave);
    Page<Producto> findByPalabrasClaveContainingIgnoreCase(String palabrasClave, Pageable pageable);
    List<Producto> findByPalabrasClaveContainingIgnoreCaseAndActivoTrue(String palabrasClave);
    Page<Producto> findByPalabrasClaveContainingIgnoreCaseAndActivoTrue(String palabrasClave, Pageable pageable);

    // Búsquedas por calificación
    List<Producto> findByCalificacionPromedioGreaterThanEqual(Double calificacion);
    Page<Producto> findByCalificacionPromedioGreaterThanEqual(Double calificacion, Pageable pageable);
    List<Producto> findByCalificacionPromedioGreaterThanEqualAndActivoTrue(Double calificacion);
    Page<Producto> findByCalificacionPromedioGreaterThanEqualAndActivoTrue(Double calificacion, Pageable pageable);
    List<Producto> findByTotalCalificacionesGreaterThanEqual(Integer totalCalificaciones);
    Page<Producto> findByTotalCalificacionesGreaterThanEqual(Integer totalCalificaciones, Pageable pageable);
    List<Producto> findByTotalCalificacionesGreaterThanEqualAndActivoTrue(Integer totalCalificaciones);
    Page<Producto> findByTotalCalificacionesGreaterThanEqualAndActivoTrue(Integer totalCalificaciones, Pageable pageable);

    // Búsquedas por ventas
    List<Producto> findByVendidosGreaterThanEqual(Integer vendidos);
    Page<Producto> findByVendidosGreaterThanEqual(Integer vendidos, Pageable pageable);
    List<Producto> findByVendidosGreaterThanEqualAndActivoTrue(Integer vendidos);
    Page<Producto> findByVendidosGreaterThanEqualAndActivoTrue(Integer vendidos, Pageable pageable);
    List<Producto> findByVendidosGreaterThanEqualOrderByVendidosDesc(Integer vendidos);
    Page<Producto> findByVendidosGreaterThanEqualOrderByVendidosDesc(Integer vendidos, Pageable pageable);
    List<Producto> findByVendidosGreaterThanEqualAndActivoTrueOrderByVendidosDesc(Integer vendidos);
    Page<Producto> findByVendidosGreaterThanEqualAndActivoTrueOrderByVendidosDesc(Integer vendidos, Pageable pageable);

    // Búsquedas por visualizaciones
    List<Producto> findByVisualizacionesGreaterThanEqual(Integer visualizaciones);
    Page<Producto> findByVisualizacionesGreaterThanEqual(Integer visualizaciones, Pageable pageable);
    List<Producto> findByVisualizacionesGreaterThanEqualAndActivoTrue(Integer visualizaciones);
    Page<Producto> findByVisualizacionesGreaterThanEqualAndActivoTrue(Integer visualizaciones, Pageable pageable);
    List<Producto> findByVisualizacionesGreaterThanEqualOrderByVisualizacionesDesc(Integer visualizaciones);
    Page<Producto> findByVisualizacionesGreaterThanEqualOrderByVisualizacionesDesc(Integer visualizaciones, Pageable pageable);
    List<Producto> findByVisualizacionesGreaterThanEqualAndActivoTrueOrderByVisualizacionesDesc(Integer visualizaciones);
    Page<Producto> findByVisualizacionesGreaterThanEqualAndActivoTrueOrderByVisualizacionesDesc(Integer visualizaciones, Pageable pageable);

    // Búsquedas por atributos físicos
    List<Producto> findByColor(String color);
    Page<Producto> findByColor(String color, Pageable pageable);
    List<Producto> findByColorAndActivoTrue(String color);
    Page<Producto> findByColorAndActivoTrue(String color, Pageable pageable);
    List<Producto> findByMaterial(String material);
    Page<Producto> findByMaterial(String material, Pageable pageable);
    List<Producto> findByMaterialAndActivoTrue(String material);
    Page<Producto> findByMaterialAndActivoTrue(String material, Pageable pageable);
    List<Producto> findByTalla(String talla);
    Page<Producto> findByTalla(String talla, Pageable pageable);
    List<Producto> findByTallaAndActivoTrue(String talla);
    Page<Producto> findByTallaAndActivoTrue(String talla, Pageable pageable);

    // Búsquedas por peso
    List<Producto> findByPesoBetween(Double pesoMin, Double pesoMax);
    Page<Producto> findByPesoBetween(Double pesoMin, Double pesoMax, Pageable pageable);
    List<Producto> findByPesoBetweenAndActivoTrue(Double pesoMin, Double pesoMax);
    Page<Producto> findByPesoBetweenAndActivoTrue(Double pesoMin, Double pesoMax, Pageable pageable);

    // Búsquedas por dimensiones
    List<Producto> findByLargoBetween(Double largoMin, Double largoMax);
    Page<Producto> findByLargoBetween(Double largoMin, Double largoMax, Pageable pageable);
    List<Producto> findByLargoBetweenAndActivoTrue(Double largoMin, Double largoMax);
    Page<Producto> findByLargoBetweenAndActivoTrue(Double largoMin, Double largoMax, Pageable pageable);
    List<Producto> findByAnchoBetween(Double anchoMin, Double anchoMax);
    Page<Producto> findByAnchoBetween(Double anchoMin, Double anchoMax, Pageable pageable);
    List<Producto> findByAnchoBetweenAndActivoTrue(Double anchoMin, Double anchoMax);
    Page<Producto> findByAnchoBetweenAndActivoTrue(Double anchoMin, Double anchoMax, Pageable pageable);
    List<Producto> findByAltoBetween(Double altoMin, Double altoMax);
    Page<Producto> findByAltoBetween(Double altoMin, Double altoMax, Pageable pageable);
    List<Producto> findByAltoBetweenAndActivoTrue(Double altoMin, Double altoMax);
    Page<Producto> findByAltoBetweenAndActivoTrue(Double altoMin, Double altoMax, Pageable pageable);

    // Búsquedas por fecha
    List<Producto> findByCreatedAtAfter(LocalDateTime fecha);
    Page<Producto> findByCreatedAtAfter(LocalDateTime fecha, Pageable pageable);
    List<Producto> findByCreatedAtAfterAndActivoTrue(LocalDateTime fecha);
    Page<Producto> findByCreatedAtAfterAndActivoTrue(LocalDateTime fecha, Pageable pageable);
    List<Producto> findByUpdatedAtAfter(LocalDateTime fecha);
    Page<Producto> findByUpdatedAtAfter(LocalDateTime fecha, Pageable pageable);
    List<Producto> findByUpdatedAtAfterAndActivoTrue(LocalDateTime fecha);
    Page<Producto> findByUpdatedAtAfterAndActivoTrue(LocalDateTime fecha, Pageable pageable);

    // Búsquedas complejas
    List<Producto> findByCategoriaIdAndMarcaIdAndActivoTrue(Long categoriaId, Long marcaId);
    Page<Producto> findByCategoriaIdAndMarcaIdAndActivoTrue(Long categoriaId, Long marcaId, Pageable pageable);
    List<Producto> findByCategoriaIdAndPrecioBetweenAndActivoTrue(Long categoriaId, BigDecimal precioMin, BigDecimal precioMax);
    Page<Producto> findByCategoriaIdAndPrecioBetweenAndActivoTrue(Long categoriaId, BigDecimal precioMin, BigDecimal precioMax, Pageable pageable);
    List<Producto> findByMarcaIdAndPrecioBetweenAndActivoTrue(Long marcaId, BigDecimal precioMin, BigDecimal precioMax);
    Page<Producto> findByMarcaIdAndPrecioBetweenAndActivoTrue(Long marcaId, BigDecimal precioMin, BigDecimal precioMax, Pageable pageable);

    // Búsquedas por productos relacionados
    @Query("SELECT pr.productoRelacionado FROM ProductoRelacionado pr WHERE pr.productoOrigen.id = :productoId")
    List<Producto> findProductosRelacionados(@Param("productoId") Long productoId);
    
    @Query("SELECT pr.productoRelacionado FROM ProductoRelacionado pr WHERE pr.productoOrigen.id = :productoId")
    Page<Producto> findProductosRelacionados(@Param("productoId") Long productoId, Pageable pageable);
    
    @Query("SELECT pr.productoRelacionado FROM ProductoRelacionado pr WHERE pr.productoOrigen.id = :productoId AND pr.productoRelacionado.activo = true")
    List<Producto> findProductosRelacionadosActivos(@Param("productoId") Long productoId);
    
    @Query("SELECT pr.productoRelacionado FROM ProductoRelacionado pr WHERE pr.productoOrigen.id = :productoId AND pr.productoRelacionado.activo = true")
    Page<Producto> findProductosRelacionadosActivos(@Param("productoId") Long productoId, Pageable pageable);

    // Búsquedas por atributos específicos
    @Query("SELECT p FROM Producto p JOIN p.atributos a WHERE a.nombre = :nombreAtributo AND a.valor = :valorAtributo")
    List<Producto> findByAtributosNombreAndAtributosValor(@Param("nombreAtributo") String nombreAtributo, @Param("valorAtributo") String valorAtributo);
    
    @Query("SELECT p FROM Producto p JOIN p.atributos a WHERE a.nombre = :nombreAtributo AND a.valor = :valorAtributo")
    Page<Producto> findByAtributosNombreAndAtributosValor(@Param("nombreAtributo") String nombreAtributo, @Param("valorAtributo") String valorAtributo, Pageable pageable);
    
    @Query("SELECT p FROM Producto p JOIN p.atributos a WHERE a.nombre = :nombreAtributo AND a.valor = :valorAtributo AND p.activo = true")
    List<Producto> findByAtributosNombreAndAtributosValorAndActivoTrue(@Param("nombreAtributo") String nombreAtributo, @Param("valorAtributo") String valorAtributo);
    
    @Query("SELECT p FROM Producto p JOIN p.atributos a WHERE a.nombre = :nombreAtributo AND a.valor = :valorAtributo AND p.activo = true")
    Page<Producto> findByAtributosNombreAndAtributosValorAndActivoTrue(@Param("nombreAtributo") String nombreAtributo, @Param("valorAtributo") String valorAtributo, Pageable pageable);

    // Búsquedas por imágenes
    List<Producto> findByImagenesIsNotEmpty();
    Page<Producto> findByImagenesIsNotEmpty(Pageable pageable);
    List<Producto> findByImagenesIsNotEmptyAndActivoTrue();
    Page<Producto> findByImagenesIsNotEmptyAndActivoTrue(Pageable pageable);
    List<Producto> findByImagenesEsPrincipalTrue();
    Page<Producto> findByImagenesEsPrincipalTrue(Pageable pageable);
    List<Producto> findByImagenesEsPrincipalTrueAndActivoTrue();
    Page<Producto> findByImagenesEsPrincipalTrueAndActivoTrue(Pageable pageable);

    // Búsquedas de productos más vendidos
    List<Producto> findTop10ByOrderByVendidosDesc();
    List<Producto> findTop10ByActivoTrueOrderByVendidosDesc();
    List<Producto> findTop10ByCategoriaIdAndActivoTrueOrderByVendidosDesc(Long categoriaId);
    List<Producto> findTop10ByMarcaIdAndActivoTrueOrderByVendidosDesc(Long marcaId);

    // Búsquedas de productos más vistos
    List<Producto> findTop10ByOrderByVisualizacionesDesc();
    List<Producto> findTop10ByActivoTrueOrderByVisualizacionesDesc();
    List<Producto> findTop10ByCategoriaIdAndActivoTrueOrderByVisualizacionesDesc(Long categoriaId);
    List<Producto> findTop10ByMarcaIdAndActivoTrueOrderByVisualizacionesDesc(Long marcaId);

    // Búsquedas de productos mejor calificados
    List<Producto> findTop10ByOrderByCalificacionPromedioDesc();
    List<Producto> findTop10ByActivoTrueOrderByCalificacionPromedioDesc();
    List<Producto> findTop10ByCategoriaIdAndActivoTrueOrderByCalificacionPromedioDesc(Long categoriaId);
    List<Producto> findTop10ByMarcaIdAndActivoTrueOrderByCalificacionPromedioDesc(Long marcaId);

    // Búsquedas de productos nuevos
    List<Producto> findTop10ByNuevoTrueAndActivoTrueOrderByCreatedAtDesc();
    List<Producto> findTop10ByCategoriaIdAndNuevoTrueAndActivoTrueOrderByCreatedAtDesc(Long categoriaId);
    List<Producto> findTop10ByMarcaIdAndNuevoTrueAndActivoTrueOrderByCreatedAtDesc(Long marcaId);

    // Búsquedas de productos en oferta
    List<Producto> findTop10ByPrecioOfertaIsNotNullAndActivoTrueOrderByPrecioOfertaAsc();
    List<Producto> findTop10ByCategoriaIdAndPrecioOfertaIsNotNullAndActivoTrueOrderByPrecioOfertaAsc(Long categoriaId);
    List<Producto> findTop10ByMarcaIdAndPrecioOfertaIsNotNullAndActivoTrueOrderByPrecioOfertaAsc(Long marcaId);

    // Búsquedas de productos con stock bajo (métodos ya definidos arriba)
    List<Producto> findByStockBetweenAndActivoTrue(Integer stockMin, Integer stockMax);

    // Búsquedas de productos sin stock
    List<Producto> findByStockEqualsAndActivoTrue(Integer stock);
    Page<Producto> findByStockEqualsAndActivoTrue(Integer stock, Pageable pageable);
    List<Producto> findByStockEqualsAndActivoTrueOrderByCreatedAtDesc(Integer stock);
    Page<Producto> findByStockEqualsAndActivoTrueOrderByCreatedAtDesc(Integer stock, Pageable pageable);

    // Búsquedas de productos por rango de fechas
    List<Producto> findByCreatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Producto> findByCreatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Producto> findByCreatedAtBetweenAndActivoTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Producto> findByCreatedAtBetweenAndActivoTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Producto> findByUpdatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Producto> findByUpdatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Producto> findByUpdatedAtBetweenAndActivoTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Producto> findByUpdatedAtBetweenAndActivoTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    // Búsquedas de productos por múltiples criterios
    List<Producto> findByCategoriaIdAndMarcaIdAndPrecioBetweenAndActivoTrue(Long categoriaId, Long marcaId, BigDecimal precioMin, BigDecimal precioMax);
    Page<Producto> findByCategoriaIdAndMarcaIdAndPrecioBetweenAndActivoTrue(Long categoriaId, Long marcaId, BigDecimal precioMin, BigDecimal precioMax, Pageable pageable);
    List<Producto> findByCategoriaIdAndPrecioBetweenAndStockGreaterThanAndActivoTrue(Long categoriaId, BigDecimal precioMin, BigDecimal precioMax, Integer stock);
    Page<Producto> findByCategoriaIdAndPrecioBetweenAndStockGreaterThanAndActivoTrue(Long categoriaId, BigDecimal precioMin, BigDecimal precioMax, Integer stock, Pageable pageable);
    List<Producto> findByMarcaIdAndPrecioBetweenAndStockGreaterThanAndActivoTrue(Long marcaId, BigDecimal precioMin, BigDecimal precioMax, Integer stock);
    Page<Producto> findByMarcaIdAndPrecioBetweenAndStockGreaterThanAndActivoTrue(Long marcaId, BigDecimal precioMin, BigDecimal precioMax, Integer stock, Pageable pageable);

    // Búsquedas de productos por texto completo
    List<Producto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCase(String texto);
    Page<Producto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCase(String texto, Pageable pageable);
    List<Producto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivoTrue(String texto);
    Page<Producto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivoTrue(String texto, Pageable pageable);

    // Búsquedas de productos por orden específico
    List<Producto> findAllByOrderByNombreAsc();
    Page<Producto> findAllByOrderByNombreAsc(Pageable pageable);
    List<Producto> findAllByOrderByPrecioAsc();
    Page<Producto> findAllByOrderByPrecioAsc(Pageable pageable);
    List<Producto> findAllByOrderByPrecioDesc();
    Page<Producto> findAllByOrderByPrecioDesc(Pageable pageable);
    List<Producto> findAllByOrderByCreatedAtDesc();
    Page<Producto> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Producto> findAllByOrderByUpdatedAtDesc();
    Page<Producto> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    List<Producto> findAllByOrderByVendidosDesc();
    Page<Producto> findAllByOrderByVendidosDesc(Pageable pageable);
    List<Producto> findAllByOrderByVisualizacionesDesc();
    Page<Producto> findAllByOrderByVisualizacionesDesc(Pageable pageable);
    List<Producto> findAllByOrderByCalificacionPromedioDesc();
    Page<Producto> findAllByOrderByCalificacionPromedioDesc(Pageable pageable);

    // Búsquedas de productos activos por orden específico
    List<Producto> findByActivoTrueOrderByNombreAsc();
    Page<Producto> findByActivoTrueOrderByNombreAsc(Pageable pageable);
    List<Producto> findByActivoTrueOrderByPrecioAsc();
    Page<Producto> findByActivoTrueOrderByPrecioAsc(Pageable pageable);
    List<Producto> findByActivoTrueOrderByPrecioDesc();
    Page<Producto> findByActivoTrueOrderByPrecioDesc(Pageable pageable);
    List<Producto> findByActivoTrueOrderByCreatedAtDesc();
    Page<Producto> findByActivoTrueOrderByCreatedAtDesc(Pageable pageable);
    List<Producto> findByActivoTrueOrderByUpdatedAtDesc();
    Page<Producto> findByActivoTrueOrderByUpdatedAtDesc(Pageable pageable);
    List<Producto> findByActivoTrueOrderByVendidosDesc();
    Page<Producto> findByActivoTrueOrderByVendidosDesc(Pageable pageable);
    List<Producto> findByActivoTrueOrderByVisualizacionesDesc();
    Page<Producto> findByActivoTrueOrderByVisualizacionesDesc(Pageable pageable);
    List<Producto> findByActivoTrueOrderByCalificacionPromedioDesc();
    Page<Producto> findByActivoTrueOrderByCalificacionPromedioDesc(Pageable pageable);
}
