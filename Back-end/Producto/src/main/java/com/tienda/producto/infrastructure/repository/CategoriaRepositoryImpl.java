package com.tienda.producto.infrastructure.repository;

import com.tienda.producto.domain.entity.Categoria;
import com.tienda.producto.domain.repository.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del repositorio de Categoria.
 * 
 * @author Tienda Italo Team
 */
@Repository
public interface CategoriaRepositoryImpl extends JpaRepository<Categoria, Long>, CategoriaRepository {

    // Implementaciones básicas
    @Override
    default Categoria save(Categoria categoria) {
        return save(categoria);
    }

    @Override
    default Optional<Categoria> findById(Long id) {
        return findById(id);
    }

    @Override
    default List<Categoria> findAll() {
        return findAll();
    }

    @Override
    default Page<Categoria> findAll(Pageable pageable) {
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
    Optional<Categoria> findByNombre(String nombre);
    Optional<Categoria> findBySlug(String slug);
    boolean existsByNombre(String nombre);
    boolean existsBySlug(String slug);

    // Búsquedas por estado
    List<Categoria> findByActivaTrue();
    Page<Categoria> findByActivaTrue(Pageable pageable);
    List<Categoria> findByActivaFalse();
    Page<Categoria> findByActivaFalse(Pageable pageable);
    long countByActivaTrue();
    long countByActivaFalse();

    // Búsquedas por características
    List<Categoria> findByDestacadaTrue();
    Page<Categoria> findByDestacadaTrue(Pageable pageable);
    List<Categoria> findByDestacadaTrueAndActivaTrue();
    Page<Categoria> findByDestacadaTrueAndActivaTrue(Pageable pageable);

    // Búsquedas por jerarquía
    List<Categoria> findByCategoriaPadreIsNull();
    Page<Categoria> findByCategoriaPadreIsNull(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullAndActivaTrue();
    Page<Categoria> findByCategoriaPadreIsNullAndActivaTrue(Pageable pageable);
    List<Categoria> findByCategoriaPadreId(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreId(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdAndActivaTrue(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdAndActivaTrue(Long categoriaPadreId, Pageable pageable);
    long countByCategoriaPadreIsNull();
    long countByCategoriaPadreIsNullAndActivaTrue();
    long countByCategoriaPadreId(Long categoriaPadreId);
    long countByCategoriaPadreIdAndActivaTrue(Long categoriaPadreId);

    // Búsquedas por texto
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    Page<Categoria> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    List<Categoria> findByNombreContainingIgnoreCaseAndActivaTrue(String nombre);
    Page<Categoria> findByNombreContainingIgnoreCaseAndActivaTrue(String nombre, Pageable pageable);
    List<Categoria> findByDescripcionContainingIgnoreCase(String descripcion);
    Page<Categoria> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
    List<Categoria> findByDescripcionContainingIgnoreCaseAndActivaTrue(String descripcion);
    Page<Categoria> findByDescripcionContainingIgnoreCaseAndActivaTrue(String descripcion, Pageable pageable);
    List<Categoria> findByPalabrasClaveContainingIgnoreCase(String palabrasClave);
    Page<Categoria> findByPalabrasClaveContainingIgnoreCase(String palabrasClave, Pageable pageable);
    List<Categoria> findByPalabrasClaveContainingIgnoreCaseAndActivaTrue(String palabrasClave);
    Page<Categoria> findByPalabrasClaveContainingIgnoreCaseAndActivaTrue(String palabrasClave, Pageable pageable);

    // Búsquedas por orden
    List<Categoria> findByOrdenBetween(Integer ordenMin, Integer ordenMax);
    Page<Categoria> findByOrdenBetween(Integer ordenMin, Integer ordenMax, Pageable pageable);
    List<Categoria> findByOrdenBetweenAndActivaTrue(Integer ordenMin, Integer ordenMax);
    Page<Categoria> findByOrdenBetweenAndActivaTrue(Integer ordenMin, Integer ordenMax, Pageable pageable);
    List<Categoria> findByOrdenGreaterThanEqual(Integer orden);
    Page<Categoria> findByOrdenGreaterThanEqual(Integer orden, Pageable pageable);
    List<Categoria> findByOrdenGreaterThanEqualAndActivaTrue(Integer orden);
    Page<Categoria> findByOrdenGreaterThanEqualAndActivaTrue(Integer orden, Pageable pageable);
    List<Categoria> findByOrdenLessThanEqual(Integer orden);
    Page<Categoria> findByOrdenLessThanEqual(Integer orden, Pageable pageable);
    List<Categoria> findByOrdenLessThanEqualAndActivaTrue(Integer orden);
    Page<Categoria> findByOrdenLessThanEqualAndActivaTrue(Integer orden, Pageable pageable);

    // Búsquedas por fecha
    List<Categoria> findByCreatedAtAfter(LocalDateTime fecha);
    Page<Categoria> findByCreatedAtAfter(LocalDateTime fecha, Pageable pageable);
    List<Categoria> findByCreatedAtAfterAndActivaTrue(LocalDateTime fecha);
    Page<Categoria> findByCreatedAtAfterAndActivaTrue(LocalDateTime fecha, Pageable pageable);
    List<Categoria> findByUpdatedAtAfter(LocalDateTime fecha);
    Page<Categoria> findByUpdatedAtAfter(LocalDateTime fecha, Pageable pageable);
    List<Categoria> findByUpdatedAtAfterAndActivaTrue(LocalDateTime fecha);
    Page<Categoria> findByUpdatedAtAfterAndActivaTrue(LocalDateTime fecha, Pageable pageable);

    // Búsquedas por rango de fechas
    List<Categoria> findByCreatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Categoria> findByCreatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Categoria> findByCreatedAtBetweenAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Categoria> findByCreatedAtBetweenAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Categoria> findByUpdatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Categoria> findByUpdatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Categoria> findByUpdatedAtBetweenAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Categoria> findByUpdatedAtBetweenAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    // Búsquedas por orden específico
    List<Categoria> findAllByOrderByNombreAsc();
    Page<Categoria> findAllByOrderByNombreAsc(Pageable pageable);
    List<Categoria> findAllByOrderByOrdenAsc();
    Page<Categoria> findAllByOrderByOrdenAsc(Pageable pageable);
    List<Categoria> findAllByOrderByOrdenDesc();
    Page<Categoria> findAllByOrderByOrdenDesc(Pageable pageable);
    List<Categoria> findAllByOrderByCreatedAtAsc();
    Page<Categoria> findAllByOrderByCreatedAtAsc(Pageable pageable);
    List<Categoria> findAllByOrderByCreatedAtDesc();
    Page<Categoria> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Categoria> findAllByOrderByUpdatedAtAsc();
    Page<Categoria> findAllByOrderByUpdatedAtAsc(Pageable pageable);
    List<Categoria> findAllByOrderByUpdatedAtDesc();
    Page<Categoria> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    // Búsquedas de categorías activas por orden específico
    List<Categoria> findByActivaTrueOrderByNombreAsc();
    Page<Categoria> findByActivaTrueOrderByNombreAsc(Pageable pageable);
    List<Categoria> findByActivaTrueOrderByOrdenAsc();
    Page<Categoria> findByActivaTrueOrderByOrdenAsc(Pageable pageable);
    List<Categoria> findByActivaTrueOrderByOrdenDesc();
    Page<Categoria> findByActivaTrueOrderByOrdenDesc(Pageable pageable);
    List<Categoria> findByActivaTrueOrderByCreatedAtAsc();
    Page<Categoria> findByActivaTrueOrderByCreatedAtAsc(Pageable pageable);
    List<Categoria> findByActivaTrueOrderByCreatedAtDesc();
    Page<Categoria> findByActivaTrueOrderByCreatedAtDesc(Pageable pageable);
    List<Categoria> findByActivaTrueOrderByUpdatedAtAsc();
    Page<Categoria> findByActivaTrueOrderByUpdatedAtAsc(Pageable pageable);
    List<Categoria> findByActivaTrueOrderByUpdatedAtDesc();
    Page<Categoria> findByActivaTrueOrderByUpdatedAtDesc(Pageable pageable);

    // Búsquedas de categorías raíz por orden específico
    List<Categoria> findByCategoriaPadreIsNullOrderByNombreAsc();
    Page<Categoria> findByCategoriaPadreIsNullOrderByNombreAsc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullOrderByOrdenAsc();
    Page<Categoria> findByCategoriaPadreIsNullOrderByOrdenAsc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullOrderByOrdenDesc();
    Page<Categoria> findByCategoriaPadreIsNullOrderByOrdenDesc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullOrderByCreatedAtAsc();
    Page<Categoria> findByCategoriaPadreIsNullOrderByCreatedAtAsc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullOrderByCreatedAtDesc();
    Page<Categoria> findByCategoriaPadreIsNullOrderByCreatedAtDesc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullOrderByUpdatedAtAsc();
    Page<Categoria> findByCategoriaPadreIsNullOrderByUpdatedAtAsc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullOrderByUpdatedAtDesc();
    Page<Categoria> findByCategoriaPadreIsNullOrderByUpdatedAtDesc(Pageable pageable);

    // Búsquedas de categorías raíz activas por orden específico
    List<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByNombreAsc();
    Page<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByNombreAsc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByOrdenAsc();
    Page<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByOrdenAsc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByOrdenDesc();
    Page<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByOrdenDesc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByCreatedAtAsc();
    Page<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByCreatedAtAsc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByCreatedAtDesc();
    Page<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByCreatedAtDesc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByUpdatedAtAsc();
    Page<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByUpdatedAtAsc(Pageable pageable);
    List<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByUpdatedAtDesc();
    Page<Categoria> findByCategoriaPadreIsNullAndActivaTrueOrderByUpdatedAtDesc(Pageable pageable);

    // Búsquedas de subcategorías por orden específico
    List<Categoria> findByCategoriaPadreIdOrderByNombreAsc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdOrderByNombreAsc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdOrderByOrdenAsc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdOrderByOrdenAsc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdOrderByOrdenDesc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdOrderByOrdenDesc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdOrderByCreatedAtAsc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdOrderByCreatedAtAsc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdOrderByCreatedAtDesc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdOrderByCreatedAtDesc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdOrderByUpdatedAtAsc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdOrderByUpdatedAtAsc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdOrderByUpdatedAtDesc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdOrderByUpdatedAtDesc(Long categoriaPadreId, Pageable pageable);

    // Búsquedas de subcategorías activas por orden específico
    List<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByNombreAsc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByNombreAsc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByOrdenAsc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByOrdenAsc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByOrdenDesc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByOrdenDesc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByCreatedAtAsc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByCreatedAtAsc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByCreatedAtDesc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByCreatedAtDesc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByUpdatedAtAsc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByUpdatedAtAsc(Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByUpdatedAtDesc(Long categoriaPadreId);
    Page<Categoria> findByCategoriaPadreIdAndActivaTrueOrderByUpdatedAtDesc(Long categoriaPadreId, Pageable pageable);

    // Búsquedas de categorías destacadas
    List<Categoria> findTop10ByDestacadaTrueAndActivaTrueOrderByOrdenAsc();
    List<Categoria> findTop10ByDestacadaTrueAndActivaTrueOrderByCreatedAtDesc();
    List<Categoria> findTop10ByDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de categorías recientes
    List<Categoria> findTop10ByActivaTrueOrderByCreatedAtDesc();
    List<Categoria> findTop10ByActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de categorías por texto completo
    List<Categoria> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCase(String texto);
    Page<Categoria> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCase(String texto, Pageable pageable);
    List<Categoria> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivaTrue(String texto);
    Page<Categoria> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivaTrue(String texto, Pageable pageable);

    // Búsquedas de categorías por nivel (métodos ya definidos arriba)

    // Búsquedas de categorías por rango de orden (métodos ya definidos arriba)
    List<Categoria> findByOrdenBetweenAndCategoriaPadreIsNullAndActivaTrue(Integer ordenMin, Integer ordenMax);
    Page<Categoria> findByOrdenBetweenAndCategoriaPadreIsNullAndActivaTrue(Integer ordenMin, Integer ordenMax, Pageable pageable);
    List<Categoria> findByOrdenBetweenAndCategoriaPadreIdAndActivaTrue(Integer ordenMin, Integer ordenMax, Long categoriaPadreId);
    Page<Categoria> findByOrdenBetweenAndCategoriaPadreIdAndActivaTrue(Integer ordenMin, Integer ordenMax, Long categoriaPadreId, Pageable pageable);

    // Búsquedas de categorías por rango de fechas (métodos ya definidos arriba)

    // Búsquedas de categorías por múltiples criterios
    List<Categoria> findByCategoriaPadreIsNullAndDestacadaTrueAndActivaTrueOrderByOrdenAsc();
    List<Categoria> findByCategoriaPadreIdAndDestacadaTrueAndActivaTrueOrderByOrdenAsc(Long categoriaPadreId);
    List<Categoria> findByCategoriaPadreIsNullAndDestacadaTrueAndActivaTrueOrderByCreatedAtDesc();
    List<Categoria> findByCategoriaPadreIdAndDestacadaTrueAndActivaTrueOrderByCreatedAtDesc(Long categoriaPadreId);
    List<Categoria> findByCategoriaPadreIsNullAndDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc();
    List<Categoria> findByCategoriaPadreIdAndDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc(Long categoriaPadreId);

    // Búsquedas de categorías por rango de orden y destacadas
    List<Categoria> findByOrdenBetweenAndDestacadaTrueAndActivaTrue(Integer ordenMin, Integer ordenMax);
    Page<Categoria> findByOrdenBetweenAndDestacadaTrueAndActivaTrue(Integer ordenMin, Integer ordenMax, Pageable pageable);
    List<Categoria> findByOrdenBetweenAndCategoriaPadreIsNullAndDestacadaTrueAndActivaTrue(Integer ordenMin, Integer ordenMax);
    Page<Categoria> findByOrdenBetweenAndCategoriaPadreIsNullAndDestacadaTrueAndActivaTrue(Integer ordenMin, Integer ordenMax, Pageable pageable);
    List<Categoria> findByOrdenBetweenAndCategoriaPadreIdAndDestacadaTrueAndActivaTrue(Integer ordenMin, Integer ordenMax, Long categoriaPadreId);
    Page<Categoria> findByOrdenBetweenAndCategoriaPadreIdAndDestacadaTrueAndActivaTrue(Integer ordenMin, Integer ordenMax, Long categoriaPadreId, Pageable pageable);

    // Búsquedas de categorías por rango de fechas y destacadas
    List<Categoria> findByCreatedAtBetweenAndDestacadaTrueAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Categoria> findByCreatedAtBetweenAndDestacadaTrueAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Categoria> findByUpdatedAtBetweenAndDestacadaTrueAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Categoria> findByUpdatedAtBetweenAndDestacadaTrueAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    // Búsquedas de categorías por rango de fechas y padre
    List<Categoria> findByCreatedAtBetweenAndCategoriaPadreIsNullAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Categoria> findByCreatedAtBetweenAndCategoriaPadreIsNullAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Categoria> findByCreatedAtBetweenAndCategoriaPadreIdAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long categoriaPadreId);
    Page<Categoria> findByCreatedAtBetweenAndCategoriaPadreIdAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long categoriaPadreId, Pageable pageable);
    List<Categoria> findByUpdatedAtBetweenAndCategoriaPadreIsNullAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Categoria> findByUpdatedAtBetweenAndCategoriaPadreIsNullAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Categoria> findByUpdatedAtBetweenAndCategoriaPadreIdAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long categoriaPadreId);
    Page<Categoria> findByUpdatedAtBetweenAndCategoriaPadreIdAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long categoriaPadreId, Pageable pageable);
}
