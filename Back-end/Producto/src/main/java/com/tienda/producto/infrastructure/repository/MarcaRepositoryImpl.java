package com.tienda.producto.infrastructure.repository;

import com.tienda.producto.domain.entity.Marca;
import com.tienda.producto.domain.repository.MarcaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del repositorio de Marca.
 * 
 * @author Tienda Italo Team
 */
@Repository
public interface MarcaRepositoryImpl extends JpaRepository<Marca, Long>, MarcaRepository {

    // Implementaciones básicas
    @Override
    default Marca save(Marca marca) {
        return save(marca);
    }

    @Override
    default Optional<Marca> findById(Long id) {
        return findById(id);
    }

    @Override
    default List<Marca> findAll() {
        return findAll();
    }

    @Override
    default Page<Marca> findAll(Pageable pageable) {
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
    Optional<Marca> findByNombre(String nombre);
    Optional<Marca> findBySlug(String slug);
    boolean existsByNombre(String nombre);
    boolean existsBySlug(String slug);

    // Búsquedas por estado
    List<Marca> findByActivaTrue();
    Page<Marca> findByActivaTrue(Pageable pageable);
    List<Marca> findByActivaFalse();
    Page<Marca> findByActivaFalse(Pageable pageable);
    long countByActivaTrue();
    long countByActivaFalse();

    // Búsquedas por características
    List<Marca> findByDestacadaTrue();
    Page<Marca> findByDestacadaTrue(Pageable pageable);
    List<Marca> findByDestacadaTrueAndActivaTrue();
    Page<Marca> findByDestacadaTrueAndActivaTrue(Pageable pageable);

    // Búsquedas por país
    List<Marca> findByPais(String pais);
    Page<Marca> findByPais(String pais, Pageable pageable);
    List<Marca> findByPaisAndActivaTrue(String pais);
    Page<Marca> findByPaisAndActivaTrue(String pais, Pageable pageable);
    List<Marca> findByPaisContainingIgnoreCase(String pais);
    Page<Marca> findByPaisContainingIgnoreCase(String pais, Pageable pageable);
    List<Marca> findByPaisContainingIgnoreCaseAndActivaTrue(String pais);
    Page<Marca> findByPaisContainingIgnoreCaseAndActivaTrue(String pais, Pageable pageable);

    // Búsquedas por sitio web
    List<Marca> findBySitioWebIsNotNull();
    Page<Marca> findBySitioWebIsNotNull(Pageable pageable);
    List<Marca> findBySitioWebIsNotNullAndActivaTrue();
    Page<Marca> findBySitioWebIsNotNullAndActivaTrue(Pageable pageable);
    List<Marca> findBySitioWebContainingIgnoreCase(String sitioWeb);
    Page<Marca> findBySitioWebContainingIgnoreCase(String sitioWeb, Pageable pageable);
    List<Marca> findBySitioWebContainingIgnoreCaseAndActivaTrue(String sitioWeb);
    Page<Marca> findBySitioWebContainingIgnoreCaseAndActivaTrue(String sitioWeb, Pageable pageable);

    // Búsquedas por logo
    List<Marca> findByLogoIsNotNull();
    Page<Marca> findByLogoIsNotNull(Pageable pageable);
    List<Marca> findByLogoIsNotNullAndActivaTrue();
    Page<Marca> findByLogoIsNotNullAndActivaTrue(Pageable pageable);
    List<Marca> findByLogoContainingIgnoreCase(String logo);
    Page<Marca> findByLogoContainingIgnoreCase(String logo, Pageable pageable);
    List<Marca> findByLogoContainingIgnoreCaseAndActivaTrue(String logo);
    Page<Marca> findByLogoContainingIgnoreCaseAndActivaTrue(String logo, Pageable pageable);

    // Búsquedas por orden
    List<Marca> findByOrdenBetween(Integer ordenMin, Integer ordenMax);
    Page<Marca> findByOrdenBetween(Integer ordenMin, Integer ordenMax, Pageable pageable);
    List<Marca> findByOrdenBetweenAndActivaTrue(Integer ordenMin, Integer ordenMax);
    Page<Marca> findByOrdenBetweenAndActivaTrue(Integer ordenMin, Integer ordenMax, Pageable pageable);
    List<Marca> findByOrdenGreaterThanEqual(Integer orden);
    Page<Marca> findByOrdenGreaterThanEqual(Integer orden, Pageable pageable);
    List<Marca> findByOrdenGreaterThanEqualAndActivaTrue(Integer orden);
    Page<Marca> findByOrdenGreaterThanEqualAndActivaTrue(Integer orden, Pageable pageable);
    List<Marca> findByOrdenLessThanEqual(Integer orden);
    Page<Marca> findByOrdenLessThanEqual(Integer orden, Pageable pageable);
    List<Marca> findByOrdenLessThanEqualAndActivaTrue(Integer orden);
    Page<Marca> findByOrdenLessThanEqualAndActivaTrue(Integer orden, Pageable pageable);

    // Búsquedas por texto
    List<Marca> findByNombreContainingIgnoreCase(String nombre);
    Page<Marca> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    List<Marca> findByNombreContainingIgnoreCaseAndActivaTrue(String nombre);
    Page<Marca> findByNombreContainingIgnoreCaseAndActivaTrue(String nombre, Pageable pageable);
    List<Marca> findByDescripcionContainingIgnoreCase(String descripcion);
    Page<Marca> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
    List<Marca> findByDescripcionContainingIgnoreCaseAndActivaTrue(String descripcion);
    Page<Marca> findByDescripcionContainingIgnoreCaseAndActivaTrue(String descripcion, Pageable pageable);
    List<Marca> findByPalabrasClaveContainingIgnoreCase(String palabrasClave);
    Page<Marca> findByPalabrasClaveContainingIgnoreCase(String palabrasClave, Pageable pageable);
    List<Marca> findByPalabrasClaveContainingIgnoreCaseAndActivaTrue(String palabrasClave);
    Page<Marca> findByPalabrasClaveContainingIgnoreCaseAndActivaTrue(String palabrasClave, Pageable pageable);

    // Búsquedas por fecha
    List<Marca> findByCreatedAtAfter(LocalDateTime fecha);
    Page<Marca> findByCreatedAtAfter(LocalDateTime fecha, Pageable pageable);
    List<Marca> findByCreatedAtAfterAndActivaTrue(LocalDateTime fecha);
    Page<Marca> findByCreatedAtAfterAndActivaTrue(LocalDateTime fecha, Pageable pageable);
    List<Marca> findByUpdatedAtAfter(LocalDateTime fecha);
    Page<Marca> findByUpdatedAtAfter(LocalDateTime fecha, Pageable pageable);
    List<Marca> findByUpdatedAtAfterAndActivaTrue(LocalDateTime fecha);
    Page<Marca> findByUpdatedAtAfterAndActivaTrue(LocalDateTime fecha, Pageable pageable);

    // Búsquedas por rango de fechas
    List<Marca> findByCreatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByCreatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Marca> findByCreatedAtBetweenAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByCreatedAtBetweenAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Marca> findByUpdatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByUpdatedAtBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Marca> findByUpdatedAtBetweenAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByUpdatedAtBetweenAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    // Búsquedas por orden específico
    List<Marca> findAllByOrderByNombreAsc();
    Page<Marca> findAllByOrderByNombreAsc(Pageable pageable);
    List<Marca> findAllByOrderByOrdenAsc();
    Page<Marca> findAllByOrderByOrdenAsc(Pageable pageable);
    List<Marca> findAllByOrderByOrdenDesc();
    Page<Marca> findAllByOrderByOrdenDesc(Pageable pageable);
    List<Marca> findAllByOrderByCreatedAtAsc();
    Page<Marca> findAllByOrderByCreatedAtAsc(Pageable pageable);
    List<Marca> findAllByOrderByCreatedAtDesc();
    Page<Marca> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Marca> findAllByOrderByUpdatedAtAsc();
    Page<Marca> findAllByOrderByUpdatedAtAsc(Pageable pageable);
    List<Marca> findAllByOrderByUpdatedAtDesc();
    Page<Marca> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    // Búsquedas de marcas activas por orden específico
    List<Marca> findByActivaTrueOrderByNombreAsc();
    Page<Marca> findByActivaTrueOrderByNombreAsc(Pageable pageable);
    List<Marca> findByActivaTrueOrderByOrdenAsc();
    Page<Marca> findByActivaTrueOrderByOrdenAsc(Pageable pageable);
    List<Marca> findByActivaTrueOrderByOrdenDesc();
    Page<Marca> findByActivaTrueOrderByOrdenDesc(Pageable pageable);
    List<Marca> findByActivaTrueOrderByCreatedAtAsc();
    Page<Marca> findByActivaTrueOrderByCreatedAtAsc(Pageable pageable);
    List<Marca> findByActivaTrueOrderByCreatedAtDesc();
    Page<Marca> findByActivaTrueOrderByCreatedAtDesc(Pageable pageable);
    List<Marca> findByActivaTrueOrderByUpdatedAtAsc();
    Page<Marca> findByActivaTrueOrderByUpdatedAtAsc(Pageable pageable);
    List<Marca> findByActivaTrueOrderByUpdatedAtDesc();
    Page<Marca> findByActivaTrueOrderByUpdatedAtDesc(Pageable pageable);

    // Búsquedas de marcas destacadas
    List<Marca> findTop10ByDestacadaTrueAndActivaTrueOrderByOrdenAsc();
    List<Marca> findTop10ByDestacadaTrueAndActivaTrueOrderByCreatedAtDesc();
    List<Marca> findTop10ByDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de marcas recientes
    List<Marca> findTop10ByActivaTrueOrderByCreatedAtDesc();
    List<Marca> findTop10ByActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de marcas por país
    List<Marca> findTop10ByPaisAndActivaTrueOrderByOrdenAsc(String pais);
    List<Marca> findTop10ByPaisAndActivaTrueOrderByCreatedAtDesc(String pais);
    List<Marca> findTop10ByPaisAndActivaTrueOrderByUpdatedAtDesc(String pais);

    // Búsquedas de marcas por texto completo - TEMPORALMENTE COMENTADAS
    // List<Marca> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCase(String texto);
    // Page<Marca> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCase(String texto, Pageable pageable);
    // List<Marca> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivaTrue(String texto);
    // Page<Marca> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivaTrue(String texto, Pageable pageable);

    // Búsquedas de marcas por rango de orden (métodos ya definidos arriba)

    // Búsquedas de marcas por rango de fechas (métodos ya definidos arriba)

    // Búsquedas de marcas por múltiples criterios
    List<Marca> findByDestacadaTrueAndActivaTrueOrderByOrdenAsc();
    List<Marca> findByDestacadaTrueAndActivaTrueOrderByCreatedAtDesc();
    List<Marca> findByDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de marcas por rango de orden y destacadas
    List<Marca> findByOrdenBetweenAndDestacadaTrueAndActivaTrue(Integer ordenMin, Integer ordenMax);
    Page<Marca> findByOrdenBetweenAndDestacadaTrueAndActivaTrue(Integer ordenMin, Integer ordenMax, Pageable pageable);

    // Búsquedas de marcas por rango de fechas y destacadas
    List<Marca> findByCreatedAtBetweenAndDestacadaTrueAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByCreatedAtBetweenAndDestacadaTrueAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Marca> findByUpdatedAtBetweenAndDestacadaTrueAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByUpdatedAtBetweenAndDestacadaTrueAndActivaTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    // Búsquedas de marcas por país y destacadas
    List<Marca> findByPaisAndDestacadaTrueAndActivaTrueOrderByOrdenAsc(String pais);
    List<Marca> findByPaisAndDestacadaTrueAndActivaTrueOrderByCreatedAtDesc(String pais);
    List<Marca> findByPaisAndDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc(String pais);

    // Búsquedas de marcas por país y rango de orden
    List<Marca> findByPaisAndOrdenBetweenAndActivaTrue(String pais, Integer ordenMin, Integer ordenMax);
    Page<Marca> findByPaisAndOrdenBetweenAndActivaTrue(String pais, Integer ordenMin, Integer ordenMax, Pageable pageable);

    // Búsquedas de marcas por país y rango de fechas
    List<Marca> findByPaisAndCreatedAtBetweenAndActivaTrue(String pais, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByPaisAndCreatedAtBetweenAndActivaTrue(String pais, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Marca> findByPaisAndUpdatedAtBetweenAndActivaTrue(String pais, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByPaisAndUpdatedAtBetweenAndActivaTrue(String pais, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    // Búsquedas de marcas por país y destacadas y rango de orden
    List<Marca> findByPaisAndOrdenBetweenAndDestacadaTrueAndActivaTrue(String pais, Integer ordenMin, Integer ordenMax);
    Page<Marca> findByPaisAndOrdenBetweenAndDestacadaTrueAndActivaTrue(String pais, Integer ordenMin, Integer ordenMax, Pageable pageable);

    // Búsquedas de marcas por país y destacadas y rango de fechas
    List<Marca> findByPaisAndCreatedAtBetweenAndDestacadaTrueAndActivaTrue(String pais, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByPaisAndCreatedAtBetweenAndDestacadaTrueAndActivaTrue(String pais, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    List<Marca> findByPaisAndUpdatedAtBetweenAndDestacadaTrueAndActivaTrue(String pais, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Marca> findByPaisAndUpdatedAtBetweenAndDestacadaTrueAndActivaTrue(String pais, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    // Búsquedas de marcas por sitio web
    List<Marca> findBySitioWebIsNotNullAndActivaTrueOrderByOrdenAsc();
    List<Marca> findBySitioWebIsNotNullAndActivaTrueOrderByCreatedAtDesc();
    List<Marca> findBySitioWebIsNotNullAndActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de marcas por logo
    List<Marca> findByLogoIsNotNullAndActivaTrueOrderByOrdenAsc();
    List<Marca> findByLogoIsNotNullAndActivaTrueOrderByCreatedAtDesc();
    List<Marca> findByLogoIsNotNullAndActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de marcas por sitio web y destacadas
    List<Marca> findBySitioWebIsNotNullAndDestacadaTrueAndActivaTrueOrderByOrdenAsc();
    List<Marca> findBySitioWebIsNotNullAndDestacadaTrueAndActivaTrueOrderByCreatedAtDesc();
    List<Marca> findBySitioWebIsNotNullAndDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de marcas por logo y destacadas
    List<Marca> findByLogoIsNotNullAndDestacadaTrueAndActivaTrueOrderByOrdenAsc();
    List<Marca> findByLogoIsNotNullAndDestacadaTrueAndActivaTrueOrderByCreatedAtDesc();
    List<Marca> findByLogoIsNotNullAndDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc();

    // Búsquedas de marcas por sitio web y país
    List<Marca> findBySitioWebIsNotNullAndPaisAndActivaTrueOrderByOrdenAsc(String pais);
    List<Marca> findBySitioWebIsNotNullAndPaisAndActivaTrueOrderByCreatedAtDesc(String pais);
    List<Marca> findBySitioWebIsNotNullAndPaisAndActivaTrueOrderByUpdatedAtDesc(String pais);

    // Búsquedas de marcas por logo y país
    List<Marca> findByLogoIsNotNullAndPaisAndActivaTrueOrderByOrdenAsc(String pais);
    List<Marca> findByLogoIsNotNullAndPaisAndActivaTrueOrderByCreatedAtDesc(String pais);
    List<Marca> findByLogoIsNotNullAndPaisAndActivaTrueOrderByUpdatedAtDesc(String pais);

    // Búsquedas de marcas por sitio web y país y destacadas
    List<Marca> findBySitioWebIsNotNullAndPaisAndDestacadaTrueAndActivaTrueOrderByOrdenAsc(String pais);
    List<Marca> findBySitioWebIsNotNullAndPaisAndDestacadaTrueAndActivaTrueOrderByCreatedAtDesc(String pais);
    List<Marca> findBySitioWebIsNotNullAndPaisAndDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc(String pais);

    // Búsquedas de marcas por logo y país y destacadas
    List<Marca> findByLogoIsNotNullAndPaisAndDestacadaTrueAndActivaTrueOrderByOrdenAsc(String pais);
    List<Marca> findByLogoIsNotNullAndPaisAndDestacadaTrueAndActivaTrueOrderByCreatedAtDesc(String pais);
    List<Marca> findByLogoIsNotNullAndPaisAndDestacadaTrueAndActivaTrueOrderByUpdatedAtDesc(String pais);
}
