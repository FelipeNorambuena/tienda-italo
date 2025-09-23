package com.Carrito.compras.application.service.impl;

import com.Carrito.compras.domain.entity.Carrito;
import com.Carrito.compras.domain.entity.ItemCarrito;
import com.Carrito.compras.infrastructure.repository.CarritoJpaRepository;
import com.Carrito.compras.infrastructure.repository.ItemCarritoJpaRepository;
import com.Carrito.compras.web.dto.AgregarItemRequestDTO;
import com.Carrito.compras.web.dto.CarritoResponseDTO;
import com.Carrito.compras.web.dto.WhatsAppPedidoDTO;
import com.Carrito.compras.web.mapper.CarritoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CarritoServiceImpl.
 * Verifica la lógica de negocio del servicio de carrito.
 */
@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoJpaRepository carritoRepository;

    @Mock
    private ItemCarritoJpaRepository itemCarritoRepository;

    @Mock
    private CarritoMapper carritoMapper;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Carrito carrito;
    private ItemCarrito itemCarrito;
    private AgregarItemRequestDTO agregarItemRequestDTO;
    private CarritoResponseDTO carritoResponseDTO;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        carrito = Carrito.builder()
                .id(1L)
                .usuarioId(123L)
                .total(BigDecimal.valueOf(599990.00))
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        itemCarrito = ItemCarrito.builder()
                .id(1L)
                .carrito(carrito)
                .productoId(456L)
                .nombreProducto("Laptop HP Pavilion")
                .precioUnitario(BigDecimal.valueOf(599990.00))
                .cantidad(1)
                .subtotal(BigDecimal.valueOf(599990.00))
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        agregarItemRequestDTO = AgregarItemRequestDTO.builder()
                .productoId(456L)
                .nombreProducto("Laptop HP Pavilion")
                .precioUnitario(BigDecimal.valueOf(599990.00))
                .cantidad(1)
                .build();

        carritoResponseDTO = CarritoResponseDTO.builder()
                .id(1L)
                .usuarioId(123L)
                .total(BigDecimal.valueOf(599990.00))
                .activo(true)
                .cantidadTotalItems(1)
                .build();
    }

    @Test
    void obtenerCarritoUsuario_CuandoExisteCarrito_DeberiaRetornarCarrito() {
        // Given
        when(carritoRepository.findByUsuarioIdAndActivoTrue(123L))
                .thenReturn(Optional.of(carrito));
        when(carritoMapper.toResponseDTO(carrito)).thenReturn(carritoResponseDTO);

        // When
        CarritoResponseDTO resultado = carritoService.obtenerCarritoUsuario(123L);

        // Then
        assertNotNull(resultado);
        assertEquals(123L, resultado.getUsuarioId());
        assertEquals(BigDecimal.valueOf(599990.00), resultado.getTotal());
        verify(carritoRepository).findByUsuarioIdAndActivoTrue(123L);
        verify(carritoMapper).toResponseDTO(carrito);
    }

    @Test
    void obtenerCarritoUsuario_CuandoNoExisteCarrito_DeberiaCrearNuevoCarrito() {
        // Given
        when(carritoRepository.findByUsuarioIdAndActivoTrue(123L))
                .thenReturn(Optional.empty());
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toResponseDTO(carrito)).thenReturn(carritoResponseDTO);

        // When
        CarritoResponseDTO resultado = carritoService.obtenerCarritoUsuario(123L);

        // Then
        assertNotNull(resultado);
        verify(carritoRepository).findByUsuarioIdAndActivoTrue(123L);
        verify(carritoRepository).save(any(Carrito.class));
        verify(carritoMapper).toResponseDTO(carrito);
    }

    @Test
    void crearCarrito_CuandoUsuarioNoTieneCarrito_DeberiaCrearNuevoCarrito() {
        // Given
        when(carritoRepository.existsByUsuarioIdAndActivoTrue(123L)).thenReturn(false);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toResponseDTO(carrito)).thenReturn(carritoResponseDTO);

        // When
        CarritoResponseDTO resultado = carritoService.crearCarrito(123L);

        // Then
        assertNotNull(resultado);
        verify(carritoRepository).existsByUsuarioIdAndActivoTrue(123L);
        verify(carritoRepository).save(any(Carrito.class));
        verify(carritoMapper).toResponseDTO(carrito);
    }

    @Test
    void agregarProducto_CuandoCarritoExiste_DeberiaAgregarItem() {
        // Given
        when(carritoRepository.findByUsuarioIdAndActivoTrue(123L))
                .thenReturn(Optional.of(carrito));
        when(itemCarritoRepository.save(any(ItemCarrito.class))).thenReturn(itemCarrito);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toResponseDTO(carrito)).thenReturn(carritoResponseDTO);

        // When
        CarritoResponseDTO resultado = carritoService.agregarProducto(123L, agregarItemRequestDTO);

        // Then
        assertNotNull(resultado);
        verify(carritoRepository).findByUsuarioIdAndActivoTrue(123L);
        verify(itemCarritoRepository).save(any(ItemCarrito.class));
        verify(carritoRepository).save(any(Carrito.class));
        verify(carritoMapper).toResponseDTO(carrito);
    }

    @Test
    void limpiarCarrito_CuandoCarritoExiste_DeberiaLimpiarItems() {
        // Given
        carrito.setItems(Arrays.asList(itemCarrito));
        when(carritoRepository.findByUsuarioIdAndActivoTrue(123L))
                .thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toResponseDTO(carrito)).thenReturn(carritoResponseDTO);

        // When
        CarritoResponseDTO resultado = carritoService.limpiarCarrito(123L);

        // Then
        assertNotNull(resultado);
        verify(carritoRepository).findByUsuarioIdAndActivoTrue(123L);
        verify(carritoRepository).save(any(Carrito.class));
        verify(carritoMapper).toResponseDTO(carrito);
    }
}
