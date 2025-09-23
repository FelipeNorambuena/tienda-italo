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
@Mapper(componentModel = "spring")
public interface ProductoRelacionadoMapper {

    // Conversión de Entity a ResponseDTO
    @Mapping(target = "tipoRelacionCompleto", expression = "java(relacion.getTipoRelacionCompleto())")
    @Mapping(target = "descripcionCompleta", expression = "java(relacion.getDescripcionCompleta())")
    @Mapping(target = "esRelacionSimetrica", expression = "java(relacion.esRelacionSimetrica())")
    @Mapping(target = "esRelacionAsimetrica", expression = "java(relacion.esRelacionAsimetrica())")
    @Mapping(target = "esRelacionFuerte", expression = "java(relacion.esRelacionFuerte())")
    @Mapping(target = "esRelacionMedia", expression = "java(relacion.esRelacionMedia())")
    @Mapping(target = "esRelacionDebil", expression = "java(relacion.esRelacionDebil())")
    @Mapping(target = "esRelacionBidireccional", expression = "java(relacion.esRelacionBidireccional())")
    @Mapping(target = "esRelacionUnidireccional", expression = "java(relacion.esRelacionUnidireccional())")
    @Mapping(target = "tipoRelacionDescripcion", expression = "java(relacion.getTipoRelacionDescripcion())")
    ProductoRelacionadoResponseDTO toResponseDTO(ProductoRelacionado relacion);

    // Conversión de lista de entidades a lista de DTOs
    List<ProductoRelacionadoResponseDTO> toResponseDTOList(List<ProductoRelacionado> relaciones);

}
