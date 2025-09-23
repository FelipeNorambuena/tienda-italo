package com.tienda.producto.web.mapper;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.domain.entity.AtributoProducto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct para conversión entre AtributoProducto y DTOs.
 * 
 * @author Tienda Italo Team
 */
@Mapper(componentModel = "spring")
public interface AtributoProductoMapper {

    // Conversión de RequestDTO a Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AtributoProducto toEntity(AtributoProductoRequestDTO requestDTO);

    // Conversión de Entity a ResponseDTO
    @Mapping(target = "valorCompleto", expression = "java(atributo.getValorCompleto())")
    @Mapping(target = "nombreCompleto", expression = "java(atributo.getNombreCompleto())")
    @Mapping(target = "esNumerico", expression = "java(atributo.esNumerico())")
    @Mapping(target = "esTexto", expression = "java(atributo.esTexto())")
    @Mapping(target = "esBooleano", expression = "java(atributo.esBooleano())")
    @Mapping(target = "esLista", expression = "java(atributo.esLista())")
    @Mapping(target = "opcionesLista", expression = "java(atributo.getOpcionesLista())")
    @Mapping(target = "tieneOpciones", expression = "java(atributo.tieneOpciones())")
    @Mapping(target = "puedeFiltrarse", expression = "java(atributo.puedeFiltrarse())")
    AtributoProductoResponseDTO toResponseDTO(AtributoProducto atributo);

    // Conversión de lista de entidades a lista de DTOs
    List<AtributoProductoResponseDTO> toResponseDTOList(List<AtributoProducto> atributos);

    // Actualización de entidad desde DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(AtributoProductoRequestDTO requestDTO, @MappingTarget AtributoProducto atributo);

}
