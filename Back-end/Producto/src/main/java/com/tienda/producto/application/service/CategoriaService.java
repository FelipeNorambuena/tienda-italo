package com.tienda.producto.application.service;

import com.tienda.producto.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para gestión de categorías.
 * 
 * @author Tienda Italo Team
 */
public interface CategoriaService {

    // Operaciones CRUD básicas
    CategoriaResponseDTO crearCategoria(CategoriaRequestDTO requestDTO);
    Optional<CategoriaResponseDTO> buscarCategoriaPorId(Long id);
    Optional<CategoriaResponseDTO> buscarCategoriaPorNombre(String nombre);
    Optional<CategoriaResponseDTO> buscarCategoriaPorSlug(String slug);
    CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO requestDTO);
    void eliminarCategoria(Long id);
    boolean existeCategoriaPorId(Long id);
    boolean existeCategoriaPorNombre(String nombre);
    boolean existeCategoriaPorSlug(String slug);

    // Operaciones de listado
    List<CategoriaResponseDTO> listarCategorias();
    Page<CategoriaResponseDTO> listarCategorias(Pageable pageable);
    List<CategoriaResponseDTO> listarCategoriasActivas();
    Page<CategoriaResponseDTO> listarCategoriasActivas(Pageable pageable);
    List<CategoriaResponseDTO> listarCategoriasInactivas();
    Page<CategoriaResponseDTO> listarCategoriasInactivas(Pageable pageable);

    // Operaciones de jerarquía
    List<CategoriaResponseDTO> listarCategoriasRaiz();
    Page<CategoriaResponseDTO> listarCategoriasRaiz(Pageable pageable);
    List<CategoriaResponseDTO> listarCategoriasRaizActivas();
    Page<CategoriaResponseDTO> listarCategoriasRaizActivas(Pageable pageable);
    List<CategoriaResponseDTO> listarSubcategorias(Long categoriaPadreId);
    Page<CategoriaResponseDTO> listarSubcategorias(Long categoriaPadreId, Pageable pageable);
    List<CategoriaResponseDTO> listarSubcategoriasActivas(Long categoriaPadreId);
    Page<CategoriaResponseDTO> listarSubcategoriasActivas(Long categoriaPadreId, Pageable pageable);
    List<CategoriaResponseDTO> listarCategoriasPorNivel(Integer nivel);
    Page<CategoriaResponseDTO> listarCategoriasPorNivel(Integer nivel, Pageable pageable);

    // Operaciones de búsqueda
    List<CategoriaResponseDTO> buscarCategoriasPorTexto(String texto);
    Page<CategoriaResponseDTO> buscarCategoriasPorTexto(String texto, Pageable pageable);
    List<CategoriaResponseDTO> buscarCategoriasPorPalabrasClave(String palabrasClave);
    Page<CategoriaResponseDTO> buscarCategoriasPorPalabrasClave(String palabrasClave, Pageable pageable);

    // Operaciones de filtrado
    List<CategoriaResponseDTO> filtrarCategoriasPorEstado(Boolean activa);
    Page<CategoriaResponseDTO> filtrarCategoriasPorEstado(Boolean activa, Pageable pageable);
    List<CategoriaResponseDTO> filtrarCategoriasDestacadas();
    Page<CategoriaResponseDTO> filtrarCategoriasDestacadas(Pageable pageable);
    List<CategoriaResponseDTO> filtrarCategoriasPorOrden(Integer ordenMin, Integer ordenMax);
    Page<CategoriaResponseDTO> filtrarCategoriasPorOrden(Integer ordenMin, Integer ordenMax, Pageable pageable);

    // Operaciones de ordenamiento
    List<CategoriaResponseDTO> listarCategoriasOrdenadasPorNombre();
    Page<CategoriaResponseDTO> listarCategoriasOrdenadasPorNombre(Pageable pageable);
    List<CategoriaResponseDTO> listarCategoriasOrdenadasPorOrden();
    Page<CategoriaResponseDTO> listarCategoriasOrdenadasPorOrden(Pageable pageable);
    List<CategoriaResponseDTO> listarCategoriasOrdenadasPorFechaCreacion();
    Page<CategoriaResponseDTO> listarCategoriasOrdenadasPorFechaCreacion(Pageable pageable);
    List<CategoriaResponseDTO> listarCategoriasOrdenadasPorFechaActualizacion();
    Page<CategoriaResponseDTO> listarCategoriasOrdenadasPorFechaActualizacion(Pageable pageable);

    // Operaciones de estado
    CategoriaResponseDTO activarCategoria(Long id);
    CategoriaResponseDTO desactivarCategoria(Long id);
    CategoriaResponseDTO marcarComoDestacada(Long id);
    CategoriaResponseDTO quitarDestacada(Long id);

    // Operaciones de orden
    CategoriaResponseDTO actualizarOrden(Long id, Integer nuevoOrden);
    CategoriaResponseDTO moverCategoriaArriba(Long id);
    CategoriaResponseDTO moverCategoriaAbajo(Long id);
    CategoriaResponseDTO moverCategoriaAlInicio(Long id);
    CategoriaResponseDTO moverCategoriaAlFinal(Long id);

    // Operaciones de jerarquía
    CategoriaResponseDTO cambiarCategoriaPadre(Long id, Long nuevaCategoriaPadreId);
    CategoriaResponseDTO quitarCategoriaPadre(Long id);
    List<CategoriaResponseDTO> obtenerRutaCompleta(Long id);
    List<CategoriaResponseDTO> obtenerDescendientes(Long id);
    List<CategoriaResponseDTO> obtenerAntecesores(Long id);

    // Operaciones de productos
    List<ProductoResponseDTO> listarProductos(Long categoriaId);
    Page<ProductoResponseDTO> listarProductos(Long categoriaId, Pageable pageable);
    List<ProductoResponseDTO> listarProductosActivos(Long categoriaId);
    Page<ProductoResponseDTO> listarProductosActivos(Long categoriaId, Pageable pageable);
    long contarProductos(Long categoriaId);
    long contarProductosActivos(Long categoriaId);

    // Operaciones de validación
    boolean validarNombreUnico(String nombre, Long idExcluir);
    boolean validarSlugUnico(String slug, Long idExcluir);
    boolean validarOrdenUnico(Integer orden, Long idExcluir);
    boolean validarJerarquiaValida(Long id, Long categoriaPadreId);
    boolean validarEliminacionSegura(Long id);

    // Operaciones de conteo
    long contarCategorias();
    long contarCategoriasActivas();
    long contarCategoriasInactivas();
    long contarCategoriasDestacadas();
    long contarCategoriasRaiz();
    long contarCategoriasRaizActivas();
    long contarSubcategorias(Long categoriaPadreId);
    long contarSubcategoriasActivas(Long categoriaPadreId);
    long contarCategoriasPorNivel(Integer nivel);
    long contarCategoriasPorOrden(Integer ordenMin, Integer ordenMax);
    long contarCategoriasPorEstado(Boolean activa);
    long contarCategoriasPorTexto(String texto);
    long contarCategoriasPorPalabrasClave(String palabrasClave);
}
