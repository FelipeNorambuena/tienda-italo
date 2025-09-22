package com.tienda.producto.unit.service;

import com.tienda.producto.application.dto.*;
import com.tienda.producto.application.service.ProductoService;
import com.tienda.producto.domain.entity.Producto;
import com.tienda.producto.domain.entity.Categoria;
import com.tienda.producto.domain.entity.Marca;
import com.tienda.producto.domain.repository.ProductoRepository;
import com.tienda.producto.domain.repository.CategoriaRepository;
import com.tienda.producto.domain.repository.MarcaRepository;
import com.tienda.producto.web.mapper.ProductoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductoService.
 * 
 * @author Tienda Italo Team
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitarios - ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private com.tienda.producto.application.service.impl.ProductoServiceImpl productoService;

    private Producto producto;
    private ProductoRequestDTO productoRequestDTO;
    private ProductoResponseDTO productoResponseDTO;
    private Categoria categoria;
    private Marca marca;

    @BeforeEach
    void setUp() {
        categoria = Categoria.builder()
                .id(1L)
                .nombre("Electrónicos")
                .descripcion("Dispositivos electrónicos")
                .slug("electronicos")
                .activa(true)
                .destacada(true)
                .orden(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        marca = Marca.builder()
                .id(1L)
                .nombre("Samsung")
                .descripcion("Tecnología coreana")
                .slug("samsung")
                .activa(true)
                .destacada(true)
                .orden(1)
                .sitioWeb("https://samsung.com")
                .palabrasClave("tecnologia,corea")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Samsung Galaxy S24")
                .descripcion("Smartphone Android de última generación")
                .slug("samsung-galaxy-s24")
                .codigo("SGS24")
                .sku("SGS24-001")
                .activo(true)
                .destacado(true)
                .precio(new BigDecimal("899000.00"))
                .precioOferta(new BigDecimal("799000.00"))
                .stock(50)
                .stockMinimo(5)
                .peso(0.168)
                .categoria(categoria)
                .marca(marca)
                .palabrasClave("smartphone,android,samsung")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productoRequestDTO = ProductoRequestDTO.builder()
                .nombre("Samsung Galaxy S24")
                .descripcion("Smartphone Android de última generación")
                .codigo("SGS24")
                .sku("SGS24-001")
                .activo(true)
                .destacado(true)
                .precio(new BigDecimal("899000.00"))
                .precioOferta(new BigDecimal("799000.00"))
                .stock(50)
                .stockMinimo(5)
                .peso(0.168)
                .categoriaId(1L)
                .marcaId(1L)
                .palabrasClave("smartphone,android,samsung")
                .build();

        productoResponseDTO = ProductoResponseDTO.builder()
                .id(1L)
                .nombre("Samsung Galaxy S24")
                .descripcion("Smartphone Android de última generación")
                .slug("samsung-galaxy-s24")
                .codigo("SGS24")
                .sku("SGS24-001")
                .activo(true)
                .destacado(true)
                .precio(new BigDecimal("899000.00"))
                .precioOferta(new BigDecimal("799000.00"))
                .stock(50)
                .stockMinimo(5)
                .peso(0.168)
                .categoria(CategoriaResponseDTO.builder()
                        .id(1L)
                        .nombre("Electrónicos")
                        .build())
                .marca(MarcaResponseDTO.builder()
                        .id(1L)
                        .nombre("Samsung")
                        .build())
                .palabrasClave("smartphone,android,samsung")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Tests de Creación de Productos")
    class CreacionDeProductos {

        @Test
        @DisplayName("Debería crear producto exitosamente")
        void deberiaCrearProductoExitosamente() {
            // Given
            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
            when(productoRepository.existsByCodigo("SGS24")).thenReturn(false);
            when(productoRepository.existsBySku("SGS24-001")).thenReturn(false);
            when(productoMapper.toEntity(any(ProductoRequestDTO.class))).thenReturn(producto);
            when(productoRepository.save(any(Producto.class))).thenReturn(producto);
            when(productoMapper.toResponseDTO(any(Producto.class))).thenReturn(productoResponseDTO);

            // When
            ProductoResponseDTO resultado = productoService.crearProducto(productoRequestDTO);

            // Then
            assertNotNull(resultado);
            assertEquals("Samsung Galaxy S24", resultado.getNombre());
            verify(productoRepository).save(any(Producto.class));
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando categoría no existe")
        void deberiaLanzarExcepcionCuandoCategoriaNoExiste() {
            // Given
            when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                productoService.crearProducto(productoRequestDTO);
            });
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando marca no existe")
        void deberiaLanzarExcepcionCuandoMarcaNoExiste() {
            // Given
            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                productoService.crearProducto(productoRequestDTO);
            });
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando código ya existe")
        void deberiaLanzarExcepcionCuandoCodigoYaExiste() {
            // Given
            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
            when(productoRepository.existsByCodigo("SGS24")).thenReturn(true);

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                productoService.crearProducto(productoRequestDTO);
            });
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando SKU ya existe")
        void deberiaLanzarExcepcionCuandoSkuYaExiste() {
            // Given
            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
            when(productoRepository.existsByCodigo("SGS24")).thenReturn(false);
            when(productoRepository.existsBySku("SGS24-001")).thenReturn(true);

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                productoService.crearProducto(productoRequestDTO);
            });
        }
    }

    @Nested
    @DisplayName("Tests de Consulta de Productos")
    class ConsultaDeProductos {

        @Test
        @DisplayName("Debería obtener producto por ID exitosamente")
        void deberiaObtenerProductoPorIdExitosamente() {
            // Given
            when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
            when(productoMapper.toResponseDTO(producto)).thenReturn(productoResponseDTO);

            // When
            ProductoResponseDTO resultado = productoService.obtenerProducto(1L);

            // Then
            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("Samsung Galaxy S24", resultado.getNombre());
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando producto no existe")
        void deberiaLanzarExcepcionCuandoProductoNoExiste() {
            // Given
            when(productoRepository.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                productoService.obtenerProducto(1L);
            });
        }

        @Test
        @DisplayName("Debería listar productos paginados")
        void deberiaListarProductosPaginados() {
            // Given
            Page<Producto> page = new PageImpl<>(List.of(producto));
            Page<ProductoResponseDTO> expectedPage = new PageImpl<>(List.of(productoResponseDTO));
            
            when(productoRepository.findAll(any(Pageable.class))).thenReturn(page);
            when(productoMapper.toResponseDTO(producto)).thenReturn(productoResponseDTO);

            // When
            Page<ProductoResponseDTO> resultado = productoService.listarProductos(Pageable.unpaged());

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.getContent().size());
            assertEquals("Samsung Galaxy S24", resultado.getContent().get(0).getNombre());
        }

        @Test
        @DisplayName("Debería buscar productos por texto")
        void deberiaBuscarProductosPorTexto() {
            // Given
            ProductoBusquedaDTO busquedaDTO = ProductoBusquedaDTO.builder()
                    .build();
            List<Producto> productos = List.of(producto);
            List<ProductoResponseDTO> productosDTO = List.of(productoResponseDTO);
            
            when(productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCaseAndActivoTrue("Samsung"))
                    .thenReturn(productos);
            when(productoMapper.toResponseDTOList(productos)).thenReturn(productosDTO);

            // When
            List<ProductoResponseDTO> resultado = productoService.buscarProductos(busquedaDTO);

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals("Samsung Galaxy S24", resultado.get(0).getNombre());
        }
    }

    @Nested
    @DisplayName("Tests de Actualización de Productos")
    class ActualizacionDeProductos {

        @Test
        @DisplayName("Debería actualizar producto exitosamente")
        void deberiaActualizarProductoExitosamente() {
            // Given
            ProductoRequestDTO requestDTO = ProductoRequestDTO.builder()
                    .nombre("Samsung Galaxy S24 Pro")
                    .descripcion("Smartphone Android Pro de última generación")
                    .codigo("SGS24")
                    .sku("SGS24-001")
                    .activo(true)
                    .destacado(true)
                    .orden(1)
                    .precio(new BigDecimal("999000.00"))
                    .precioOferta(new BigDecimal("899000.00"))
                    .stock(60)
                    .stockMinimo(5)
                    .peso(0.168)
                    .dimensiones("147.0 x 70.6 x 7.6 mm")
                    .categoriaId(1L)
                    .marcaId(1L)
                    .palabrasClave("smartphone,android,samsung,pro")
                    .build();

            when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
            when(productoRepository.save(any(Producto.class))).thenReturn(producto);
            when(productoMapper.toResponseDTO(any(Producto.class))).thenReturn(productoResponseDTO);

            // When
            ProductoResponseDTO resultado = productoService.actualizarProducto(1L, requestDTO);

            // Then
            assertNotNull(resultado);
            verify(productoRepository).save(any(Producto.class));
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando producto a actualizar no existe")
        void deberiaLanzarExcepcionCuandoProductoAActualizarNoExiste() {
            // Given
            when(productoRepository.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                productoService.actualizarProducto(1L, productoRequestDTO);
            });
        }
    }

    @Nested
    @DisplayName("Tests de Eliminación de Productos")
    class EliminacionDeProductos {

        @Test
        @DisplayName("Debería eliminar producto exitosamente")
        void deberiaEliminarProductoExitosamente() {
            // Given
            when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

            // When
            productoService.eliminarProducto(1L);

            // Then
            verify(productoRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando producto a eliminar no existe")
        void deberiaLanzarExcepcionCuandoProductoAEliminarNoExiste() {
            // Given
            when(productoRepository.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                productoService.eliminarProducto(1L);
            });
        }
    }

    @Nested
    @DisplayName("Tests de Filtros y Búsquedas")
    class FiltrosYBusquedas {

        @Test
        @DisplayName("Debería filtrar productos por categoría")
        void deberiaFiltrarProductosPorCategoria() {
            // Given
            List<Producto> productos = List.of(producto);
            List<ProductoResponseDTO> productosDTO = List.of(productoResponseDTO);
            
            when(productoRepository.findByCategoriaIdAndActivoTrue(1L)).thenReturn(productos);
            when(productoMapper.toResponseDTOList(productos)).thenReturn(productosDTO);

            // When
            List<ProductoResponseDTO> resultado = productoService.listarProductosPorCategoria(1L);

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debería filtrar productos por marca")
        void deberiaFiltrarProductosPorMarca() {
            // Given
            List<Producto> productos = List.of(producto);
            List<ProductoResponseDTO> productosDTO = List.of(productoResponseDTO);
            
            when(productoRepository.findByMarcaIdAndActivoTrue(1L)).thenReturn(productos);
            when(productoMapper.toResponseDTOList(productos)).thenReturn(productosDTO);

            // When
            List<ProductoResponseDTO> resultado = productoService.listarProductosPorMarca(1L);

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debería filtrar productos por rango de precio")
        void deberiaFiltrarProductosPorRangoDePrecio() {
            // Given
            BigDecimal precioMin = new BigDecimal("500000.00");
            BigDecimal precioMax = new BigDecimal("1000000.00");
            List<Producto> productos = List.of(producto);
            List<ProductoResponseDTO> productosDTO = List.of(productoResponseDTO);
            
            when(productoRepository.findByPrecioBetweenAndActivoTrue(precioMin, precioMax)).thenReturn(productos);
            when(productoMapper.toResponseDTOList(productos)).thenReturn(productosDTO);

            // When
            List<ProductoResponseDTO> resultado = productoService.listarProductosPorRangoPrecio(precioMin, precioMax);

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debería listar productos en oferta")
        void deberiaListarProductosEnOferta() {
            // Given
            List<Producto> productos = List.of(producto);
            List<ProductoResponseDTO> productosDTO = List.of(productoResponseDTO);
            
            when(productoRepository.findByPrecioOfertaIsNotNullAndActivoTrue()).thenReturn(productos);
            when(productoMapper.toResponseDTOList(productos)).thenReturn(productosDTO);

            // When
            List<ProductoResponseDTO> resultado = productoService.listarProductosEnOferta();

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("Tests de Gestión de Stock")
    class GestionDeStock {

        @Test
        @DisplayName("Debería listar productos con stock bajo")
        void deberiaListarProductosConStockBajo() {
            // Given
            List<Producto> productos = List.of(producto);
            List<ProductoResponseDTO> productosDTO = List.of(productoResponseDTO);
            
            when(productoRepository.findByStockLessThanEqualAndActivoTrue(10)).thenReturn(productos);
            when(productoMapper.toResponseDTOList(productos)).thenReturn(productosDTO);

            // When
            List<ProductoResponseDTO> resultado = productoService.listarProductosConStockBajo();

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debería listar productos sin stock")
        void deberiaListarProductosSinStock() {
            // Given
            List<Producto> productos = List.of(producto);
            List<ProductoResponseDTO> productosDTO = List.of(productoResponseDTO);
            
            when(productoRepository.findByStockEqualsAndActivoTrue(0)).thenReturn(productos);
            when(productoMapper.toResponseDTOList(productos)).thenReturn(productosDTO);

            // When
            List<ProductoResponseDTO> resultado = productoService.listarProductosSinStock();

            // Then
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("Tests de Estadísticas")
    class Estadisticas {

        @Test
        @DisplayName("Debería obtener estadísticas de productos")
        void deberiaObtenerEstadisticasDeProductos() {
            // Given
            when(productoRepository.count()).thenReturn(10L);
            when(productoRepository.countByActivoTrue()).thenReturn(8L);

            // When
            ProductoEstadisticasDTO estadisticas = productoService.obtenerEstadisticas();

            // Then
            assertNotNull(estadisticas);
            assertEquals(10L, estadisticas.getTotalProductos());
            assertEquals(8L, estadisticas.getTotalProductosActivos());
        }
    }
}
