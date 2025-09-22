package com.tienda.producto.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para solicitudes de productos.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    @Size(max = 2000, message = "La descripción larga no puede exceder 2000 caracteres")
    private String descripcionLarga;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal precio;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de oferta debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio de oferta debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal precioOferta;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    @Builder.Default
    private Boolean activo = true;

    @Builder.Default
    private Boolean destacado = false;

    @Builder.Default
    private Boolean nuevo = true;

    @Size(max = 100, message = "El SKU no puede exceder 100 caracteres")
    private String sku;

    @Size(max = 100, message = "El EAN no puede exceder 100 caracteres")
    private String ean;

    @Size(max = 100, message = "El ISBN no puede exceder 100 caracteres")
    private String isbn;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    @Digits(integer = 8, fraction = 3, message = "El peso debe tener máximo 8 dígitos enteros y 3 decimales")
    private Double peso;

    @NotNull(message = "El largo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El largo debe ser mayor a 0")
    @Digits(integer = 8, fraction = 3, message = "El largo debe tener máximo 8 dígitos enteros y 3 decimales")
    private Double largo;

    @NotNull(message = "El ancho es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El ancho debe ser mayor a 0")
    @Digits(integer = 8, fraction = 3, message = "El ancho debe tener máximo 8 dígitos enteros y 3 decimales")
    private Double ancho;

    @NotNull(message = "El alto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El alto debe ser mayor a 0")
    @Digits(integer = 8, fraction = 3, message = "El alto debe tener máximo 8 dígitos enteros y 3 decimales")
    private Double alto;

    @Size(max = 50, message = "El color no puede exceder 50 caracteres")
    private String color;

    @Size(max = 50, message = "El material no puede exceder 50 caracteres")
    private String material;

    @Size(max = 50, message = "La talla no puede exceder 50 caracteres")
    private String talla;

    @Size(max = 500, message = "Las palabras clave no pueden exceder 500 caracteres")
    private String palabrasClave;

    @Size(max = 200, message = "El meta título no puede exceder 200 caracteres")
    private String metaTitulo;

    @Size(max = 500, message = "La meta descripción no puede exceder 500 caracteres")
    private String metaDescripcion;

    @Size(max = 200, message = "El slug no puede exceder 200 caracteres")
    private String slug;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    private Long marcaId;

    private List<AtributoProductoRequestDTO> atributos;

    private List<ImagenProductoRequestDTO> imagenes;

    private List<Long> productosRelacionadosIds;

    // Validaciones personalizadas
    @AssertTrue(message = "El precio de oferta debe ser menor al precio normal")
    public boolean isPrecioOfertaValido() {
        if (precioOferta == null || precio == null) {
            return true;
        }
        return precioOferta.compareTo(precio) < 0;
    }

    @AssertTrue(message = "El stock mínimo debe ser menor o igual al stock")
    public boolean isStockMinimoValido() {
        if (stockMinimo == null || stock == null) {
            return true;
        }
        return stockMinimo <= stock;
    }

    @AssertTrue(message = "El slug debe ser válido")
    public boolean isSlugValido() {
        if (slug == null || slug.isEmpty()) {
            return true;
        }
        return slug.matches("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    }

    @AssertTrue(message = "El SKU debe ser único si se proporciona")
    public boolean isSkuValido() {
        if (sku == null || sku.isEmpty()) {
            return true;
        }
        return sku.matches("^[A-Z0-9]+(?:-[A-Z0-9]+)*$");
    }

    @AssertTrue(message = "El EAN debe ser válido si se proporciona")
    public boolean isEanValido() {
        if (ean == null || ean.isEmpty()) {
            return true;
        }
        return ean.matches("^[0-9]{8,14}$");
    }

    @AssertTrue(message = "El ISBN debe ser válido si se proporciona")
    public boolean isIsbnValido() {
        if (isbn == null || isbn.isEmpty()) {
            return true;
        }
        return isbn.matches("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    }
}
