package com.Carrito.compras.web.mapper;

import com.Carrito.compras.domain.entity.Carrito;
import com.Carrito.compras.domain.entity.ItemCarrito;
import com.Carrito.compras.web.dto.CarritoResponseDTO;
import com.Carrito.compras.web.dto.ItemCarritoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarritoMapper {
    
    @Mapping(target = "cantidadTotalItems", source = "carrito", qualifiedByName = "calcularCantidadTotal")
    CarritoResponseDTO toResponseDTO(Carrito carrito);
    
    ItemCarritoResponseDTO toResponseDTO(ItemCarrito itemCarrito);
    
    List<ItemCarritoResponseDTO> toResponseDTOList(List<ItemCarrito> items);
    
    @Named("calcularCantidadTotal")
    default Integer calcularCantidadTotal(Carrito carrito) {
        return carrito.getItems().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }
}
