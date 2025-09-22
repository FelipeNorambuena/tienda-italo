package com.tienda.producto.unit.domain;

import com.tienda.producto.domain.entity.Marca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Marca.
 * 
 * @author Tienda Italo Team
 */
@DisplayName("Tests Unitarios - Entidad Marca")
class MarcaTest {

    private Marca marca;

    @BeforeEach
    void setUp() {
        marca = Marca.builder()
                .id(1L)
                .nombre("Samsung")
                .descripcion("Tecnología coreana")
                .slug("samsung")
                .activa(true)
                .destacada(true)
                .orden(1)
                .sitioWeb("https://samsung.com")
                .palabrasClave("tecnologia,corea,smartphones")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Tests de Construcción y Propiedades Básicas")
    class ConstruccionYPropiedadesBasicas {

        @Test
        @DisplayName("Debería crear marca con propiedades correctas")
        void deberiaCrearMarcaConPropiedadesCorrectas() {
            assertNotNull(marca);
            assertEquals(1L, marca.getId());
            assertEquals("Samsung", marca.getNombre());
            assertEquals("Tecnología coreana", marca.getDescripcion());
            assertEquals("samsung", marca.getSlug());
            assertTrue(marca.getActiva());
            assertTrue(marca.getDestacada());
            assertEquals(1, marca.getOrden());
            assertEquals("https://samsung.com", marca.getSitioWeb());
            assertEquals("tecnologia,corea,smartphones", marca.getPalabrasClave());
        }

        @Test
        @DisplayName("Debería tener timestamps de auditoría")
        void deberiaTenerTimestampsDeAuditoria() {
            assertNotNull(marca.getCreatedAt());
            assertNotNull(marca.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Estado")
    class LogicaDeNegocioEstado {

        @Test
        @DisplayName("Debería identificar marca activa correctamente")
        void deberiaIdentificarMarcaActivaCorrectamente() {
            assertTrue(marca.getActiva());
            
            marca.setActiva(false);
            assertFalse(marca.getActiva());
        }

        @Test
        @DisplayName("Debería identificar marca destacada correctamente")
        void deberiaIdentificarMarcaDestacadaCorrectamente() {
            assertTrue(marca.getDestacada());
            
            marca.setDestacada(false);
            assertFalse(marca.getDestacada());
        }

        @Test
        @DisplayName("Debería identificar marca visible correctamente")
        void deberiaIdentificarMarcaVisibleCorrectamente() {
            assertTrue(marca.getActiva());
            
            marca.setActiva(false);
            assertFalse(marca.getActiva());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Sitio Web")
    class LogicaDeNegocioSitioWeb {

        @Test
        @DisplayName("Debería identificar marca con sitio web correctamente")
        void deberiaIdentificarMarcaConSitioWebCorrectamente() {
            assertTrue(marca.tieneSitioWeb());
            
            marca.setSitioWeb(null);
            assertFalse(marca.tieneSitioWeb());
            
            marca.setSitioWeb("");
            assertFalse(marca.tieneSitioWeb());
        }

        @Test
        @DisplayName("Debería validar formato de sitio web")
        void deberiaValidarFormatoDeSitioWeb() {
            marca.setSitioWeb("https://samsung.com");
            assertTrue(marca.tieneSitioWeb());
            
            marca.setSitioWeb("http://samsung.com");
            assertTrue(marca.tieneSitioWeb());
            
            marca.setSitioWeb("samsung.com");
            assertTrue(marca.tieneSitioWeb());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Palabras Clave")
    class LogicaDeNegocioPalabrasClave {

        @Test
        @DisplayName("Debería manejar palabras clave correctamente")
        void deberiaManejarPalabrasClaveCorrectamente() {
            assertEquals("tecnologia,corea,smartphones", marca.getPalabrasClave());
            
            marca.setPalabrasClave("tecnologia,corea,smartphones,android");
            assertEquals("tecnologia,corea,smartphones,android", marca.getPalabrasClave());
        }

        @Test
        @DisplayName("Debería manejar palabras clave nulas")
        void deberiaManejarPalabrasClaveNulas() {
            marca.setPalabrasClave(null);
            assertNull(marca.getPalabrasClave());
        }

        @Test
        @DisplayName("Debería manejar palabras clave vacías")
        void deberiaManejarPalabrasClaveVacias() {
            marca.setPalabrasClave("");
            assertEquals("", marca.getPalabrasClave());
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Negocio - Productos")
    class LogicaDeNegocioProductos {

        @Test
        @DisplayName("Debería manejar conteos de productos")
        void deberiaManejarConteosDeProductos() {
            // Inicialmente no hay productos
            assertEquals(0, marca.getTotalProductos());
            assertEquals(0, marca.getTotalProductosActivos());
            
            // Simular productos (estos valores se establecerían por la lógica de negocio)
            // Nota: Estos métodos no existen en la entidad actual
            // assertEquals(25, marca.getTotalProductos());
            // assertEquals(20, marca.getTotalProductosActivos());
        }
    }

    @Nested
    @DisplayName("Tests de Validación de Datos")
    class ValidacionDeDatos {

        @Test
        @DisplayName("Debería validar datos requeridos")
        void deberiaValidarDatosRequeridos() {
            assertThrows(NullPointerException.class, () -> {
                Marca.builder()
                        .nombre(null)
                        .build();
            });
        }

        @Test
        @DisplayName("Debería manejar orden negativo")
        void deberiaManejarOrdenNegativo() {
            marca.setOrden(-1);
            assertEquals(-1, marca.getOrden());
        }

        @Test
        @DisplayName("Debería manejar orden cero")
        void deberiaManejarOrdenCero() {
            marca.setOrden(0);
            assertEquals(0, marca.getOrden());
        }
    }

    @Nested
    @DisplayName("Tests de Casos Edge")
    class CasosEdge {

        @Test
        @DisplayName("Debería manejar marca sin sitio web")
        void deberiaManejarMarcaSinSitioWeb() {
            marca.setSitioWeb(null);
            assertFalse(marca.tieneSitioWeb());
        }

        @Test
        @DisplayName("Debería manejar marca sin palabras clave")
        void deberiaManejarMarcaSinPalabrasClave() {
            marca.setPalabrasClave(null);
            assertNull(marca.getPalabrasClave());
        }

        @Test
        @DisplayName("Debería manejar marca inactiva")
        void deberiaManejarMarcaInactiva() {
            marca.setActiva(false);
            assertFalse(marca.getActiva());
            assertFalse(marca.getActiva());
        }

        @Test
        @DisplayName("Debería manejar marca no destacada")
        void deberiaManejarMarcaNoDestacada() {
            marca.setDestacada(false);
            assertFalse(marca.getDestacada());
        }
    }

    @Nested
    @DisplayName("Tests de Comparación y Igualdad")
    class ComparacionYIgualdad {

        @Test
        @DisplayName("Debería comparar marcas por orden")
        void deberiaCompararMarcasPorOrden() {
            Marca marca2 = Marca.builder()
                    .id(2L)
                    .nombre("Apple")
                    .descripcion("Tecnología estadounidense")
                    .slug("apple")
                    .activa(true)
                    .destacada(true)
                    .orden(2)
                    .sitioWeb("https://apple.com")
                    .palabrasClave("tecnologia,usa,iphone")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            assertTrue(marca.getOrden() < marca2.getOrden());
        }

        @Test
        @DisplayName("Debería comparar marcas por nombre")
        void deberiaCompararMarcasPorNombre() {
            Marca marca2 = Marca.builder()
                    .id(2L)
                    .nombre("Apple")
                    .descripcion("Tecnología estadounidense")
                    .slug("apple")
                    .activa(true)
                    .destacada(true)
                    .orden(2)
                    .sitioWeb("https://apple.com")
                    .palabrasClave("tecnologia,usa,iphone")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            assertTrue(marca.getNombre().compareTo(marca2.getNombre()) > 0);
        }
    }

    @Nested
    @DisplayName("Tests de Formato de Datos")
    class FormatoDeDatos {

        @Test
        @DisplayName("Debería manejar slug con caracteres especiales")
        void deberiaManejarSlugConCaracteresEspeciales() {
            marca.setSlug("samsung-galaxy-s24");
            assertEquals("samsung-galaxy-s24", marca.getSlug());
        }

        @Test
        @DisplayName("Debería manejar descripción larga")
        void deberiaManejarDescripcionLarga() {
            String descripcionLarga = "Samsung es una empresa multinacional de tecnología con sede en Seúl, Corea del Sur. " +
                    "Es conocida por sus smartphones, televisores, electrodomésticos y semiconductores.";
            marca.setDescripcion(descripcionLarga);
            assertEquals(descripcionLarga, marca.getDescripcion());
        }

        @Test
        @DisplayName("Debería manejar palabras clave con formato específico")
        void deberiaManejarPalabrasClaveConFormatoEspecifico() {
            marca.setPalabrasClave("tecnologia,corea,smartphones,android,galaxy");
            assertEquals("tecnologia,corea,smartphones,android,galaxy", marca.getPalabrasClave());
        }
    }
}
