package com.Carrito.compras.web.controller;

import com.Carrito.compras.application.service.CarritoService;
import com.Carrito.compras.web.dto.AgregarItemRequestDTO;
import com.Carrito.compras.web.dto.CarritoResponseDTO;
import com.Carrito.compras.web.dto.WhatsAppPedidoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Carrito de Compras", description = "API para gestión del carrito de compras")
public class CarritoController {
    
    private final CarritoService carritoService;
    
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener carrito de usuario", description = "Obtiene el carrito activo de un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<CarritoResponseDTO> obtenerCarrito(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        
        log.info("Obteniendo carrito para usuario: {}", usuarioId);
        CarritoResponseDTO carrito = carritoService.obtenerCarritoUsuario(usuarioId);
        return ResponseEntity.ok(carrito);
    }
    
    @PostMapping("/usuario/{usuarioId}")
    @Operation(summary = "Crear nuevo carrito", description = "Crea un nuevo carrito para un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carrito creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<CarritoResponseDTO> crearCarrito(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        
        log.info("Creando nuevo carrito para usuario: {}", usuarioId);
        CarritoResponseDTO carrito = carritoService.crearCarrito(usuarioId);
        return ResponseEntity.ok(carrito);
    }
    
    @PostMapping("/usuario/{usuarioId}/items")
    @Operation(summary = "Agregar producto al carrito", description = "Agrega un producto al carrito del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<CarritoResponseDTO> agregarProducto(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId,
            @Valid @RequestBody AgregarItemRequestDTO request) {
        
        log.info("Agregando producto {} al carrito del usuario {}", request.getProductoId(), usuarioId);
        CarritoResponseDTO carrito = carritoService.agregarProducto(usuarioId, request);
        return ResponseEntity.ok(carrito);
    }
    
    @PutMapping("/usuario/{usuarioId}/items/{itemId}")
    @Operation(summary = "Actualizar cantidad de producto", description = "Actualiza la cantidad de un producto en el carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId,
            @Parameter(description = "ID del item") @PathVariable Long itemId,
            @Parameter(description = "Nueva cantidad") @RequestParam Integer cantidad) {
        
        log.info("Actualizando cantidad del item {} a {} para usuario {}", itemId, cantidad, usuarioId);
        CarritoResponseDTO carrito = carritoService.actualizarCantidad(usuarioId, itemId, cantidad);
        return ResponseEntity.ok(carrito);
    }
    
    @DeleteMapping("/usuario/{usuarioId}/items/{itemId}")
    @Operation(summary = "Remover producto del carrito", description = "Remueve un producto del carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto removido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    public ResponseEntity<CarritoResponseDTO> removerProducto(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId,
            @Parameter(description = "ID del item") @PathVariable Long itemId) {
        
        log.info("Removiendo item {} del carrito del usuario {}", itemId, usuarioId);
        CarritoResponseDTO carrito = carritoService.removerProducto(usuarioId, itemId);
        return ResponseEntity.ok(carrito);
    }
    
    @DeleteMapping("/usuario/{usuarioId}")
    @Operation(summary = "Limpiar carrito", description = "Limpia todos los productos del carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito limpiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<CarritoResponseDTO> limpiarCarrito(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        
        log.info("Limpiando carrito del usuario {}", usuarioId);
        CarritoResponseDTO carrito = carritoService.limpiarCarrito(usuarioId);
        return ResponseEntity.ok(carrito);
    }
    
    @GetMapping("/usuario/{usuarioId}/pedido-whatsapp")
    @Operation(summary = "Generar pedido para WhatsApp", description = "Genera el pedido formateado para WhatsApp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido generado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado o vacío")
    })
    public ResponseEntity<WhatsAppPedidoDTO> generarPedidoWhatsApp(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        
        log.info("Generando pedido WhatsApp para usuario {}", usuarioId);
        WhatsAppPedidoDTO pedido = carritoService.generarPedidoWhatsApp(usuarioId);
        return ResponseEntity.ok(pedido);
    }
    
    @PostMapping("/usuario/{usuarioId}/finalizar-compra")
    @Operation(summary = "Finalizar compra", description = "Finaliza la compra y genera URL de WhatsApp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra finalizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado o vacío")
    })
    public ResponseEntity<String> finalizarCompra(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        
        log.info("Finalizando compra para usuario {}", usuarioId);
        String urlWhatsApp = carritoService.finalizarCompra(usuarioId);
        return ResponseEntity.ok(urlWhatsApp);
    }
}
