package com.tienda.producto.application.service;

import com.tienda.producto.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para gestión de marcas.
 * 
 * @author Tienda Italo Team
 */
public interface MarcaService {

    // Operaciones CRUD básicas
    MarcaResponseDTO crearMarca(MarcaRequestDTO requestDTO);
    Optional<MarcaResponseDTO> buscarMarcaPorId(Long id);
    Optional<MarcaResponseDTO> buscarMarcaPorNombre(String nombre);
    Optional<MarcaResponseDTO> buscarMarcaPorSlug(String slug);
    MarcaResponseDTO actualizarMarca(Long id, MarcaRequestDTO requestDTO);
    void eliminarMarca(Long id);
    boolean existeMarcaPorId(Long id);
    boolean existeMarcaPorNombre(String nombre);
    boolean existeMarcaPorSlug(String slug);

    // Operaciones de listado
    List<MarcaResponseDTO> listarMarcas();
    Page<MarcaResponseDTO> listarMarcas(Pageable pageable);
    List<MarcaResponseDTO> listarMarcasActivas();
    Page<MarcaResponseDTO> listarMarcasActivas(Pageable pageable);
    List<MarcaResponseDTO> listarMarcasInactivas();
    Page<MarcaResponseDTO> listarMarcasInactivas(Pageable pageable);

    // Operaciones de búsqueda
    List<MarcaResponseDTO> buscarMarcasPorTexto(String texto);
    Page<MarcaResponseDTO> buscarMarcasPorTexto(String texto, Pageable pageable);
    List<MarcaResponseDTO> buscarMarcasPorPais(String pais);
    Page<MarcaResponseDTO> buscarMarcasPorPais(String pais, Pageable pageable);
    List<MarcaResponseDTO> buscarMarcasPorPalabrasClave(String palabrasClave);
    Page<MarcaResponseDTO> buscarMarcasPorPalabrasClave(String palabrasClave, Pageable pageable);

    // Operaciones de filtrado
    List<MarcaResponseDTO> filtrarMarcasPorEstado(Boolean activa);
    Page<MarcaResponseDTO> filtrarMarcasPorEstado(Boolean activa, Pageable pageable);
    List<MarcaResponseDTO> filtrarMarcasDestacadas();
    Page<MarcaResponseDTO> filtrarMarcasDestacadas(Pageable pageable);
    List<MarcaResponseDTO> filtrarMarcasPorOrden(Integer ordenMin, Integer ordenMax);
    Page<MarcaResponseDTO> filtrarMarcasPorOrden(Integer ordenMin, Integer ordenMax, Pageable pageable);
    List<MarcaResponseDTO> filtrarMarcasConSitioWeb();
    Page<MarcaResponseDTO> filtrarMarcasConSitioWeb(Pageable pageable);
    List<MarcaResponseDTO> filtrarMarcasConLogo();
    Page<MarcaResponseDTO> filtrarMarcasConLogo(Pageable pageable);

    // Operaciones de ordenamiento
    List<MarcaResponseDTO> listarMarcasOrdenadasPorNombre();
    Page<MarcaResponseDTO> listarMarcasOrdenadasPorNombre(Pageable pageable);
    List<MarcaResponseDTO> listarMarcasOrdenadasPorOrden();
    Page<MarcaResponseDTO> listarMarcasOrdenadasPorOrden(Pageable pageable);
    List<MarcaResponseDTO> listarMarcasOrdenadasPorPais();
    Page<MarcaResponseDTO> listarMarcasOrdenadasPorPais(Pageable pageable);
    List<MarcaResponseDTO> listarMarcasOrdenadasPorFechaCreacion();
    Page<MarcaResponseDTO> listarMarcasOrdenadasPorFechaCreacion(Pageable pageable);
    List<MarcaResponseDTO> listarMarcasOrdenadasPorFechaActualizacion();
    Page<MarcaResponseDTO> listarMarcasOrdenadasPorFechaActualizacion(Pageable pageable);

    // Operaciones de estado
    MarcaResponseDTO activarMarca(Long id);
    MarcaResponseDTO desactivarMarca(Long id);
    MarcaResponseDTO marcarComoDestacada(Long id);
    MarcaResponseDTO quitarDestacada(Long id);

    // Operaciones de orden
    MarcaResponseDTO actualizarOrden(Long id, Integer nuevoOrden);
    MarcaResponseDTO moverMarcaArriba(Long id);
    MarcaResponseDTO moverMarcaAbajo(Long id);
    MarcaResponseDTO moverMarcaAlInicio(Long id);
    MarcaResponseDTO moverMarcaAlFinal(Long id);

    // Operaciones de productos
    List<ProductoResponseDTO> listarProductos(Long marcaId);
    Page<ProductoResponseDTO> listarProductos(Long marcaId, Pageable pageable);
    List<ProductoResponseDTO> listarProductosActivos(Long marcaId);
    Page<ProductoResponseDTO> listarProductosActivos(Long marcaId, Pageable pageable);
    long contarProductos(Long marcaId);
    long contarProductosActivos(Long marcaId);

    // Operaciones de validación
    boolean validarNombreUnico(String nombre, Long idExcluir);
    boolean validarSlugUnico(String slug, Long idExcluir);
    boolean validarOrdenUnico(Integer orden, Long idExcluir);
    boolean validarSitioWebValido(String sitioWeb);
    boolean validarLogoValido(String logo);

    // Operaciones de conteo
    long contarMarcas();
    long contarMarcasActivas();
    long contarMarcasInactivas();
    long contarMarcasDestacadas();
    long contarMarcasPorPais(String pais);
    long contarMarcasPorOrden(Integer ordenMin, Integer ordenMax);
    long contarMarcasPorEstado(Boolean activa);
    long contarMarcasPorTexto(String texto);
    long contarMarcasPorPalabrasClave(String palabrasClave);
    long contarMarcasConSitioWeb();
    long contarMarcasConLogo();
    long contarMarcasPorRangoFechas(java.time.LocalDateTime fechaInicio, java.time.LocalDateTime fechaFin);
}
