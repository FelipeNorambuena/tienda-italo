package com.tienda.producto.web.mapper;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.domain.entity.ProductoRelacionado;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct para conversión entre ProductoRelacionado y DTOs.
 * 
 * @author Tienda Italo Team
 */
@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface ProductoRelacionadoMapper {

    // Conversión de Entity a ResponseDTO
    @Mapping(target = "tipoRelacionCompleto", expression = "java(relacion.getTipoRelacionCompleto())")
    @Mapping(target = "descripcionCompleta", expression = "java(relacion.getDescripcionCompleta())")
    @Mapping(target = "esRelacionSimetrica", expression = "java(relacion.esRelacionSimetrica())")
    @Mapping(target = "esRelacionAsimetrica", expression = "java(relacion.esRelacionAsimetrica())")
    @Mapping(target = "esRelacionFuerte", expression = "java(relacion.esRelacionFuerte())")
    @Mapping(target = "esRelacionMedia", expression = "java(relacion.esRelacionMedia())")
    @Mapping(target = "esRelacionDebil", expression = "java(relacion.esRelacionDebil())")
    @Mapping(target = "nivelRelacion", expression = "java(relacion.getNivelRelacion())")
    @Mapping(target = "nombreRelacion", expression = "java(relacion.getNombreRelacion())")
    @Mapping(target = "esRelacionBidireccional", expression = "java(relacion.esRelacionBidireccional())")
    @Mapping(target = "esRelacionUnidireccional", expression = "java(relacion.esRelacionUnidireccional())")
    @Mapping(target = "tipoRelacionDescripcion", expression = "java(relacion.getTipoRelacionDescripcion())")
    ProductoRelacionadoResponseDTO toResponseDTO(ProductoRelacionado relacion);

    // Conversión de lista de entidades a lista de DTOs
    List<ProductoRelacionadoResponseDTO> toResponseDTOList(List<ProductoRelacionado> relaciones);

    // Conversión de entidad a DTO simplificado
    @Mapping(target = "tipoRelacionCompleto", expression = "java(relacion.getTipoRelacionCompleto())")
    @Mapping(target = "descripcionCompleta", expression = "java(relacion.getDescripcionCompleta())")
    @Mapping(target = "esRelacionSimetrica", expression = "java(relacion.esRelacionSimetrica())")
    @Mapping(target = "esRelacionAsimetrica", expression = "java(relacion.esRelacionAsimetrica())")
    @Mapping(target = "esRelacionFuerte", expression = "java(relacion.esRelacionFuerte())")
    @Mapping(target = "esRelacionMedia", expression = "java(relacion.esRelacionMedia())")
    @Mapping(target = "esRelacionDebil", expression = "java(relacion.esRelacionDebil())")
    @Mapping(target = "nivelRelacion", expression = "java(relacion.getNivelRelacion())")
    @Mapping(target = "nombreRelacion", expression = "java(relacion.getNombreRelacion())")
    @Mapping(target = "esRelacionBidireccional", expression = "java(relacion.esRelacionBidireccional())")
    @Mapping(target = "esRelacionUnidireccional", expression = "java(relacion.esRelacionUnidireccional())")
    @Mapping(target = "tipoRelacionDescripcion", expression = "java(relacion.getTipoRelacionDescripcion())")
    ProductoRelacionadoResponseDTO toSimpleResponseDTO(ProductoRelacionado relacion);

    // Conversión de lista de entidades a lista de DTOs simplificados
    List<ProductoRelacionadoResponseDTO> toSimpleResponseDTOList(List<ProductoRelacionado> relaciones);
    
    // Métodos auxiliares para resolver ambigüedades de MapStruct
    @Named("productosRelacionadosToResponseDTOList")
    default List<ProductoRelacionadoResponseDTO> productosRelacionadosToResponseDTOList(List<ProductoRelacionado> relaciones) {
        return toResponseDTOList(relaciones);
    }
}
