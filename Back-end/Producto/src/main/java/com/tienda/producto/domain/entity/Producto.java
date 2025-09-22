package com.tienda.producto.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio Producto.
 * Representa un producto en el catálogo de la tienda.
 * 
 * @author Tienda Italo Team
 */
@Entity
@Table(name = "productos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 2000)
    private String descripcionLarga;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioOferta;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private Boolean destacado;

    @Column(nullable = false)
    private Boolean nuevo;

    @Column(length = 100)
    private String sku;

    @Column(length = 100)
    private String ean;

    @Column(length = 100)
    private String isbn;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private Double largo;

    @Column(nullable = false)
    private Double ancho;

    @Column(nullable = false)
    private Double alto;

    @Column(length = 50)
    private String color;

    @Column(length = 50)
    private String material;

    @Column(length = 50)
    private String talla;

    @Column(nullable = false)
    private Integer vendidos;

    @Column(nullable = false)
    private Integer visualizaciones;

    @Column(nullable = false)
    private Double calificacionPromedio;

    @Column(nullable = false)
    private Integer totalCalificaciones;

    @Column(length = 500)
    private String palabrasClave;

    @Column(length = 200)
    private String metaTitulo;

    @Column(length = 500)
    private String metaDescripcion;

    @Column(length = 200)
    private String slug;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AtributoProducto> atributos = new ArrayList<>();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ImagenProducto> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "productoOrigen", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductoRelacionado> productosRelacionados = new ArrayList<>();

    // Métodos de negocio
    public boolean tieneStock() {
        return stock > 0;
    }

    public boolean tieneStockSuficiente(int cantidad) {
        return stock >= cantidad;
    }

    public boolean estaEnOferta() {
        return precioOferta != null && precioOferta.compareTo(precio) < 0;
    }

    public BigDecimal getPrecioFinal() {
        return estaEnOferta() ? precioOferta : precio;
    }

    public BigDecimal getDescuento() {
        if (!estaEnOferta()) {
            return BigDecimal.ZERO;
        }
        return precio.subtract(precioOferta);
    }

    public BigDecimal getPorcentajeDescuento() {
        if (!estaEnOferta()) {
            return BigDecimal.ZERO;
        }
        return precio.subtract(precioOferta)
                .divide(precio, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public boolean necesitaReposicion() {
        return stock <= stockMinimo;
    }

    public void incrementarVendidos(int cantidad) {
        this.vendidos += cantidad;
    }

    public void incrementarVisualizaciones() {
        this.visualizaciones++;
    }

    public void actualizarCalificacion(double nuevaCalificacion) {
        double sumaTotal = calificacionPromedio * totalCalificaciones + nuevaCalificacion;
        totalCalificaciones++;
        calificacionPromedio = sumaTotal / totalCalificaciones;
    }

    public void reducirStock(int cantidad) {
        if (!tieneStockSuficiente(cantidad)) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    public String getNombreCompleto() {
        return String.format("%s - %s", codigo, nombre);
    }

    public boolean esDisponible() {
        return activo && tieneStock();
    }

    public String getEstadoStock() {
        if (!tieneStock()) {
            return "AGOTADO";
        } else if (necesitaReposicion()) {
            return "BAJO_STOCK";
        } else {
            return "DISPONIBLE";
        }
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void marcarComoDestacado() {
        this.destacado = true;
    }

    public void quitarDestacado() {
        this.destacado = false;
    }

    public void marcarComoNuevo() {
        this.nuevo = true;
    }

    public void quitarNuevo() {
        this.nuevo = false;
    }

    public void agregarAtributo(AtributoProducto atributo) {
        atributos.add(atributo);
        atributo.setProducto(this);
    }

    public void removerAtributo(AtributoProducto atributo) {
        atributos.remove(atributo);
        atributo.setProducto(null);
    }

    public void agregarImagen(ImagenProducto imagen) {
        imagenes.add(imagen);
        imagen.setProducto(this);
    }

    public void removerImagen(ImagenProducto imagen) {
        imagenes.remove(imagen);
        imagen.setProducto(null);
    }

    public ImagenProducto getImagenPrincipal() {
        return imagenes.stream()
                .filter(ImagenProducto::getEsPrincipal)
                .findFirst()
                .orElse(null);
    }

    public List<ImagenProducto> getImagenesSecundarias() {
        return imagenes.stream()
                .filter(img -> !img.getEsPrincipal())
                .toList();
    }

    public void agregarProductoRelacionado(Producto productoRelacionado) {
        ProductoRelacionado relacion = ProductoRelacionado.builder()
                .productoOrigen(this)
                .productoRelacionado(productoRelacionado)
                .build();
        productosRelacionados.add(relacion);
    }

    public void removerProductoRelacionado(Producto productoRelacionado) {
        productosRelacionados.removeIf(rel -> 
            rel.getProductoRelacionado().equals(productoRelacionado));
    }

    @PrePersist
    protected void onCreate() {
        if (vendidos == null) vendidos = 0;
        if (visualizaciones == null) visualizaciones = 0;
        if (calificacionPromedio == null) calificacionPromedio = 0.0;
        if (totalCalificaciones == null) totalCalificaciones = 0;
        if (activo == null) activo = true;
        if (destacado == null) destacado = false;
        if (nuevo == null) nuevo = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
