package com.tienda.producto.web.mapper;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.domain.entity.Marca;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct para conversión entre Marca y DTOs.
 * 
 * @author Tienda Italo Team
 */
@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface MarcaMapper {

    // Conversión de RequestDTO a Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Marca toEntity(MarcaRequestDTO requestDTO);

    // Conversión de Entity a ResponseDTO
    @Mapping(target = "nombreCompleto", expression = "java(marca.getNombreCompleto())")
    @Mapping(target = "paisCompleto", expression = "java(marca.getPaisCompleto())")
    @Mapping(target = "esVisible", expression = "java(marca.esVisible())")
    @Mapping(target = "tieneSitioWeb", expression = "java(marca.tieneSitioWeb())")
    @Mapping(target = "tieneLogo", expression = "java(marca.tieneLogo())")
    MarcaResponseDTO toResponseDTO(Marca marca);

    // Conversión de lista de entidades a lista de DTOs
    List<MarcaResponseDTO> toResponseDTOList(List<Marca> marcas);

    // Conversión de página de entidades a página de DTOs
    @Mapping(target = "marcas", source = "content", qualifiedByName = "marcasToResponseDTOList")
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
    MarcaPageResponseDTO toPageResponseDTO(org.springframework.data.domain.Page<Marca> page);

    // Actualización de entidad desde DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(MarcaRequestDTO requestDTO, @MappingTarget Marca marca);

    // Conversión de entidad a DTO simplificado (sin relaciones)
    @Mapping(target = "productos", ignore = true)
    MarcaResponseDTO toSimpleResponseDTO(Marca marca);

    // Conversión de lista de entidades a lista de DTOs simplificados
    List<MarcaResponseDTO> toSimpleResponseDTOList(List<Marca> marcas);
    
    // Métodos auxiliares para resolver ambigüedades de MapStruct
    @Named("marcasToResponseDTOList")
    default List<MarcaResponseDTO> marcasToResponseDTOList(List<Marca> marcas) {
        return toResponseDTOList(marcas);
    }
}
