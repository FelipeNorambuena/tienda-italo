package com.tienda.producto.web.mapper;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.domain.entity.ImagenProducto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct para conversión entre ImagenProducto y DTOs.
 * 
 * @author Tienda Italo Team
 */
@Mapper(componentModel = "spring")
public interface ImagenProductoMapper {

    // Conversión de RequestDTO a Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ImagenProducto toEntity(ImagenProductoRequestDTO requestDTO);

    // Conversión de Entity a ResponseDTO
    @Mapping(target = "urlCompleta", expression = "java(imagen.getUrlCompleta())")
    @Mapping(target = "nombreCompleto", expression = "java(imagen.getNombreCompleto())")
    @Mapping(target = "tamanioFormateado", expression = "java(imagen.getTamanioFormateado())")
    @Mapping(target = "dimensiones", expression = "java(imagen.getDimensiones())")
    @Mapping(target = "aspectRatio", expression = "java(imagen.getAspectRatio())")
    @Mapping(target = "esImagenValida", expression = "java(imagen.esImagenValida())")
    @Mapping(target = "esImagenGrande", expression = "java(imagen.esImagenGrande())")
    @Mapping(target = "esImagenPequena", expression = "java(imagen.esImagenPequena())")
    @Mapping(target = "esImagenCuadrada", expression = "java(imagen.esImagenCuadrada())")
    @Mapping(target = "esImagenHorizontal", expression = "java(imagen.esImagenHorizontal())")
    @Mapping(target = "esImagenVertical", expression = "java(imagen.esImagenVertical())")
    @Mapping(target = "orientacion", expression = "java(imagen.getOrientacion())")
    ImagenProductoResponseDTO toResponseDTO(ImagenProducto imagen);

    // Conversión de lista de entidades a lista de DTOs
    List<ImagenProductoResponseDTO> toResponseDTOList(List<ImagenProducto> imagenes);

    // Actualización de entidad desde DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(ImagenProductoRequestDTO requestDTO, @MappingTarget ImagenProducto imagen);

}
