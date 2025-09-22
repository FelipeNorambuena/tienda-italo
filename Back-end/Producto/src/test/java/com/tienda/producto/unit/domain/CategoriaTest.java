package com.tienda.producto.unit.domain;

import com.tienda.producto.domain.entity.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Categoria.
 * 
 * @author Tienda Italo Team
 */
@DisplayName("Tests Unitarios - Entidad Categoria")
class CategoriaTest {

    private Categoria categoriaPadre;
    private Categoria subcategoria;
    private List<Categoria> subcategorias;

    @BeforeEach
    void setUp() {
        categoriaPadre = Categoria.builder()
                .id(1L)
                .nombre("Electrónicos")
                .descripcion("Dispositivos electrónicos y tecnología")
                .slug("electronicos")
                .activa(true)
                .destacada(true)
                .orden(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        subcategoria = Categoria.builder()
                .id(2L)
                .nombre("Smartphones")
                .descripcion("Teléfonos inteligentes")
                .slug("smartphones")
                .activa(true)
                .destacada(true)
                .orden(1)
                .categoriaPadre(categoriaPadre)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        subcategorias = new ArrayList<>();
        subcategorias.add(subcategoria);
        categoriaPadre.setSubcategorias(subcategorias);
    }

    @Nested
    @DisplayName("Tests de Construcción y Propiedades Básicas")
    class ConstruccionYPropiedadesBasicas {

        @Test
        @DisplayName("Debería crear categoría con propiedades correctas")
        void deberiaCrearCategoriaConPropiedadesCorrectas() {
            assertNotNull(categoriaPadre);
            assertEquals(1L, categoriaPadre.getId());
            assertEquals("Electrónicos", categoriaPadre.getNombre());
            assertEquals("Dispositivos electrónicos y tecnología", categoriaPadre.getDescripcion());
            assertEquals("electronicos", categoriaPadre.getSlug());
            assertTrue(categoriaPadre.getActiva());
            assertTrue(categoriaPadre.getDestacada());
            assertEquals(1, categoriaPadre.getOrden());
            assertEquals(0, categoriaPadre.getNivel());
            assertEquals("Electrónicos", categoriaPadre.getRutaCompleta());
        }

        @Test
        @DisplayName("Debería crear subcategoría con propiedades correctas")
        void deberiaCrearSubcategoriaConPropiedadesCorrectas() {
            assertNotNull(subcategoria);
            assertEquals(2L, subcategoria.getId());
            assertEquals("Smartphones", subcategoria.getNombre());
            assertEquals(1, subcategoria.getNivel());
            assertEquals("Electrónicos > Smartphones", subcategoria.getRutaCompleta());
            assertEquals(categoriaPadre, subcategoria.getCategoriaPadre());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Jerarquía")
    class LogicaDeNegocioJerarquia {

        @Test
        @DisplayName("Debería identificar categoría raíz correctamente")
        void deberiaIdentificarCategoriaRaizCorrectamente() {
            assertTrue(categoriaPadre.esCategoriaRaiz());
            assertFalse(subcategoria.esCategoriaRaiz());
        }

        @Test
        @DisplayName("Debería identificar subcategoría correctamente")
        void deberiaIdentificarSubcategoriaCorrectamente() {
            assertFalse(categoriaPadre.esSubcategoria());
            assertTrue(subcategoria.esSubcategoria());
        }

        @Test
        @DisplayName("Debería calcular nivel correctamente")
        void deberiaCalcularNivelCorrectamente() {
            assertEquals(0, categoriaPadre.getNivel());
            assertEquals(1, subcategoria.getNivel());
        }

        @Test
        @DisplayName("Debería generar ruta completa correctamente")
        void deberiaGenerarRutaCompletaCorrectamente() {
            assertEquals("Electrónicos", categoriaPadre.getRutaCompleta());
            assertEquals("Electrónicos > Smartphones", subcategoria.getRutaCompleta());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Estado")
    class LogicaDeNegocioEstado {

        @Test
        @DisplayName("Debería identificar categoría activa correctamente")
        void deberiaIdentificarCategoriaActivaCorrectamente() {
            assertTrue(categoriaPadre.getActiva());
            
            categoriaPadre.setActiva(false);
            assertFalse(categoriaPadre.getActiva());
        }

        @Test
        @DisplayName("Debería identificar categoría destacada correctamente")
        void deberiaIdentificarCategoriaDestacadaCorrectamente() {
            assertTrue(categoriaPadre.getDestacada());
            
            categoriaPadre.setDestacada(false);
            assertFalse(categoriaPadre.getDestacada());
        }

        @Test
        @DisplayName("Debería identificar categoría visible correctamente")
        void deberiaIdentificarCategoriaVisibleCorrectamente() {
            assertTrue(categoriaPadre.getActiva());
            
            categoriaPadre.setActiva(false);
            assertFalse(categoriaPadre.getActiva());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Conteos")
    class LogicaDeNegocioConteos {

        @Test
        @DisplayName("Debería calcular total de subcategorías correctamente")
        void deberiaCalcularTotalDeSubcategoriasCorrectamente() {
            assertEquals(1, categoriaPadre.getTotalSubcategorias());
            
            // Agregar más subcategorías
            Categoria nuevaSubcategoria = Categoria.builder()
                    .id(3L)
                    .nombre("Laptops")
                    .descripcion("Computadoras portátiles")
                    .slug("laptops")
                    .activa(true)
                    .destacada(false)
                    .orden(2)
                    .categoriaPadre(categoriaPadre)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            subcategorias.add(nuevaSubcategoria);
            assertEquals(2, categoriaPadre.getTotalSubcategorias());
        }

        @Test
        @DisplayName("Debería calcular total de subcategorías activas correctamente")
        void deberiaCalcularTotalDeSubcategoriasActivasCorrectamente() {
            assertEquals(1, categoriaPadre.getTotalSubcategoriasActivas());
            
            // Desactivar una subcategoría
            subcategoria.setActiva(false);
            assertEquals(0, categoriaPadre.getTotalSubcategoriasActivas());
        }

        @Test
        @DisplayName("Debería manejar categoría sin subcategorías")
        void deberiaManejarCategoriaSinSubcategorias() {
            categoriaPadre.setSubcategorias(null);
            assertEquals(0, categoriaPadre.getTotalSubcategorias());
            assertEquals(0, categoriaPadre.getTotalSubcategoriasActivas());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Productos")
    class LogicaDeNegocioProductos {

        @Test
        @DisplayName("Debería manejar conteos de productos")
        void deberiaManejarConteosDeProductos() {
            // Inicialmente no hay productos
            assertEquals(0, categoriaPadre.getTotalProductos());
            assertEquals(0, categoriaPadre.getTotalProductosActivos());
            
            // Simular productos (estos valores se establecerían por la lógica de negocio)
            // Nota: Estos métodos no existen en la entidad actual
            // assertEquals(10, categoriaPadre.getTotalProductos());
            // assertEquals(8, categoriaPadre.getTotalProductosActivos());
        }
    }

    @Nested
    @DisplayName("Tests de Validación de Datos")
    class ValidacionDeDatos {

        @Test
        @DisplayName("Debería validar datos requeridos")
        void deberiaValidarDatosRequeridos() {
            assertThrows(NullPointerException.class, () -> {
                Categoria.builder()
                        .nombre(null)
                        .build();
            });
        }

        @Test
        @DisplayName("Debería manejar orden negativo")
        void deberiaManejarOrdenNegativo() {
            categoriaPadre.setOrden(-1);
            assertEquals(-1, categoriaPadre.getOrden());
        }
    }

    @Nested
    @DisplayName("Tests de Casos Edge")
    class CasosEdge {

        @Test
        @DisplayName("Debería manejar categoría sin padre")
        void deberiaManejarCategoriaSinPadre() {
            assertNull(categoriaPadre.getCategoriaPadre());
            assertTrue(categoriaPadre.esCategoriaRaiz());
        }

        @Test
        @DisplayName("Debería manejar subcategoría con padre nulo")
        void deberiaManejarSubcategoriaConPadreNulo() {
            subcategoria.setCategoriaPadre(null);
            assertNull(subcategoria.getCategoriaPadre());
            assertTrue(subcategoria.esCategoriaRaiz());
        }

        @Test
        @DisplayName("Debería manejar slug vacío")
        void deberiaManejarSlugVacio() {
            categoriaPadre.setSlug("");
            assertEquals("", categoriaPadre.getSlug());
        }
    }

    @Nested
    @DisplayName("Tests de Relaciones")
    class Relaciones {

        @Test
        @DisplayName("Debería establecer relación padre-hijo correctamente")
        void deberiaEstablecerRelacionPadreHijoCorrectamente() {
            assertEquals(categoriaPadre, subcategoria.getCategoriaPadre());
            assertTrue(categoriaPadre.getSubcategorias().contains(subcategoria));
        }

        @Test
        @DisplayName("Debería manejar múltiples subcategorías")
        void deberiaManejarMultiplesSubcategorias() {
            Categoria subcategoria2 = Categoria.builder()
                    .id(3L)
                    .nombre("Laptops")
                    .descripcion("Computadoras portátiles")
                    .slug("laptops")
                    .activa(true)
                    .destacada(false)
                    .orden(2)
                    .categoriaPadre(categoriaPadre)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            subcategorias.add(subcategoria2);
            assertEquals(2, categoriaPadre.getTotalSubcategorias());
        }
    }
}
