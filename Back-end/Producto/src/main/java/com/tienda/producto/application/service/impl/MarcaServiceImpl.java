package com.tienda.producto.application.service.impl;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.application.service.MarcaService;
import com.tienda.producto.domain.entity.Marca;
import com.tienda.producto.domain.repository.MarcaRepository;
import com.tienda.producto.web.mapper.MarcaMapper;
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
 * Implementación del servicio de marcas.
 * 
 * @author Tienda Italo Team
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    // Operaciones CRUD básicas
    @Override
    public MarcaResponseDTO crearMarca(MarcaRequestDTO requestDTO) {
        log.info("Creando nueva marca: {}", requestDTO.getNombre());
        
        // Validar unicidad de nombre y slug
        validarUnicidadMarca(requestDTO);
        
        // Crear marca
        Marca marca = marcaMapper.toEntity(requestDTO);
        marca.setCreatedAt(LocalDateTime.now());
        marca.setUpdatedAt(LocalDateTime.now());
        
        Marca marcaGuardada = marcaRepository.save(marca);
        log.info("Marca creada exitosamente con ID: {}", marcaGuardada.getId());
        
        return marcaMapper.toResponseDTO(marcaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarcaResponseDTO> buscarMarcaPorId(Long id) {
        log.debug("Buscando marca por ID: {}", id);
        return marcaRepository.findById(id)
                .map(marcaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarcaResponseDTO> buscarMarcaPorNombre(String nombre) {
        log.debug("Buscando marca por nombre: {}", nombre);
        return marcaRepository.findByNombre(nombre)
                .map(marcaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarcaResponseDTO> buscarMarcaPorSlug(String slug) {
        log.debug("Buscando marca por slug: {}", slug);
        return marcaRepository.findBySlug(slug)
                .map(marcaMapper::toResponseDTO);
    }

    @Override
    public MarcaResponseDTO actualizarMarca(Long id, MarcaRequestDTO requestDTO) {
        log.info("Actualizando marca con ID: {}", id);
        
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        
        // Validar unicidad de nombre y slug (excluyendo la marca actual)
        validarUnicidadMarca(requestDTO, id);
        
        // Actualizar datos
        marcaMapper.updateEntityFromDTO(requestDTO, marca);
        marca.setUpdatedAt(LocalDateTime.now());
        
        Marca marcaActualizada = marcaRepository.save(marca);
        log.info("Marca actualizada exitosamente con ID: {}", marcaActualizada.getId());
        
        return marcaMapper.toResponseDTO(marcaActualizada);
    }

    @Override
    public void eliminarMarca(Long id) {
        log.info("Eliminando marca con ID: {}", id);
        
        if (!marcaRepository.existsById(id)) {
            throw new RuntimeException("Marca no encontrada");
        }
        
        marcaRepository.deleteById(id);
        log.info("Marca eliminada exitosamente con ID: {}", id);
    }

    // Operaciones de listado
    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> listarMarcas() {
        log.debug("Listando todas las marcas");
        return marcaMapper.toResponseDTOList(marcaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarcaResponseDTO> listarMarcas(Pageable pageable) {
        log.debug("Listando marcas paginadas: {}", pageable);
        return marcaRepository.findAll(pageable)
                .map(marcaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> listarMarcasActivas() {
        log.debug("Listando marcas activas");
        return marcaMapper.toResponseDTOList(marcaRepository.findByActivaTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarcaResponseDTO> listarMarcasActivas(Pageable pageable) {
        log.debug("Listando marcas activas paginadas: {}", pageable);
        return marcaRepository.findByActivaTrue(pageable)
                .map(marcaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> listarMarcasInactivas() {
        log.debug("Listando marcas inactivas");
        return marcaMapper.toResponseDTOList(marcaRepository.findByActivaFalse());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarcaResponseDTO> listarMarcasInactivas(Pageable pageable) {
        log.debug("Listando marcas inactivas paginadas: {}", pageable);
        return marcaRepository.findByActivaFalse(pageable)
                .map(marcaMapper::toResponseDTO);
    }

    // Operaciones de búsqueda
    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> buscarMarcasPorTexto(String texto) {
        log.debug("Buscando marcas por texto: {}", texto);
        return marcaMapper.toResponseDTOList(
                marcaRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivaTrue(texto)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarcaResponseDTO> buscarMarcasPorTexto(String texto, Pageable pageable) {
        log.debug("Buscando marcas por texto paginadas: {}", texto);
        return marcaRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivaTrue(texto, pageable)
                .map(marcaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> buscarMarcasPorPais(String pais) {
        log.debug("Buscando marcas por país: {}", pais);
        return marcaMapper.toResponseDTOList(
                marcaRepository.findByPaisAndActivaTrue(pais)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarcaResponseDTO> buscarMarcasPorPais(String pais, Pageable pageable) {
        log.debug("Buscando marcas por país paginadas: {}", pais);
        return marcaRepository.findByPaisAndActivaTrue(pais, pageable)
                .map(marcaMapper::toResponseDTO);
    }

    // Operaciones de estado
    @Override
    public MarcaResponseDTO activarMarca(Long id) {
        log.info("Activando marca con ID: {}", id);
        return cambiarEstadoMarca(id, true);
    }

    @Override
    public MarcaResponseDTO desactivarMarca(Long id) {
        log.info("Desactivando marca con ID: {}", id);
        return cambiarEstadoMarca(id, false);
    }

    @Override
    public MarcaResponseDTO marcarComoDestacada(Long id) {
        log.info("Marcando marca como destacada con ID: {}", id);
        return cambiarDestacadaMarca(id, true);
    }

    @Override
    public MarcaResponseDTO quitarDestacada(Long id) {
        log.info("Quitando destacada de la marca con ID: {}", id);
        return cambiarDestacadaMarca(id, false);
    }

    // Operaciones de conteo
    @Override
    @Transactional(readOnly = true)
    public long contarMarcas() {
        return marcaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarMarcasActivas() {
        return marcaRepository.countByActivaTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarMarcasInactivas() {
        return marcaRepository.countByActivaFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarMarcasDestacadas() {
        return marcaRepository.findByDestacadaTrueAndActivaTrue().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarMarcasPorPais(String pais) {
        return marcaRepository.findByPaisAndActivaTrue(pais).size();
    }

    // Métodos auxiliares privados
    private void validarUnicidadMarca(MarcaRequestDTO requestDTO) {
        validarUnicidadMarca(requestDTO, null);
    }

    private void validarUnicidadMarca(MarcaRequestDTO requestDTO, Long idExcluir) {
        if (marcaRepository.existsByNombre(requestDTO.getNombre())) {
            if (idExcluir == null || !marcaRepository.findByNombre(requestDTO.getNombre())
                    .map(Marca::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El nombre de marca ya existe");
            }
        }
        
        if (requestDTO.getSlug() != null && marcaRepository.existsBySlug(requestDTO.getSlug())) {
            if (idExcluir == null || !marcaRepository.findBySlug(requestDTO.getSlug())
                    .map(Marca::getId).orElse(0L).equals(idExcluir)) {
                throw new RuntimeException("El slug de marca ya existe");
            }
        }
    }

    private MarcaResponseDTO cambiarEstadoMarca(Long id, boolean activa) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        
        marca.setActiva(activa);
        marca.setUpdatedAt(LocalDateTime.now());
        
        Marca marcaActualizada = marcaRepository.save(marca);
        return marcaMapper.toResponseDTO(marcaActualizada);
    }

    private MarcaResponseDTO cambiarDestacadaMarca(Long id, boolean destacada) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        
        marca.setDestacada(destacada);
        marca.setUpdatedAt(LocalDateTime.now());
        
        Marca marcaActualizada = marcaRepository.save(marca);
        return marcaMapper.toResponseDTO(marcaActualizada);
    }

    // Implementaciones placeholder para métodos no implementados
    @Override
    public boolean existeMarcaPorId(Long id) {
        return marcaRepository.existsById(id);
    }

    @Override
    public boolean existeMarcaPorNombre(String nombre) {
        return marcaRepository.existsByNombre(nombre);
    }

    @Override
    public boolean existeMarcaPorSlug(String slug) {
        return marcaRepository.existsBySlug(slug);
    }

    @Override
    public List<MarcaResponseDTO> buscarMarcasPorPalabrasClave(String palabrasClave) {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> buscarMarcasPorPalabrasClave(String palabrasClave, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> filtrarMarcasPorEstado(Boolean activa) {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> filtrarMarcasPorEstado(Boolean activa, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> filtrarMarcasDestacadas() {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> filtrarMarcasDestacadas(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> filtrarMarcasPorOrden(Integer ordenMin, Integer ordenMax) {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> filtrarMarcasPorOrden(Integer ordenMin, Integer ordenMax, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> filtrarMarcasConSitioWeb() {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> filtrarMarcasConSitioWeb(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> filtrarMarcasConLogo() {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> filtrarMarcasConLogo(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> listarMarcasOrdenadasPorNombre() {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> listarMarcasOrdenadasPorNombre(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> listarMarcasOrdenadasPorOrden() {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> listarMarcasOrdenadasPorOrden(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> listarMarcasOrdenadasPorPais() {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> listarMarcasOrdenadasPorPais(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> listarMarcasOrdenadasPorFechaCreacion() {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> listarMarcasOrdenadasPorFechaCreacion(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<MarcaResponseDTO> listarMarcasOrdenadasPorFechaActualizacion() {
        return List.of();
    }

    @Override
    public Page<MarcaResponseDTO> listarMarcasOrdenadasPorFechaActualizacion(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public MarcaResponseDTO actualizarOrden(Long id, Integer nuevoOrden) {
        return null;
    }

    @Override
    public MarcaResponseDTO moverMarcaArriba(Long id) {
        return null;
    }

    @Override
    public MarcaResponseDTO moverMarcaAbajo(Long id) {
        return null;
    }

    @Override
    public MarcaResponseDTO moverMarcaAlInicio(Long id) {
        return null;
    }

    @Override
    public MarcaResponseDTO moverMarcaAlFinal(Long id) {
        return null;
    }

    @Override
    public List<ProductoResponseDTO> listarProductos(Long marcaId) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductos(Long marcaId, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<ProductoResponseDTO> listarProductosActivos(Long marcaId) {
        return List.of();
    }

    @Override
    public Page<ProductoResponseDTO> listarProductosActivos(Long marcaId, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public long contarProductos(Long marcaId) {
        return 0;
    }

    @Override
    public long contarProductosActivos(Long marcaId) {
        return 0;
    }

    @Override
    public boolean validarNombreUnico(String nombre, Long idExcluir) {
        return !marcaRepository.existsByNombre(nombre);
    }

    @Override
    public boolean validarSlugUnico(String slug, Long idExcluir) {
        return !marcaRepository.existsBySlug(slug);
    }

    @Override
    public boolean validarOrdenUnico(Integer orden, Long idExcluir) {
        return true;
    }

    @Override
    public boolean validarSitioWebValido(String sitioWeb) {
        return true;
    }

    @Override
    public boolean validarLogoValido(String logo) {
        return true;
    }

    @Override
    public long contarMarcasPorOrden(Integer ordenMin, Integer ordenMax) {
        return 0;
    }

    @Override
    public long contarMarcasPorEstado(Boolean activa) {
        return 0;
    }

    @Override
    public long contarMarcasPorTexto(String texto) {
        return 0;
    }

    @Override
    public long contarMarcasPorPalabrasClave(String palabrasClave) {
        return 0;
    }

    @Override
    public long contarMarcasConSitioWeb() {
        return 0;
    }

    @Override
    public long contarMarcasConLogo() {
        return 0;
    }

    @Override
    public long contarMarcasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return 0;
    }
}
