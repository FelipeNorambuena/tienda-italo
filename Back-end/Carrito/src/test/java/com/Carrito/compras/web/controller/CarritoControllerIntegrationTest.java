package com.Carrito.compras.web.controller;

import com.Carrito.compras.application.service.CarritoService;
import com.Carrito.compras.web.dto.AgregarItemRequestDTO;
import com.Carrito.compras.web.dto.CarritoResponseDTO;
import com.Carrito.compras.web.dto.WhatsAppPedidoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para CarritoController.
 * Verifica el comportamiento completo de los endpoints REST.
 */
@WebMvcTest(CarritoController.class)
class CarritoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarritoService carritoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerCarrito_ConUsuarioValido_DeberiaRetornar200() throws Exception {
        // Given
        Long usuarioId = 123L;
        CarritoResponseDTO carritoResponseDTO = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(usuarioId)
                .total(BigDecimal.valueOf(599990.00))
                .activo(true)
                .cantidadTotalItems(1)
                .build();

        when(carritoService.obtenerCarritoUsuario(usuarioId)).thenReturn(carritoResponseDTO);

        // When & Then
        mockMvc.perform(get("/carrito/usuario/{usuarioId}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value(123))
                .andExpect(jsonPath("$.total").value(599990.00))
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$.cantidadTotalItems").value(1));
    }

    @Test
    void crearCarrito_ConUsuarioValido_DeberiaRetornar200() throws Exception {
        // Given
        Long usuarioId = 123L;
        CarritoResponseDTO carritoResponseDTO = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(usuarioId)
                .total(BigDecimal.ZERO)
                .activo(true)
                .cantidadTotalItems(0)
                .build();

        when(carritoService.crearCarrito(usuarioId)).thenReturn(carritoResponseDTO);

        // When & Then
        mockMvc.perform(post("/carrito/usuario/{usuarioId}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value(123))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    void agregarProducto_ConDatosValidos_DeberiaRetornar200() throws Exception {
        // Given
        Long usuarioId = 123L;
        AgregarItemRequestDTO requestDTO = AgregarItemRequestDTO.builder()
                .productoId(456L)
                .nombreProducto("Laptop HP Pavilion")
                .precioUnitario(BigDecimal.valueOf(599990.00))
                .cantidad(1)
                .build();

        CarritoResponseDTO carritoResponseDTO = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(usuarioId)
                .total(BigDecimal.valueOf(599990.00))
                .activo(true)
                .cantidadTotalItems(1)
                .build();

        when(carritoService.agregarProducto(eq(usuarioId), any(AgregarItemRequestDTO.class)))
                .thenReturn(carritoResponseDTO);

        // When & Then
        mockMvc.perform(post("/carrito/usuario/{usuarioId}/items", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value(123))
                .andExpect(jsonPath("$.total").value(599990.00))
                .andExpect(jsonPath("$.cantidadTotalItems").value(1));
    }

    @Test
    void agregarProducto_ConDatosInvalidos_DeberiaRetornar400() throws Exception {
        // Given
        Long usuarioId = 123L;
        AgregarItemRequestDTO requestDTO = AgregarItemRequestDTO.builder()
                .productoId(null) // ID inválido
                .nombreProducto("") // Nombre vacío
                .precioUnitario(BigDecimal.valueOf(-100)) // Precio negativo
                .cantidad(0) // Cantidad inválida
                .build();

        // When & Then
        mockMvc.perform(post("/carrito/usuario/{usuarioId}/items", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarCantidad_ConParametrosValidos_DeberiaRetornar200() throws Exception {
        // Given
        Long usuarioId = 123L;
        Long itemId = 1L;
        Integer nuevaCantidad = 2;

        CarritoResponseDTO carritoResponseDTO = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(usuarioId)
                .total(BigDecimal.valueOf(1199980.00))
                .activo(true)
                .cantidadTotalItems(2)
                .build();

        when(carritoService.actualizarCantidad(usuarioId, itemId, nuevaCantidad))
                .thenReturn(carritoResponseDTO);

        // When & Then
        mockMvc.perform(put("/carrito/usuario/{usuarioId}/items/{itemId}", usuarioId, itemId)
                        .param("cantidad", nuevaCantidad.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1199980.00))
                .andExpect(jsonPath("$.cantidadTotalItems").value(2));
    }

    @Test
    void removerProducto_ConParametrosValidos_DeberiaRetornar200() throws Exception {
        // Given
        Long usuarioId = 123L;
        Long itemId = 1L;

        CarritoResponseDTO carritoResponseDTO = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(usuarioId)
                .total(BigDecimal.ZERO)
                .activo(true)
                .cantidadTotalItems(0)
                .build();

        when(carritoService.removerProducto(usuarioId, itemId)).thenReturn(carritoResponseDTO);

        // When & Then
        mockMvc.perform(delete("/carrito/usuario/{usuarioId}/items/{itemId}", usuarioId, itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.cantidadTotalItems").value(0));
    }

    @Test
    void limpiarCarrito_ConUsuarioValido_DeberiaRetornar200() throws Exception {
        // Given
        Long usuarioId = 123L;

        CarritoResponseDTO carritoResponseDTO = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(usuarioId)
                .total(BigDecimal.ZERO)
                .activo(true)
                .cantidadTotalItems(0)
                .build();

        when(carritoService.limpiarCarrito(usuarioId)).thenReturn(carritoResponseDTO);

        // When & Then
        mockMvc.perform(delete("/carrito/usuario/{usuarioId}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.cantidadTotalItems").value(0));
    }

    @Test
    void generarPedidoWhatsApp_ConCarritoValido_DeberiaRetornar200() throws Exception {
        // Given
        Long usuarioId = 123L;
        WhatsAppPedidoDTO pedidoDTO = WhatsAppPedidoDTO.builder()
                .total(BigDecimal.valueOf(599990.00))
                .build();

        when(carritoService.generarPedidoWhatsApp(usuarioId)).thenReturn(pedidoDTO);

        // When & Then
        mockMvc.perform(get("/carrito/usuario/{usuarioId}/pedido-whatsapp", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(599990.00));
    }

    @Test
    void finalizarCompra_ConCarritoValido_DeberiaRetornar200() throws Exception {
        // Given
        Long usuarioId = 123L;
        String urlWhatsApp = "https://wa.me/56974161396?text=Hola!%20Quiero%20realizar%20el%20siguiente%20pedido:";

        when(carritoService.finalizarCompra(usuarioId)).thenReturn(urlWhatsApp);

        // When & Then
        mockMvc.perform(post("/carrito/usuario/{usuarioId}/finalizar-compra", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(urlWhatsApp));
    }
}
