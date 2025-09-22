package com.tienda.producto.web.mapper;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.domain.entity.Categoria;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct para conversión entre Categoria y DTOs.
 * 
 * @author Tienda Italo Team
 */
@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface CategoriaMapper {

    // Conversión de RequestDTO a Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoriaPadre", ignore = true)
    @Mapping(target = "subcategorias", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Categoria toEntity(CategoriaRequestDTO requestDTO);

    // Conversión de Entity a ResponseDTO
    @Mapping(target = "nombreCompleto", expression = "java(categoria.getNombreCompleto())")
    @Mapping(target = "esCategoriaRaiz", expression = "java(categoria.esCategoriaRaiz())")
    @Mapping(target = "esSubcategoria", expression = "java(categoria.esSubcategoria())")
    @Mapping(target = "nivel", expression = "java(categoria.getNivel())")
    @Mapping(target = "rutaCompleta", expression = "java(categoria.getRutaCompleta())")
    @Mapping(target = "totalProductos", expression = "java(categoria.getTotalProductos())")
    @Mapping(target = "totalProductosActivos", expression = "java(categoria.getTotalProductosActivos())")
    @Mapping(target = "totalSubcategorias", expression = "java(categoria.getTotalSubcategorias())")
    @Mapping(target = "totalSubcategoriasActivas", expression = "java(categoria.getTotalSubcategoriasActivas())")
    @Mapping(target = "esVisible", expression = "java(categoria.esVisible())")
    @Mapping(target = "categoriaPadre", source = "categoriaPadre", qualifiedByName = "categoriaToResponseDTO")
    @Mapping(target = "subcategorias", source = "subcategorias", qualifiedByName = "categoriasToResponseDTOList")
    CategoriaResponseDTO toResponseDTO(Categoria categoria);

    // Conversión de lista de entidades a lista de DTOs
    List<CategoriaResponseDTO> toResponseDTOList(List<Categoria> categorias);

    // Conversión de página de entidades a página de DTOs
    @Mapping(target = "categorias", source = "content", qualifiedByName = "categoriasToResponseDTOList")
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
    CategoriaPageResponseDTO toPageResponseDTO(org.springframework.data.domain.Page<Categoria> page);

    // Actualización de entidad desde DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoriaPadre", ignore = true)
    @Mapping(target = "subcategorias", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CategoriaRequestDTO requestDTO, @MappingTarget Categoria categoria);

    // Conversión de entidad a DTO simplificado (sin relaciones)
    @Mapping(target = "categoriaPadre", ignore = true)
    @Mapping(target = "subcategorias", ignore = true)
    @Mapping(target = "productos", ignore = true)
    CategoriaResponseDTO toSimpleResponseDTO(Categoria categoria);

    // Conversión de lista de entidades a lista de DTOs simplificados
    List<CategoriaResponseDTO> toSimpleResponseDTOList(List<Categoria> categorias);
    
    // Métodos auxiliares para resolver ambigüedades de MapStruct
    @Named("categoriaToResponseDTO")
    default CategoriaResponseDTO categoriaToResponseDTO(Categoria categoria) {
        return toResponseDTO(categoria);
    }
    
    @Named("categoriasToResponseDTOList")
    default List<CategoriaResponseDTO> categoriasToResponseDTOList(List<Categoria> categorias) {
        return toResponseDTOList(categorias);
    }
}
