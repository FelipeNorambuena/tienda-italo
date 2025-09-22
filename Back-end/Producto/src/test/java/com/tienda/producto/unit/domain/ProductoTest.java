package com.tienda.producto.unit.domain;

import com.tienda.producto.domain.entity.Producto;
import com.tienda.producto.domain.entity.Categoria;
import com.tienda.producto.domain.entity.Marca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Producto.
 * 
 * @author Tienda Italo Team
 */
@DisplayName("Tests Unitarios - Entidad Producto")
class ProductoTest {

    private Producto producto;
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
                .nivel(0)
                .rutaCompleta("Electrónicos")
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
                .orden(1)
                .precio(new BigDecimal("899000.00"))
                .precioOferta(new BigDecimal("799000.00"))
                .stock(50)
                .stockMinimo(5)
                .peso(0.168)
                .dimensiones("147.0 x 70.6 x 7.6 mm")
                .categoria(categoria)
                .marca(marca)
                .palabrasClave("smartphone,android,samsung")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Tests de Construcción y Propiedades Básicas")
    class ConstruccionYPropiedadesBasicas {

        @Test
        @DisplayName("Debería crear producto con propiedades correctas")
        void deberiaCrearProductoConPropiedadesCorrectas() {
            assertNotNull(producto);
            assertEquals(1L, producto.getId());
            assertEquals("Samsung Galaxy S24", producto.getNombre());
            assertEquals("Smartphone Android de última generación", producto.getDescripcion());
            assertEquals("samsung-galaxy-s24", producto.getSlug());
            assertEquals("SGS24", producto.getCodigo());
            assertEquals("SGS24-001", producto.getSku());
            assertTrue(producto.getActivo());
            assertTrue(producto.getDestacado());
            assertEquals(1, producto.getOrden());
            assertEquals(new BigDecimal("899000.00"), producto.getPrecio());
            assertEquals(new BigDecimal("799000.00"), producto.getPrecioOferta());
            assertEquals(50, producto.getStock());
            assertEquals(5, producto.getStockMinimo());
            assertEquals(0.168, producto.getPeso());
            assertEquals("147.0 x 70.6 x 7.6 mm", producto.getDimensiones());
            assertEquals(categoria, producto.getCategoria());
            assertEquals(marca, producto.getMarca());
            assertEquals("smartphone,android,samsung", producto.getPalabrasClave());
        }

        @Test
        @DisplayName("Debería tener timestamps de auditoría")
        void deberiaTenerTimestampsDeAuditoria() {
            assertNotNull(producto.getCreatedAt());
            assertNotNull(producto.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Estado del Producto")
    class LogicaDeNegocioEstado {

        @Test
        @DisplayName("Debería identificar producto activo correctamente")
        void deberiaIdentificarProductoActivoCorrectamente() {
            assertTrue(producto.esActivo());
            
            producto.setActivo(false);
            assertFalse(producto.esActivo());
        }

        @Test
        @DisplayName("Debería identificar producto destacado correctamente")
        void deberiaIdentificarProductoDestacadoCorrectamente() {
            assertTrue(producto.esDestacado());
            
            producto.setDestacado(false);
            assertFalse(producto.esDestacado());
        }

        @Test
        @DisplayName("Debería identificar producto en oferta correctamente")
        void deberiaIdentificarProductoEnOfertaCorrectamente() {
            assertTrue(producto.estaEnOferta());
            
            producto.setPrecioOferta(null);
            assertFalse(producto.estaEnOferta());
            
            producto.setPrecioOferta(new BigDecimal("0.00"));
            assertFalse(producto.estaEnOferta());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Stock")
    class LogicaDeNegocioStock {

        @Test
        @DisplayName("Debería identificar producto con stock correctamente")
        void deberiaIdentificarProductoConStockCorrectamente() {
            assertTrue(producto.tieneStock());
            
            producto.setStock(0);
            assertFalse(producto.tieneStock());
            
            producto.setStock(-5);
            assertFalse(producto.tieneStock());
        }

        @Test
        @DisplayName("Debería identificar producto que necesita reposición correctamente")
        void deberiaIdentificarProductoQueNecesitaReposicionCorrectamente() {
            assertFalse(producto.necesitaReposicion());
            
            producto.setStock(3);
            assertTrue(producto.necesitaReposicion());
            
            producto.setStock(5);
            assertTrue(producto.necesitaReposicion());
        }

        @Test
        @DisplayName("Debería calcular estado de stock correctamente")
        void deberiaCalcularEstadoDeStockCorrectamente() {
            assertEquals("SUFICIENTE", producto.getEstadoStock());
            
            producto.setStock(3);
            assertEquals("BAJO", producto.getEstadoStock());
            
            producto.setStock(0);
            assertEquals("AGOTADO", producto.getEstadoStock());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Precios")
    class LogicaDeNegocioPrecios {

        @Test
        @DisplayName("Debería calcular porcentaje de descuento correctamente")
        void deberiaCalcularPorcentajeDeDescuentoCorrectamente() {
            BigDecimal porcentaje = producto.getPorcentajeDescuento();
            assertNotNull(porcentaje);
            assertTrue(porcentaje.compareTo(BigDecimal.ZERO) > 0);
            
            // Verificar cálculo: (899000 - 799000) / 899000 * 100 = 11.12%
            BigDecimal esperado = new BigDecimal("11.12");
            assertTrue(porcentaje.compareTo(esperado) >= 0);
        }

        @Test
        @DisplayName("Debería retornar cero cuando no hay oferta")
        void deberiaRetornarCeroCuandoNoHayOferta() {
            producto.setPrecioOferta(null);
            assertEquals(BigDecimal.ZERO, producto.getPorcentajeDescuento());
            
            producto.setPrecioOferta(new BigDecimal("0.00"));
            assertEquals(BigDecimal.ZERO, producto.getPorcentajeDescuento());
        }

        @Test
        @DisplayName("Debería obtener precio efectivo correctamente")
        void deberiaObtenerPrecioEfectivoCorrectamente() {
            assertEquals(producto.getPrecioOferta(), producto.getPrecioEfectivo());
            
            producto.setPrecioOferta(null);
            assertEquals(producto.getPrecio(), producto.getPrecioEfectivo());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Disponibilidad")
    class LogicaDeNegocioDisponibilidad {

        @Test
        @DisplayName("Debería identificar producto disponible correctamente")
        void deberiaIdentificarProductoDisponibleCorrectamente() {
            assertTrue(producto.esDisponible());
            
            producto.setActivo(false);
            assertFalse(producto.esDisponible());
            
            producto.setActivo(true);
            producto.setStock(0);
            assertFalse(producto.esDisponible());
        }

        @Test
        @DisplayName("Debería generar nombre completo correctamente")
        void deberiaGenerarNombreCompletoCorrectamente() {
            String nombreCompleto = producto.getNombreCompleto();
            assertNotNull(nombreCompleto);
            assertTrue(nombreCompleto.contains(producto.getNombre()));
            assertTrue(nombreCompleto.contains(marca.getNombre()));
        }
    }

    @Nested
    @DisplayName("Tests de Validación de Datos")
    class ValidacionDeDatos {

        @Test
        @DisplayName("Debería validar datos requeridos")
        void deberiaValidarDatosRequeridos() {
            assertThrows(NullPointerException.class, () -> {
                Producto.builder()
                        .nombre(null)
                        .build();
            });
        }

        @Test
        @DisplayName("Debería manejar precios negativos")
        void deberiaManejarPreciosNegativos() {
            producto.setPrecio(new BigDecimal("-100.00"));
            assertTrue(producto.getPrecio().compareTo(BigDecimal.ZERO) < 0);
        }

        @Test
        @DisplayName("Debería manejar stock negativo")
        void deberiaManejarStockNegativo() {
            producto.setStock(-10);
            assertFalse(producto.tieneStock());
            assertEquals("AGOTADO", producto.getEstadoStock());
        }
    }

    @Nested
    @DisplayName("Tests de Casos Edge")
    class CasosEdge {

        @Test
        @DisplayName("Debería manejar producto sin categoría")
        void deberiaManejarProductoSinCategoria() {
            producto.setCategoria(null);
            assertNull(producto.getCategoria());
            assertNotNull(producto.getNombreCompleto());
        }

        @Test
        @DisplayName("Debería manejar producto sin marca")
        void deberiaManejarProductoSinMarca() {
            producto.setMarca(null);
            assertNull(producto.getMarca());
            assertNotNull(producto.getNombreCompleto());
        }

        @Test
        @DisplayName("Debería manejar precios muy grandes")
        void deberiaManejarPreciosMuyGrandes() {
            BigDecimal precioGrande = new BigDecimal("999999999.99");
            producto.setPrecio(precioGrande);
            assertEquals(precioGrande, producto.getPrecio());
        }

        @Test
        @DisplayName("Debería manejar stock muy grande")
        void deberiaManejarStockMuyGrande() {
            producto.setStock(Integer.MAX_VALUE);
            assertTrue(producto.tieneStock());
            assertEquals("SUFICIENTE", producto.getEstadoStock());
        }
    }
}
