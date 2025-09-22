package com.tienda.producto.application.service.impl;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.application.service.ProductoService;
import com.tienda.producto.domain.entity.Producto;
import com.tienda.producto.domain.entity.Categoria;
import com.tienda.producto.domain.entity.Marca;
import com.tienda.producto.domain.repository.ProductoRepository;
import com.tienda.producto.domain.repository.CategoriaRepository;
import com.tienda.producto.domain.repository.MarcaRepository;
import com.tienda.producto.web.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de productos.
 * 
 * @author Tienda Italo Team
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProductoMapper productoMapper;

    // Operaciones CRUD básicas
    @Override
    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO) {
        log.info("Creando nuevo producto: {}", requestDTO.getNombre());
        
        // Validar unicidad de códigos
        validarUnicidadCodigos(requestDTO);
        
        // Obtener categoría
        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        // Obtener marca si se proporciona
        Marca marca = null;
        if (requestDTO.getMarcaId() != null) {
            marca = marcaRepository.findById(requestDTO.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        }
        
        // Crear producto
        Producto producto = productoMapper.toEntity(requestDTO);
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setCreatedAt(LocalDateTime.now());
        producto.setUpdatedAt(LocalDateTime.now());
        
        Producto productoGuardado = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", productoGuardado.getId());
        
        return productoMapper.toResponseDTO(productoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoResponseDTO> buscarProductoPorId(Long id) {
        log.debug("Buscando producto por ID: {}", id);
        return productoRepository.findById(id)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoResponseDTO> buscarProductoPorCodigo(String codigo) {
        log.debug("Buscando producto por código: {}", codigo);
        return productoRepository.findByCodigo(codigo)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoResponseDTO> buscarProductoPorSku(String sku) {
        log.debug("Buscando producto por SKU: {}", sku);
        return productoRepository.findBySku(sku)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoResponseDTO> buscarProductoPorSlug(String slug) {
        log.debug("Buscando producto por slug: {}", slug);
        return productoRepository.findBySlug(slug)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO) {
        log.info("Actualizando producto con ID: {}", id);
        
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Validar unicidad de códigos (excluyendo el producto actual)
        validarUnicidadCodigos(requestDTO, id);
        
        // Actualizar datos
        productoMapper.updateEntityFromDTO(requestDTO, producto);
        producto.setUpdatedAt(LocalDateTime.now());
        
        // Actualizar categoría si cambió
        if (!producto.getCategoria().getId().equals(requestDTO.getCategoriaId())) {
            Categoria nuevaCategoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(nuevaCategoria);
        }
        
        // Actualizar marca si cambió
        if (requestDTO.getMarcaId() != null) {
            Marca nuevaMarca = marcaRepository.findById(requestDTO.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
            producto.setMarca(nuevaMarca);
        } else {
            producto.setMarca(null);
        }
        
        Producto productoActualizado = productoRepository.save(producto);
        log.info("Producto actualizado exitosamente con ID: {}", productoActualizado.getId());
        
        return productoMapper.toResponseDTO(productoActualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        log.info("Eliminando producto con ID: {}", id);
        
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        
        productoRepository.deleteById(id);
        log.info("Producto eliminado exitosamente con ID: {}", id);
    }

    // Operaciones de listado
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductos() {
        log.debug("Listando todos los productos");
        return productoMapper.toResponseDTOList(productoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> listarProductos(Pageable pageable) {
        log.debug("Listando productos paginados: {}", pageable);
        return productoRepository.findAll(pageable)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductosActivos() {
        log.debug("Listando productos activos");
        return productoMapper.toResponseDTOList(productoRepository.findByActivoTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> listarProductosActivos(Pageable pageable) {
        log.debug("Listando productos activos paginados: {}", pageable);
        return productoRepository.findByActivoTrue(pageable)
                .map(productoMapper::toResponseDTO);
    }

    // Operaciones de búsqueda
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductos(ProductoBusquedaDTO busquedaDTO) {
        log.debug("Buscando productos con criterios: {}", busquedaDTO);
        // Implementar lógica de búsqueda compleja
        return List.of(); // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> buscarProductos(ProductoBusquedaDTO busquedaDTO, Pageable pageable) {
        log.debug("Buscando productos paginados con criterios: {}", busquedaDTO);
        // Implementar lógica de búsqueda compleja
        return Page.empty(); // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductosPorTexto(String texto) {
        log.debug("Buscando productos por texto: {}", texto);
        return productoMapper.toResponseDTOList(
                productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivoTrue(texto)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> buscarProductosPorTexto(String texto, Pageable pageable) {
        log.debug("Buscando productos por texto paginados: {}", texto);
        return productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivoTrue(texto, pageable)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductosPorCategoria(Long categoriaId) {
        log.debug("Buscando productos por categoría: {}", categoriaId);
        return productoMapper.toResponseDTOList(
                productoRepository.findByCategoriaIdAndActivoTrue(categoriaId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> buscarProductosPorCategoria(Long categoriaId, Pageable pageable) {
        log.debug("Buscando productos por categoría paginados: {}", categoriaId);
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId, pageable)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductosPorMarca(Long marcaId) {
        log.debug("Buscando productos por marca: {}", marcaId);
        return productoMapper.toResponseDTOList(
                productoRepository.findByMarcaIdAndActivoTrue(marcaId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> buscarProductosPorMarca(Long marcaId, Pageable pageable) {
        log.debug("Buscando productos por marca paginados: {}", marcaId);
        return productoRepository.findByMarcaIdAndActivoTrue(marcaId, pageable)
                .map(productoMapper::toResponseDTO);
    }

    // Operaciones de filtrado
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> filtrarProductos(ProductoFiltroDTO filtroDTO) {
        log.debug("Filtrando productos con criterios: {}", filtroDTO);
        // Implementar lógica de filtrado compleja
        return List.of(); // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> filtrarProductos(ProductoFiltroDTO filtroDTO, Pageable pageable) {
        log.debug("Filtrando productos paginados con criterios: {}", filtroDTO);
        // Implementar lógica de filtrado compleja
        return Page.empty(); // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> filtrarProductosPorPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        log.debug("Filtrando productos por precio: {} - {}", precioMin, precioMax);
        return productoMapper.toResponseDTOList(
                productoRepository.findByPrecioBetweenAndActivoTrue(precioMin, precioMax)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> filtrarProductosPorPrecio(BigDecimal precioMin, BigDecimal precioMax, Pageable pageable) {
        log.debug("Filtrando productos por precio paginados: {} - {}", precioMin, precioMax);
        return productoRepository.findByPrecioBetweenAndActivoTrue(precioMin, precioMax, pageable)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> filtrarProductosEnOferta() {
        log.debug("Filtrando productos en oferta");
        return productoMapper.toResponseDTOList(
                productoRepository.findByPrecioOfertaIsNotNullAndActivoTrue()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> filtrarProductosEnOferta(Pageable pageable) {
        log.debug("Filtrando productos en oferta paginados");
        return productoRepository.findByPrecioOfertaIsNotNullAndActivoTrue(pageable)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> filtrarProductosDestacados() {
        log.debug("Filtrando productos destacados");
        return productoMapper.toResponseDTOList(
                productoRepository.findByDestacadoTrueAndActivoTrue()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> filtrarProductosDestacados(Pageable pageable) {
        log.debug("Filtrando productos destacados paginados");
        return productoRepository.findByDestacadoTrueAndActivoTrue(pageable)
                .map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> filtrarProductosNuevos() {
        log.debug("Filtrando productos nuevos");
        return productoMapper.toResponseDTOList(
                productoRepository.findByNuevoTrueAndActivoTrue()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> filtrarProductosNuevos(Pageable pageable) {
        log.debug("Filtrando productos nuevos paginados");
        return productoRepository.findByNuevoTrueAndActivoTrue(pageable)
                .map(productoMapper::toResponseDTO);
    }

    // Operaciones de estado
    @Override
    public ProductoResponseDTO activarProducto(Long id) {
        log.info("Activando producto con ID: {}", id);
        return cambiarEstadoProducto(id, true);
    }

    @Override
    public ProductoResponseDTO desactivarProducto(Long id) {
        log.info("Desactivando producto con ID: {}", id);
        return cambiarEstadoProducto(id, false);
    }

    @Override
    public ProductoResponseDTO marcarComoDestacado(Long id) {
        log.info("Marcando producto como destacado con ID: {}", id);
        return cambiarDestacadoProducto(id, true);
    }

    @Override
    public ProductoResponseDTO quitarDestacado(Long id) {
        log.info("Quitando destacado del producto con ID: {}", id);
        return cambiarDestacadoProducto(id, false);
    }

    @Override
    public ProductoResponseDTO marcarComoNuevo(Long id) {
        log.info("Marcando producto como nuevo con ID: {}", id);
        return cambiarNuevoProducto(id, true);
    }

    @Override
    public ProductoResponseDTO quitarNuevo(Long id) {
        log.info("Quitando nuevo del producto con ID: {}", id);
        return cambiarNuevoProducto(id, false);
    }

    // Operaciones de stock
    @Override
    public ProductoResponseDTO actualizarStock(Long id, Integer nuevoStock) {
        log.info("Actualizando stock del producto {} a {}", id, nuevoStock);
        
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        producto.setStock(nuevoStock);
        producto.setUpdatedAt(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        log.info("Stock actualizado exitosamente para producto {}", id);
        
        return productoMapper.toResponseDTO(productoActualizado);
    }

    @Override
    public ProductoResponseDTO reducirStock(Long id, Integer cantidad) {
        log.info("Reduciendo stock del producto {} en {}", id, cantidad);
        
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }
        
        producto.setStock(producto.getStock() - cantidad);
        producto.setUpdatedAt(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        log.info("Stock reducido exitosamente para producto {}", id);
        
        return productoMapper.toResponseDTO(productoActualizado);
    }

    @Override
    public ProductoResponseDTO aumentarStock(Long id, Integer cantidad) {
        log.info("Aumentando stock del producto {} en {}", id, cantidad);
        
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        producto.setStock(producto.getStock() + cantidad);
        producto.setUpdatedAt(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        log.info("Stock aumentado exitosamente para producto {}", id);
        
        return productoMapper.toResponseDTO(productoActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductosConStockBajo() {
        log.debug("Listando productos con stock bajo");
        return productoMapper.toResponseDTOList(
                productoRepository.findByStockLessThanEqualAndActivoTrue(10) // Stock bajo = 10 o menos
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> listarProductosConStockBajo(Pageable pageable) {
        log.debug("Listando productos con stock bajo paginados");
        List<Producto> productos = productoRepository.findByStockLessThanEqualAndActivoTrue(10);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productos.size());
        List<Producto> pageContent = productos.subList(start, end);
        
        return new PageImpl<>(pageContent.stream()
                .map(productoMapper::toResponseDTO)
                .collect(Collectors.toList()), pageable, productos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductosSinStock() {
        log.debug("Listando productos sin stock");
        return productoMapper.toResponseDTOList(
                productoRepository.findByStockEqualsAndActivoTrue(0)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> listarProductosSinStock(Pageable pageable) {
        log.debug("Listando productos sin stock paginados");
        return productoRepository.findByStockEqualsAndActivoTrue(0, pageable)
                .map(productoMapper::toResponseDTO);
    }

    // Operaciones de conteo
    @Override
    @Transactional(readOnly = true)
    public long contarProductos() {
        return productoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarProductosActivos() {
        return productoRepository.countByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarProductosInactivos() {
        return productoRepository.countByActivoFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarProductosDestacados() {
        return productoRepository.findByDestacadoTrueAndActivoTrue().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarProductosNuevos() {
        return productoRepository.findByNuevoTrueAndActivoTrue().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarProductosEnOferta() {
        return productoRepository.findByPrecioOfertaIsNotNullAndActivoTrue().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarProductosSinStock() {
        return productoRepository.findByStockEqualsAndActivoTrue(0).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarProductosConStockBajo() {
        return productoRepository.findByStockLessThanEqualAndActivoTrue(10).size();
    }

    // Métodos auxiliares privados
    private void validarUnicidadCodigos(ProductoRequestDTO requestDTO) {
        validarUnicidadCodigos(requestDTO, null);
    }

    private void validarUnicidadCodigos(ProductoRequestDTO requestDTO, Long idExcluir) {
        if (requestDTO.getCodigo() != null && productoRepository.existsByCodigo(requestDTO.getCodigo())) {
            if (idExcluir == null || !productoRepository.findByCodigo(requestDTO.getCodigo())
                    .map(Producto::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El código ya existe");
            }
        }
        
        if (requestDTO.getSku() != null && productoRepository.existsBySku(requestDTO.getSku())) {
            if (idExcluir == null || !productoRepository.findBySku(requestDTO.getSku())
                    .map(Producto::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El SKU ya existe");
            }
        }
        
        if (requestDTO.getEan() != null && productoRepository.existsByEan(requestDTO.getEan())) {
            if (idExcluir == null || !productoRepository.findByEan(requestDTO.getEan())
                    .map(Producto::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El EAN ya existe");
            }
        }
        
        if (requestDTO.getIsbn() != null && productoRepository.existsByIsbn(requestDTO.getIsbn())) {
            if (idExcluir == null || !productoRepository.findByIsbn(requestDTO.getIsbn())
                    .map(Producto::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El ISBN ya existe");
            }
        }
        
        if (requestDTO.getSlug() != null && productoRepository.existsBySlug(requestDTO.getSlug())) {
            if (idExcluir == null || !productoRepository.findBySlug(requestDTO.getSlug())
                    .map(Producto::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El slug ya existe");
            }
        }
    }

    private ProductoResponseDTO cambiarEstadoProducto(Long id, boolean activo) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        producto.setActivo(activo);
        producto.setUpdatedAt(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        return productoMapper.toResponseDTO(productoActualizado);
    }

    private ProductoResponseDTO cambiarDestacadoProducto(Long id, boolean destacado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        producto.setDestacado(destacado);
        producto.setUpdatedAt(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        return productoMapper.toResponseDTO(productoActualizado);
    }

    private ProductoResponseDTO cambiarNuevoProducto(Long id, boolean nuevo) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        producto.setNuevo(nuevo);
        producto.setUpdatedAt(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        return productoMapper.toResponseDTO(productoActualizado);
    }

    // Implementaciones placeholder para métodos no implementados
    @Override
    public boolean existeProductoPorId(Long id) {
        return productoRepository.existsById(id);
    }

    @Override
    public boolean existeProductoPorCodigo(String codigo) {
        return productoRepository.existsByCodigo(codigo);
    }

    @Override
    public boolean existeProductoPorSku(String sku) {
        return productoRepository.existsBySku(sku);
    }

    @Override
    public boolean existeProductoPorSlug(String slug) {
        return productoRepository.existsBySlug(slug);
    }

    @Override
    public List<ProductoResponseDTO> listarProductosInactivos() {
        return productoMapper.toResponseDTOList(productoRepository.findByActivoFalse());
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosInactivos(Pageable pageable) {
        return productoRepository.findByActivoFalse(pageable)
                .map(productoMapper::toResponseDTO);
    }

    // Métodos placeholder para funcionalidades futuras
    @Override
    public List<ProductoResponseDTO> listarProductosOrdenadosPorNombre() {
        return productoMapper.toResponseDTOList(productoRepository.findAllByOrderByNombreAsc());
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosOrdenadosPorNombre(Pageable pageable) {
        return productoRepository.findAllByOrderByNombreAsc(pageable)
                .map(productoMapper::toResponseDTO);
    }

    // Implementaciones placeholder para el resto de métodos
    // (Se implementarán en iteraciones futuras)
    @Override
    public List<ProductoResponseDTO> listarProductosOrdenadosPorPrecio() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosOrdenadosPorPrecio(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosOrdenadosPorPrecioDesc() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosOrdenadosPorPrecioDesc(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosOrdenadosPorFechaCreacion() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosOrdenadosPorFechaCreacion(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosOrdenadosPorVendidos() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosOrdenadosPorVendidos(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosOrdenadosPorVisualizaciones() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosOrdenadosPorVisualizaciones(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosOrdenadosPorCalificacion() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosOrdenadosPorCalificacion(Pageable pageable) {
        return Page.empty();
    }

    // Placeholder para el resto de métodos no implementados
    @Override
    public List<ProductoResponseDTO> listarProductosConStock(Integer stockMin) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosConStock(Integer stockMin, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public ProductoResponseDTO actualizarPrecio(Long id, BigDecimal nuevoPrecio) {
        return null;
    }

    @Override
    public ProductoResponseDTO actualizarPrecioOferta(Long id, BigDecimal nuevoPrecioOferta) {
        return null;
    }

    @Override
    public ProductoResponseDTO quitarOferta(Long id) {
        return null;
    }

    @Override
    public List<ProductoResponseDTO> listarProductosEnOferta() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosEnOferta(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax, Pageable pageable) {
        return Page.empty();
    }

    // Placeholder para métodos de atributos, imágenes, productos relacionados, etc.
    @Override
    public ProductoResponseDTO agregarAtributo(Long productoId, AtributoProductoRequestDTO atributoDTO) {
        return null;
    }

    @Override
    public ProductoResponseDTO actualizarAtributo(Long productoId, Long atributoId, AtributoProductoRequestDTO atributoDTO) {
        return null;
    }

    @Override
    public ProductoResponseDTO eliminarAtributo(Long productoId, Long atributoId) {
        return null;
    }

    @Override
    public List<AtributoProductoResponseDTO> listarAtributos(Long productoId) {
        return List.of();
    }

    @Override
    public ProductoResponseDTO agregarImagen(Long productoId, ImagenProductoRequestDTO imagenDTO) {
        return null;
    }

    @Override
    public ProductoResponseDTO actualizarImagen(Long productoId, Long imagenId, ImagenProductoRequestDTO imagenDTO) {
        return null;
    }

    @Override
    public ProductoResponseDTO eliminarImagen(Long productoId, Long imagenId) {
        return null;
    }

    @Override
    public ProductoResponseDTO marcarImagenComoPrincipal(Long productoId, Long imagenId) {
        return null;
    }

    @Override
    public List<ImagenProductoResponseDTO> listarImagenes(Long productoId) {
        return List.of();
    }

    @Override
    public ImagenProductoResponseDTO obtenerImagenPrincipal(Long productoId) {
        return null;
    }

    @Override
    public List<ImagenProductoResponseDTO> listarImagenesSecundarias(Long productoId) {
        return List.of();
    }

    @Override
    public ProductoResponseDTO agregarProductoRelacionado(Long productoId, Long productoRelacionadoId, String tipoRelacion) {
        return null;
    }

    @Override
    public ProductoResponseDTO eliminarProductoRelacionado(Long productoId, Long productoRelacionadoId) {
        return null;
    }

    @Override
    public List<ProductoResponseDTO> listarProductosRelacionados(Long productoId) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosRelacionados(Long productoId, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public ProductoResponseDTO calificarProducto(Long id, Double calificacion) {
        return null;
    }

    @Override
    public ProductoResponseDTO actualizarCalificacion(Long id, Double nuevaCalificacion) {
        return null;
    }

    @Override
    public List<ProductoResponseDTO> listarProductosMejorCalificados() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosMejorCalificados(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosPorCalificacionMinima(Double calificacionMin) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosPorCalificacionMinima(Double calificacionMin, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public ProductoResponseDTO incrementarVisualizaciones(Long id) {
        return null;
    }

    @Override
    public List<ProductoResponseDTO> listarProductosMasVistos() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosMasVistos(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosPorVisualizacionesMinimas(Integer visualizacionesMin) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosPorVisualizacionesMinimas(Integer visualizacionesMin, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public ProductoResponseDTO incrementarVendidos(Long id, Integer cantidad) {
        return null;
    }

    @Override
    public List<ProductoResponseDTO> listarProductosMasVendidos() {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosMasVendidos(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosPorVendidosMinimos(Integer vendidosMin) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosPorVendidosMinimos(Integer vendidosMin, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public ProductoEstadisticasDTO obtenerEstadisticas() {
        return null;
    }

    @Override
    public ProductoEstadisticasDTO obtenerEstadisticasPorCategoria(Long categoriaId) {
        return null;
    }

    @Override
    public ProductoEstadisticasDTO obtenerEstadisticasPorMarca(Long marcaId) {
        return null;
    }

    @Override
    public ProductoEstadisticasDTO obtenerEstadisticasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return null;
    }

    @Override
    public boolean validarCodigoUnico(String codigo, Long idExcluir) {
        return !productoRepository.existsByCodigo(codigo);
    }

    @Override
    public boolean validarSkuUnico(String sku, Long idExcluir) {
        return !productoRepository.existsBySku(sku);
    }

    @Override
    public boolean validarEanUnico(String ean, Long idExcluir) {
        return !productoRepository.existsByEan(ean);
    }

    @Override
    public boolean validarIsbnUnico(String isbn, Long idExcluir) {
        return !productoRepository.existsByIsbn(isbn);
    }

    @Override
    public boolean validarSlugUnico(String slug, Long idExcluir) {
        return !productoRepository.existsBySlug(slug);
    }

    @Override
    public long contarProductosPorCategoria(Long categoriaId) {
        return productoRepository.countByCategoriaIdAndActivoTrue(categoriaId);
    }

    @Override
    public long contarProductosPorMarca(Long marcaId) {
        return productoRepository.countByMarcaIdAndActivoTrue(marcaId);
    }

    @Override
    public long contarProductosPorPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.findByPrecioBetweenAndActivoTrue(precioMin, precioMax).size();
    }

    @Override
    public long contarProductosPorStock(Integer stockMin, Integer stockMax) {
        return productoRepository.findByStockBetweenAndActivoTrue(stockMin, stockMax).size();
    }

    @Override
    public long contarProductosPorCalificacion(Double calificacionMin) {
        return productoRepository.findByCalificacionPromedioGreaterThanEqualAndActivoTrue(calificacionMin).size();
    }

    @Override
    public long contarProductosPorVendidos(Integer vendidosMin) {
        return productoRepository.findByVendidosGreaterThanEqualAndActivoTrue(vendidosMin).size();
    }

    @Override
    public long contarProductosPorVisualizaciones(Integer visualizacionesMin) {
        return productoRepository.findByVisualizacionesGreaterThanEqualAndActivoTrue(visualizacionesMin).size();
    }
}
