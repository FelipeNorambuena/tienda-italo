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
@Mapper(componentModel = "spring", uses = {CategoriaMapper.class, MarcaMapper.class, AtributoProductoMapper.class, ImagenProductoMapper.class, ProductoRelacionadoMapper.class})
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
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "categoriaToResponseDTO")
    @Mapping(target = "marca", source = "marca", qualifiedByName = "marcaToResponseDTO")
    @Mapping(target = "atributos", source = "atributos", qualifiedByName = "atributosToResponseDTOList")
    @Mapping(target = "imagenes", source = "imagenes", qualifiedByName = "imagenesToResponseDTOList")
    @Mapping(target = "productosRelacionados", source = "productosRelacionados", qualifiedByName = "productosRelacionadosToResponseDTOList")
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
    @Mapping(target = "tieneSiguiente", source = "hasNext")
    @Mapping(target = "tieneAnterior", source = "hasPrevious")
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
    @Mapping(target = "productosMasVendidos", expression = "java(java.util.List.of(toResponseDTO(producto)))")
    @Mapping(target = "productosMasVistos", expression = "java(java.util.List.of(toResponseDTO(producto)))")
    @Mapping(target = "productosMejorCalificados", expression = "java(java.util.List.of(toResponseDTO(producto)))")
    @Mapping(target = "productosNuevos", expression = "java(java.util.List.of(toResponseDTO(producto)))")
    @Mapping(target = "productosEnOferta", expression = "java(java.util.List.of(toResponseDTO(producto)))")
    @Mapping(target = "productosConStockBajo", expression = "java(java.util.List.of(toResponseDTO(producto)))")
    @Mapping(target = "productosSinStock", expression = "java(java.util.List.of(toResponseDTO(producto)))")
    @Mapping(target = "fechaGeneracion", expression = "java(java.time.LocalDateTime.now())")
    ProductoEstadisticasDTO toEstadisticasDTO(Producto producto);
    
    // Métodos auxiliares para resolver ambigüedades de MapStruct
    @Named("categoriaToResponseDTO")
    default CategoriaResponseDTO categoriaToResponseDTO(Categoria categoria) {
        if (categoria == null) return null;
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .slug(categoria.getSlug())
                .activa(categoria.getActiva())
                .destacada(categoria.getDestacada())
                .orden(categoria.getOrden())
                .nivel(categoria.getNivel())
                .rutaCompleta(categoria.getRutaCompleta())
                .totalProductos(categoria.getTotalProductos())
                .totalProductosActivos(categoria.getTotalProductosActivos())
                .totalSubcategorias(categoria.getTotalSubcategorias())
                .totalSubcategoriasActivas(categoria.getTotalSubcategoriasActivas())
                .esVisible(categoria.esVisible())
                .createdAt(categoria.getCreatedAt())
                .updatedAt(categoria.getUpdatedAt())
                .build();
    }
    
    @Named("marcaToResponseDTO")
    default MarcaResponseDTO marcaToResponseDTO(Marca marca) {
        if (marca == null) return null;
        return MarcaResponseDTO.builder()
                .id(marca.getId())
                .nombre(marca.getNombre())
                .descripcion(marca.getDescripcion())
                .slug(marca.getSlug())
                .activa(marca.getActiva())
                .destacada(marca.getDestacada())
                .orden(marca.getOrden())
                .logoUrl(marca.getLogoUrl())
                .sitioWeb(marca.getSitioWeb())
                .paisOrigen(marca.getPaisOrigen())
                .anoFundacion(marca.getAnoFundacion())
                .palabrasClave(marca.getPalabrasClave())
                .createdAt(marca.getCreatedAt())
                .updatedAt(marca.getUpdatedAt())
                .build();
    }
    
    @Named("atributosToResponseDTOList")
    default List<AtributoProductoResponseDTO> atributosToResponseDTOList(List<AtributoProducto> atributos) {
        if (atributos == null) return null;
        return atributos.stream()
                .map(this::atributoToResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Named("imagenesToResponseDTOList")
    default List<ImagenProductoResponseDTO> imagenesToResponseDTOList(List<ImagenProducto> imagenes) {
        if (imagenes == null) return null;
        return imagenes.stream()
                .map(this::imagenToResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Named("productosRelacionadosToResponseDTOList")
    default List<ProductoRelacionadoResponseDTO> productosRelacionadosToResponseDTOList(List<ProductoRelacionado> productosRelacionados) {
        if (productosRelacionados == null) return null;
        return productosRelacionados.stream()
                .map(this::productoRelacionadoToResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    // Métodos auxiliares para conversiones individuales
    default AtributoProductoResponseDTO atributoToResponseDTO(AtributoProducto atributo) {
        if (atributo == null) return null;
        return AtributoProductoResponseDTO.builder()
                .id(atributo.getId())
                .nombre(atributo.getNombre())
                .valor(atributo.getValor())
                .unidad(atributo.getUnidad())
                .valorCompleto(atributo.getValorCompleto())
                .nombreCompleto(atributo.getNombreCompleto())
                .esVisible(atributo.getEsVisible())
                .esFiltrable(atributo.getEsFiltrable())
                .orden(atributo.getOrden())
                .tipo(atributo.getTipo())
                .opcionesLista(atributo.getOpcionesLista())
                .tieneOpciones(atributo.tieneOpciones())
                .puedeFiltrarse(atributo.puedeFiltrarse())
                .esNumerico(atributo.esNumerico())
                .esTexto(atributo.esTexto())
                .esBooleano(atributo.esBooleano())
                .esLista(atributo.esLista())
                .createdAt(atributo.getCreatedAt())
                .updatedAt(atributo.getUpdatedAt())
                .build();
    }
    
    default ImagenProductoResponseDTO imagenToResponseDTO(ImagenProducto imagen) {
        if (imagen == null) return null;
        return ImagenProductoResponseDTO.builder()
                .id(imagen.getId())
                .url(imagen.getUrl())
                .urlThumbnail(imagen.getUrlThumbnail())
                .urlMiniatura(imagen.getUrlMiniatura())
                .alt(imagen.getAlt())
                .titulo(imagen.getTitulo())
                .descripcion(imagen.getDescripcion())
                .esPrincipal(imagen.getEsPrincipal())
                .orden(imagen.getOrden())
                .ancho(imagen.getAncho())
                .alto(imagen.getAlto())
                .tamañoBytes(imagen.getTamañoBytes())
                .formato(imagen.getFormato())
                .esImagenHorizontal(imagen.esImagenHorizontal())
                .esImagenVertical(imagen.esImagenVertical())
                .orientacion(imagen.getOrientacion())
                .createdAt(imagen.getCreatedAt())
                .updatedAt(imagen.getUpdatedAt())
                .build();
    }
    
    default ProductoRelacionadoResponseDTO productoRelacionadoToResponseDTO(ProductoRelacionado relacion) {
        if (relacion == null) return null;
        return ProductoRelacionadoResponseDTO.builder()
                .id(relacion.getId())
                .productoRelacionadoId(relacion.getProductoRelacionadoId())
                .tipoRelacion(relacion.getTipoRelacion())
                .tipoRelacionCompleto(relacion.getTipoRelacionCompleto())
                .descripcion(relacion.getDescripcion())
                .descripcionCompleta(relacion.getDescripcionCompleta())
                .esSimetrica(relacion.getEsSimetrica())
                .esRelacionSimetrica(relacion.esRelacionSimetrica())
                .esRelacionAsimetrica(relacion.esRelacionAsimetrica())
                .esRelacionBidireccional(relacion.esRelacionBidireccional())
                .esRelacionUnidireccional(relacion.esRelacionUnidireccional())
                .tipoRelacionDescripcion(relacion.getTipoRelacionDescripcion())
                .orden(relacion.getOrden())
                .activa(relacion.getActiva())
                .createdAt(relacion.getCreatedAt())
                .updatedAt(relacion.getUpdatedAt())
                .build();
    }
}
