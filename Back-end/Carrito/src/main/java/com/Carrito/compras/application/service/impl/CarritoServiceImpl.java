package com.Carrito.compras.application.service.impl;

import com.Carrito.compras.application.service.CarritoService;
import com.Carrito.compras.domain.entity.Carrito;
import com.Carrito.compras.domain.entity.ItemCarrito;
import com.Carrito.compras.infrastructure.repository.CarritoJpaRepository;
import com.Carrito.compras.infrastructure.repository.ItemCarritoJpaRepository;
import com.Carrito.compras.web.dto.AgregarItemRequestDTO;
import com.Carrito.compras.web.dto.CarritoResponseDTO;
import com.Carrito.compras.web.dto.WhatsAppPedidoDTO;
import com.Carrito.compras.web.mapper.CarritoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CarritoServiceImpl implements CarritoService {
    
    private final CarritoJpaRepository carritoRepository;
    private final ItemCarritoJpaRepository itemCarritoRepository;
    private final CarritoMapper carritoMapper;
    
    @Value("${whatsapp.number}")
    private String numeroWhatsApp;
    
    @Value("${whatsapp.message.template}")
    private String templateMensaje;
    
    @Override
    @Transactional(readOnly = true)
    public CarritoResponseDTO obtenerCarritoUsuario(Long usuarioId) {
        log.info("Obteniendo carrito para usuario: {}", usuarioId);
        
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
                .orElseGet(() -> crearCarritoEntity(usuarioId));
        
        return carritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    public CarritoResponseDTO crearCarrito(Long usuarioId) {
        log.info("Creando nuevo carrito para usuario: {}", usuarioId);
        
        // Desactivar carritos anteriores
        carritoRepository.desactivarCarritosByUsuarioId(usuarioId);
        
        Carrito carrito = crearCarritoEntity(usuarioId);
        carrito = carritoRepository.save(carrito);
        
        return carritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    public CarritoResponseDTO agregarProducto(Long usuarioId, AgregarItemRequestDTO request) {
        log.info("Agregando producto {} al carrito del usuario {}", request.getProductoId(), usuarioId);
        
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        
        // Verificar si el producto ya existe en el carrito
        ItemCarrito itemExistente = itemCarritoRepository
                .findByCarritoIdAndProductoId(carrito.getId(), request.getProductoId())
                .orElse(null);
        
        if (itemExistente != null) {
            // Actualizar cantidad del producto existente
            itemExistente.actualizarCantidad(itemExistente.getCantidad() + request.getCantidad());
            itemCarritoRepository.save(itemExistente);
        } else {
            // Crear nuevo item
            ItemCarrito nuevoItem = ItemCarrito.builder()
                    .carrito(carrito)
                    .productoId(request.getProductoId())
                    .nombreProducto(request.getNombreProducto())
                    .precioUnitario(request.getPrecioUnitario())
                    .cantidad(request.getCantidad())
                    .build();
            
            nuevoItem.calcularSubtotal();
            itemCarritoRepository.save(nuevoItem);
        }
        
        // Recalcular total del carrito
        carrito.calcularTotal();
        carrito = carritoRepository.save(carrito);
        
        return carritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    public CarritoResponseDTO actualizarCantidad(Long usuarioId, Long itemId, Integer nuevaCantidad) {
        log.info("Actualizando cantidad del item {} a {} para usuario {}", itemId, nuevaCantidad, usuarioId);
        
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        
        if (!item.getCarrito().getId().equals(carrito.getId())) {
            throw new RuntimeException("El item no pertenece al carrito del usuario");
        }
        
        item.actualizarCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);
        
        carrito.calcularTotal();
        carrito = carritoRepository.save(carrito);
        
        return carritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    public CarritoResponseDTO removerProducto(Long usuarioId, Long itemId) {
        log.info("Removiendo item {} del carrito del usuario {}", itemId, usuarioId);
        
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        
        if (!item.getCarrito().getId().equals(carrito.getId())) {
            throw new RuntimeException("El item no pertenece al carrito del usuario");
        }
        
        itemCarritoRepository.delete(item);
        
        carrito.calcularTotal();
        carrito = carritoRepository.save(carrito);
        
        return carritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    public CarritoResponseDTO limpiarCarrito(Long usuarioId) {
        log.info("Limpiando carrito del usuario {}", usuarioId);
        
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        carrito.limpiar();
        carrito = carritoRepository.save(carrito);
        
        return carritoMapper.toResponseDTO(carrito);
    }
    
    @Override
    @Transactional(readOnly = true)
    public WhatsAppPedidoDTO generarPedidoWhatsApp(Long usuarioId) {
        log.info("Generando pedido WhatsApp para usuario {}", usuarioId);
        
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        
        if (carrito.estaVacio()) {
            throw new RuntimeException("El carrito está vacío");
        }
        
        List<WhatsAppPedidoDTO.ProductoPedidoDTO> productos = carrito.getItems().stream()
                .map(item -> WhatsAppPedidoDTO.ProductoPedidoDTO.builder()
                        .nombre(item.getNombreProducto())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());
        
        String productosTexto = productos.stream()
                .map(p -> String.format("• %s x%d - $%.0f", p.getNombre(), p.getCantidad(), p.getSubtotal()))
                .collect(Collectors.joining("\n"));
        
        String mensaje = templateMensaje
                .replace("{productos}", productosTexto)
                .replace("${total}", String.format("$%.0f", carrito.getTotal()));
        
        String urlWhatsApp = String.format("https://wa.me/%s?text=%s", 
                numeroWhatsApp.replace("+", ""), 
                URLEncoder.encode(mensaje, StandardCharsets.UTF_8));
        
        return WhatsAppPedidoDTO.builder()
                .numeroWhatsApp(numeroWhatsApp)
                .mensaje(mensaje)
                .productos(productos)
                .total(carrito.getTotal())
                .urlWhatsApp(urlWhatsApp)
                .build();
    }
    
    @Override
    public String finalizarCompra(Long usuarioId) {
        log.info("Finalizando compra para usuario {}", usuarioId);
        
        WhatsAppPedidoDTO pedido = generarPedidoWhatsApp(usuarioId);
        
        // Desactivar el carrito después de finalizar la compra
        Carrito carrito = carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        
        carrito.setActivo(false);
        carritoRepository.save(carrito);
        
        log.info("Compra finalizada. Redirigiendo a WhatsApp: {}", pedido.getUrlWhatsApp());
        
        return pedido.getUrlWhatsApp();
    }
    
    private Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
                .orElseGet(() -> crearCarritoEntity(usuarioId));
    }
    
    private Carrito crearCarritoEntity(Long usuarioId) {
        return Carrito.builder()
                .usuarioId(usuarioId)
                .activo(true)
                .total(BigDecimal.ZERO)
                .build();
    }
}
