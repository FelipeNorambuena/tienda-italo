package com.Carrito.compras.application.service;

import com.Carrito.compras.domain.entity.Carrito;
import com.Carrito.compras.domain.entity.ItemCarrito;
import com.Carrito.compras.web.dto.AgregarItemRequestDTO;
import com.Carrito.compras.web.dto.CarritoResponseDTO;
import com.Carrito.compras.web.dto.WhatsAppPedidoDTO;

import java.util.List;

public interface CarritoService {
    
    /**
     * Obtiene el carrito activo de un usuario
     */
    CarritoResponseDTO obtenerCarritoUsuario(Long usuarioId);
    
    /**
     * Crea un nuevo carrito para un usuario
     */
    CarritoResponseDTO crearCarrito(Long usuarioId);
    
    /**
     * Agrega un producto al carrito
     */
    CarritoResponseDTO agregarProducto(Long usuarioId, AgregarItemRequestDTO request);
    
    /**
     * Actualiza la cantidad de un producto en el carrito
     */
    CarritoResponseDTO actualizarCantidad(Long usuarioId, Long itemId, Integer nuevaCantidad);
    
    /**
     * Remueve un producto del carrito
     */
    CarritoResponseDTO removerProducto(Long usuarioId, Long itemId);
    
    /**
     * Limpia el carrito de un usuario
     */
    CarritoResponseDTO limpiarCarrito(Long usuarioId);
    
    /**
     * Genera el pedido para WhatsApp
     */
    WhatsAppPedidoDTO generarPedidoWhatsApp(Long usuarioId);
    
    /**
     * Finaliza la compra y redirige a WhatsApp
     */
    String finalizarCompra(Long usuarioId);
}
