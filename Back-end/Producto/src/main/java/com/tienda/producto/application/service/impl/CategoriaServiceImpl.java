package com.tienda.producto.application.service.impl;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.application.service.CategoriaService;
import com.tienda.producto.domain.entity.Categoria;
import com.tienda.producto.domain.repository.CategoriaRepository;
import com.tienda.producto.web.mapper.CategoriaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de categorías.
 * 
 * @author Tienda Italo Team
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    // Operaciones CRUD básicas
    @Override
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO requestDTO) {
        log.info("Creando nueva categoría: {}", requestDTO.getNombre());
        
        // Validar unicidad de nombre y slug
        validarUnicidadCategoria(requestDTO);
        
        // Obtener categoría padre si se proporciona
        Categoria categoriaPadre = null;
        if (requestDTO.getCategoriaPadreId() != null) {
            categoriaPadre = categoriaRepository.findById(requestDTO.getCategoriaPadreId())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
        }
        
        // Crear categoría
        Categoria categoria = categoriaMapper.toEntity(requestDTO);
        categoria.setCategoriaPadre(categoriaPadre);
        categoria.setCreatedAt(LocalDateTime.now());
        categoria.setUpdatedAt(LocalDateTime.now());
        
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        log.info("Categoría creada exitosamente con ID: {}", categoriaGuardada.getId());
        
        return categoriaMapper.toResponseDTO(categoriaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaResponseDTO> buscarCategoriaPorId(Long id) {
        log.debug("Buscando categoría por ID: {}", id);
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaResponseDTO> buscarCategoriaPorNombre(String nombre) {
        log.debug("Buscando categoría por nombre: {}", nombre);
        return categoriaRepository.findByNombre(nombre)
                .map(categoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaResponseDTO> buscarCategoriaPorSlug(String slug) {
        log.debug("Buscando categoría por slug: {}", slug);
        return categoriaRepository.findBySlug(slug)
                .map(categoriaMapper::toResponseDTO);
    }

    @Override
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO requestDTO) {
        log.info("Actualizando categoría con ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        // Validar unicidad de nombre y slug (excluyendo la categoría actual)
        validarUnicidadCategoria(requestDTO, id);
        
        // Actualizar datos
        categoriaMapper.updateEntityFromDTO(requestDTO, categoria);
        categoria.setUpdatedAt(LocalDateTime.now());
        
        // Actualizar categoría padre si cambió
        if (requestDTO.getCategoriaPadreId() != null) {
            Categoria nuevaCategoriaPadre = categoriaRepository.findById(requestDTO.getCategoriaPadreId())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
            categoria.setCategoriaPadre(nuevaCategoriaPadre);
        } else {
            categoria.setCategoriaPadre(null);
        }
        
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        log.info("Categoría actualizada exitosamente con ID: {}", categoriaActualizada.getId());
        
        return categoriaMapper.toResponseDTO(categoriaActualizada);
    }

    @Override
    public void eliminarCategoria(Long id) {
        log.info("Eliminando categoría con ID: {}", id);
        
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        
        // Verificar que no tenga subcategorías
        if (categoriaRepository.countByCategoriaPadreId(id) > 0) {
            throw new RuntimeException("No se puede eliminar una categoría que tiene subcategorías");
        }
        
        categoriaRepository.deleteById(id);
        log.info("Categoría eliminada exitosamente con ID: {}", id);
    }

    // Operaciones de listado
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategorias() {
        log.debug("Listando todas las categorías");
        return categoriaMapper.toResponseDTOList(categoriaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarCategorias(Pageable pageable) {
        log.debug("Listando categorías paginadas: {}", pageable);
        return categoriaRepository.findAll(pageable)
                .map(categoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategoriasActivas() {
        log.debug("Listando categorías activas");
        return categoriaMapper.toResponseDTOList(categoriaRepository.findByActivaTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarCategoriasActivas(Pageable pageable) {
        log.debug("Listando categorías activas paginadas: {}", pageable);
        return categoriaRepository.findByActivaTrue(pageable)
                .map(categoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategoriasInactivas() {
        log.debug("Listando categorías inactivas");
        return categoriaMapper.toResponseDTOList(categoriaRepository.findByActivaFalse());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarCategoriasInactivas(Pageable pageable) {
        log.debug("Listando categorías inactivas paginadas: {}", pageable);
        return categoriaRepository.findByActivaFalse(pageable)
                .map(categoriaMapper::toResponseDTO);
    }

    // Operaciones de jerarquía
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategoriasRaiz() {
        log.debug("Listando categorías raíz");
        return categoriaMapper.toResponseDTOList(categoriaRepository.findByCategoriaPadreIsNull());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarCategoriasRaiz(Pageable pageable) {
        log.debug("Listando categorías raíz paginadas: {}", pageable);
        return categoriaRepository.findByCategoriaPadreIsNull(pageable)
                .map(categoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategoriasRaizActivas() {
        log.debug("Listando categorías raíz activas");
        return categoriaMapper.toResponseDTOList(categoriaRepository.findByCategoriaPadreIsNullAndActivaTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarCategoriasRaizActivas(Pageable pageable) {
        log.debug("Listando categorías raíz activas paginadas: {}", pageable);
        return categoriaRepository.findByCategoriaPadreIsNullAndActivaTrue(pageable)
                .map(categoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarSubcategorias(Long categoriaPadreId) {
        log.debug("Listando subcategorías de: {}", categoriaPadreId);
        return categoriaMapper.toResponseDTOList(categoriaRepository.findByCategoriaPadreId(categoriaPadreId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarSubcategorias(Long categoriaPadreId, Pageable pageable) {
        log.debug("Listando subcategorías paginadas de: {}", categoriaPadreId);
        return categoriaRepository.findByCategoriaPadreId(categoriaPadreId, pageable)
                .map(categoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarSubcategoriasActivas(Long categoriaPadreId) {
        log.debug("Listando subcategorías activas de: {}", categoriaPadreId);
        return categoriaMapper.toResponseDTOList(categoriaRepository.findByCategoriaPadreIdAndActivaTrue(categoriaPadreId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarSubcategoriasActivas(Long categoriaPadreId, Pageable pageable) {
        log.debug("Listando subcategorías activas paginadas de: {}", categoriaPadreId);
        return categoriaRepository.findByCategoriaPadreIdAndActivaTrue(categoriaPadreId, pageable)
                .map(categoriaMapper::toResponseDTO);
    }

    // Operaciones de búsqueda
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> buscarCategoriasPorTexto(String texto) {
        log.debug("Buscando categorías por texto: {}", texto);
        return categoriaMapper.toResponseDTOList(
                categoriaRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivaTrue(texto)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> buscarCategoriasPorTexto(String texto, Pageable pageable) {
        log.debug("Buscando categorías por texto paginadas: {}", texto);
        return categoriaRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivaTrue(texto, pageable)
                .map(categoriaMapper::toResponseDTO);
    }

    // Operaciones de estado
    @Override
    public CategoriaResponseDTO activarCategoria(Long id) {
        log.info("Activando categoría con ID: {}", id);
        return cambiarEstadoCategoria(id, true);
    }

    @Override
    public CategoriaResponseDTO desactivarCategoria(Long id) {
        log.info("Desactivando categoría con ID: {}", id);
        return cambiarEstadoCategoria(id, false);
    }

    @Override
    public CategoriaResponseDTO marcarComoDestacada(Long id) {
        log.info("Marcando categoría como destacada con ID: {}", id);
        return cambiarDestacadaCategoria(id, true);
    }

    @Override
    public CategoriaResponseDTO quitarDestacada(Long id) {
        log.info("Quitando destacada de la categoría con ID: {}", id);
        return cambiarDestacadaCategoria(id, false);
    }

    // Operaciones de conteo
    @Override
    @Transactional(readOnly = true)
    public long contarCategorias() {
        return categoriaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCategoriasActivas() {
        return categoriaRepository.countByActivaTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCategoriasInactivas() {
        return categoriaRepository.countByActivaFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCategoriasDestacadas() {
        return categoriaRepository.findByDestacadaTrueAndActivaTrue().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCategoriasRaiz() {
        return categoriaRepository.countByCategoriaPadreIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCategoriasRaizActivas() {
        return categoriaRepository.countByCategoriaPadreIsNullAndActivaTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarSubcategorias(Long categoriaPadreId) {
        return categoriaRepository.countByCategoriaPadreId(categoriaPadreId);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarSubcategoriasActivas(Long categoriaPadreId) {
        return categoriaRepository.countByCategoriaPadreIdAndActivaTrue(categoriaPadreId);
    }

    // Métodos auxiliares privados
    private void validarUnicidadCategoria(CategoriaRequestDTO requestDTO) {
        validarUnicidadCategoria(requestDTO, null);
    }

    private void validarUnicidadCategoria(CategoriaRequestDTO requestDTO, Long idExcluir) {
        if (categoriaRepository.existsByNombre(requestDTO.getNombre())) {
            if (idExcluir == null || !categoriaRepository.findByNombre(requestDTO.getNombre())
                    .map(Categoria::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El nombre de categoría ya existe");
            }
        }
        
        if (requestDTO.getSlug() != null && categoriaRepository.existsBySlug(requestDTO.getSlug())) {
            if (idExcluir == null || !categoriaRepository.findBySlug(requestDTO.getSlug())
                    .map(Categoria::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El slug de categoría ya existe");
            }
        }
    }

    private CategoriaResponseDTO cambiarEstadoCategoria(Long id, boolean activa) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        categoria.setActiva(activa);
        categoria.setUpdatedAt(LocalDateTime.now());
        
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        return categoriaMapper.toResponseDTO(categoriaActualizada);
    }

    private CategoriaResponseDTO cambiarDestacadaCategoria(Long id, boolean destacada) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        categoria.setDestacada(destacada);
        categoria.setUpdatedAt(LocalDateTime.now());
        
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        return categoriaMapper.toResponseDTO(categoriaActualizada);
    }

    // Implementaciones placeholder para métodos no implementados
    @Override
    public boolean existeCategoriaPorId(Long id) {
        return categoriaRepository.existsById(id);
    }

    @Override
    public boolean existeCategoriaPorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    @Override
    public boolean existeCategoriaPorSlug(String slug) {
        return categoriaRepository.existsBySlug(slug);
    }

    @Override
    public List<CategoriaResponseDTO> listarCategoriasPorNivel(Integer nivel) {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> listarCategoriasPorNivel(Integer nivel, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<CategoriaResponseDTO> buscarCategoriasPorPalabrasClave(String palabrasClave) {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> buscarCategoriasPorPalabrasClave(String palabrasClave, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<CategoriaResponseDTO> filtrarCategoriasPorEstado(Boolean activa) {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> filtrarCategoriasPorEstado(Boolean activa, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<CategoriaResponseDTO> filtrarCategoriasDestacadas() {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> filtrarCategoriasDestacadas(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<CategoriaResponseDTO> filtrarCategoriasPorOrden(Integer ordenMin, Integer ordenMax) {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> filtrarCategoriasPorOrden(Integer ordenMin, Integer ordenMax, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<CategoriaResponseDTO> listarCategoriasOrdenadasPorNombre() {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> listarCategoriasOrdenadasPorNombre(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<CategoriaResponseDTO> listarCategoriasOrdenadasPorOrden() {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> listarCategoriasOrdenadasPorOrden(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<CategoriaResponseDTO> listarCategoriasOrdenadasPorFechaCreacion() {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> listarCategoriasOrdenadasPorFechaCreacion(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<CategoriaResponseDTO> listarCategoriasOrdenadasPorFechaActualizacion() {
        return List.of();
    }

    @Override
    public Page<CategoriaResponseDTO> listarCategoriasOrdenadasPorFechaActualizacion(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public CategoriaResponseDTO actualizarOrden(Long id, Integer nuevoOrden) {
        return null;
    }

    @Override
    public CategoriaResponseDTO moverCategoriaArriba(Long id) {
        return null;
    }

    @Override
    public CategoriaResponseDTO moverCategoriaAbajo(Long id) {
        return null;
    }

    @Override
    public CategoriaResponseDTO moverCategoriaAlInicio(Long id) {
        return null;
    }

    @Override
    public CategoriaResponseDTO moverCategoriaAlFinal(Long id) {
        return null;
    }

    @Override
    public CategoriaResponseDTO cambiarCategoriaPadre(Long id, Long nuevaCategoriaPadreId) {
        return null;
    }

    @Override
    public CategoriaResponseDTO quitarCategoriaPadre(Long id) {
        return null;
    }

    @Override
    public List<CategoriaResponseDTO> obtenerRutaCompleta(Long id) {
        return List.of();
    }

    @Override
    public List<CategoriaResponseDTO> obtenerDescendientes(Long id) {
        return List.of();
    }

    @Override
    public List<CategoriaResponseDTO> obtenerAntecesores(Long id) {
        return List.of();
    }

    @Override
    public List<ProductoResponseDTO> listarProductos(Long categoriaId) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductos(Long categoriaId, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosActivos(Long categoriaId) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosActivos(Long categoriaId, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public long contarProductos(Long categoriaId) {
        return 0;
    }

    @Override
    public long contarProductosActivos(Long categoriaId) {
        return 0;
    }

    @Override
    public boolean validarNombreUnico(String nombre, Long idExcluir) {
        return !categoriaRepository.existsByNombre(nombre);
    }

    @Override
    public boolean validarSlugUnico(String slug, Long idExcluir) {
        return !categoriaRepository.existsBySlug(slug);
    }

    @Override
    public boolean validarOrdenUnico(Integer orden, Long idExcluir) {
        return true;
    }

    @Override
    public boolean validarJerarquiaValida(Long id, Long categoriaPadreId) {
        return true;
    }

    @Override
    public boolean validarEliminacionSegura(Long id) {
        return categoriaRepository.countByCategoriaPadreId(id) == 0;
    }

    @Override
    public long contarCategoriasPorNivel(Integer nivel) {
        return 0;
    }

    @Override
    public long contarCategoriasPorOrden(Integer ordenMin, Integer ordenMax) {
        return 0;
    }

    @Override
    public long contarCategoriasPorEstado(Boolean activa) {
        return 0;
    }

    @Override
    public long contarCategoriasPorTexto(String texto) {
        return 0;
    }

    @Override
    public long contarCategoriasPorPalabrasClave(String palabrasClave) {
        return 0;
    }
}
