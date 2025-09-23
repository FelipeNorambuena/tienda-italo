package com.tienda.producto.web.mapper;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.domain.entity.*;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct para conversión entre Producto y DTOs.
 * 
 * @author Tienda Italo Team
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper {

    // Conversión de RequestDTO a Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "atributos", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    @Mapping(target = "productosRelacionados", ignore = true)
    @Mapping(target = "vendidos", constant = "0")
    @Mapping(target = "visualizaciones", constant = "0")
    @Mapping(target = "calificacionPromedio", constant = "0.0")
    @Mapping(target = "totalCalificaciones", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Producto toEntity(ProductoRequestDTO requestDTO);

    // Conversión de Entity a ResponseDTO
    @Mapping(target = "precioFinal", expression = "java(producto.getPrecioFinal())")
    @Mapping(target = "descuento", expression = "java(producto.getDescuento())")
    @Mapping(target = "porcentajeDescuento", expression = "java(producto.getPorcentajeDescuento())")
    @Mapping(target = "nombreCompleto", expression = "java(producto.getNombreCompleto())")
    @Mapping(target = "estaEnOferta", expression = "java(producto.estaEnOferta())")
    @Mapping(target = "tieneStock", expression = "java(producto.tieneStock())")
    @Mapping(target = "necesitaReposicion", expression = "java(producto.necesitaReposicion())")
    @Mapping(target = "estadoStock", expression = "java(producto.getEstadoStock())")
    @Mapping(target = "esDisponible", expression = "java(producto.esDisponible())")
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "atributos", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    @Mapping(target = "productosRelacionados", ignore = true)
    ProductoResponseDTO toResponseDTO(Producto producto);

    // Conversión de lista de entidades a lista de DTOs
    List<ProductoResponseDTO> toResponseDTOList(List<Producto> productos);

    // Conversión de página de entidades a página de DTOs
    @Mapping(target = "productos", source = "content")
    @Mapping(target = "pagina", source = "number")
    @Mapping(target = "tamanio", source = "size")
    @Mapping(target = "totalElementos", source = "totalElements")
    @Mapping(target = "totalPaginas", source = "totalPages")
    @Mapping(target = "primeraPagina", source = "first")
    @Mapping(target = "ultimaPagina", source = "last")
    @Mapping(target = "tieneSiguiente", expression = "java(!page.isLast())")
    @Mapping(target = "tieneAnterior", expression = "java(!page.isFirst())")
    @Mapping(target = "numeroElementos", source = "numberOfElements")
    @Mapping(target = "numeroElementosTotal", source = "totalElements")
    ProductoPageResponseDTO toPageResponseDTO(org.springframework.data.domain.Page<Producto> page);

    // Actualización de entidad desde DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "atributos", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    @Mapping(target = "productosRelacionados", ignore = true)
    @Mapping(target = "vendidos", ignore = true)
    @Mapping(target = "visualizaciones", ignore = true)
    @Mapping(target = "calificacionPromedio", ignore = true)
    @Mapping(target = "totalCalificaciones", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(ProductoRequestDTO requestDTO, @MappingTarget Producto producto);

    // Conversión de filtro a criterios de búsqueda
    @Mapping(target = "categoriaIds", source = "categoriaIds")
    @Mapping(target = "marcaIds", source = "marcaIds")
    @Mapping(target = "precioMin", source = "precioMin")
    @Mapping(target = "precioMax", source = "precioMax")
    @Mapping(target = "enOferta", source = "enOferta")
    @Mapping(target = "destacado", source = "destacado")
    @Mapping(target = "nuevo", source = "nuevo")
    @Mapping(target = "stockMin", source = "stockMin")
    @Mapping(target = "stockMax", source = "stockMax")
    @Mapping(target = "calificacionMin", source = "calificacionMin")
    @Mapping(target = "color", source = "color")
    @Mapping(target = "material", source = "material")
    @Mapping(target = "talla", source = "talla")
    @Mapping(target = "ordenarPor", source = "ordenarPor")
    @Mapping(target = "direccionOrden", source = "direccionOrden")
    @Mapping(target = "pagina", source = "pagina")
    @Mapping(target = "tamanio", source = "tamanio")
    ProductoBusquedaDTO toBusquedaDTO(ProductoFiltroDTO filtroDTO);

    // Conversión de entidad a estadísticas
    @Mapping(target = "totalProductos", expression = "java(1L)")
    @Mapping(target = "totalProductosActivos", expression = "java(producto.getActivo() ? 1L : 0L)")
    @Mapping(target = "totalProductosInactivos", expression = "java(producto.getActivo() ? 0L : 1L)")
    @Mapping(target = "totalProductosDestacados", expression = "java(producto.getDestacado() ? 1L : 0L)")
    @Mapping(target = "totalProductosNuevos", expression = "java(producto.getNuevo() ? 1L : 0L)")
    @Mapping(target = "totalProductosEnOferta", expression = "java(producto.estaEnOferta() ? 1L : 0L)")
    @Mapping(target = "totalProductosSinStock", expression = "java(producto.getStock() == 0 ? 1L : 0L)")
    @Mapping(target = "totalProductosConStockBajo", expression = "java(producto.necesitaReposicion() ? 1L : 0L)")
    @Mapping(target = "totalCategorias", expression = "java(1L)")
    @Mapping(target = "totalMarcas", expression = "java(producto.getMarca() != null ? 1L : 0L)")
    @Mapping(target = "precioPromedio", source = "precio")
    @Mapping(target = "precioMinimo", source = "precio")
    @Mapping(target = "precioMaximo", source = "precio")
    @Mapping(target = "calificacionPromedio", source = "calificacionPromedio")
    @Mapping(target = "totalVendidos", source = "vendidos")
    @Mapping(target = "totalVisualizaciones", source = "visualizaciones")
    @Mapping(target = "productosMasVendidos", ignore = true)
    @Mapping(target = "productosMasVistos", ignore = true)
    @Mapping(target = "productosMejorCalificados", ignore = true)
    @Mapping(target = "productosNuevos", ignore = true)
    @Mapping(target = "productosEnOferta", ignore = true)
    @Mapping(target = "productosConStockBajo", ignore = true)
    @Mapping(target = "productosSinStock", ignore = true)
    @Mapping(target = "productosPorCategoria", ignore = true)
    @Mapping(target = "productosPorMarca", ignore = true)
    @Mapping(target = "productosPorColor", ignore = true)
    @Mapping(target = "productosPorMaterial", ignore = true)
    @Mapping(target = "productosPorTalla", ignore = true)
    @Mapping(target = "fechaGeneracion", expression = "java(java.time.LocalDateTime.now())")
    ProductoEstadisticasDTO toEstadisticasDTO(Producto producto);
}