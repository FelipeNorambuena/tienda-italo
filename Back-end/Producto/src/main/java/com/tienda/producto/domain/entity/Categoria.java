package com.tienda.producto.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio Categoria.
 * Representa una categoría de productos en el catálogo.
 * 
 * @author Tienda Italo Team
 */
@Entity
@Table(name = "categorias")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 200)
    private String imagen;

    @Column(length = 200)
    private String icono;

    @Column(nullable = false)
    private Boolean activa;

    @Column(nullable = false)
    private Boolean destacada;

    @Column(nullable = false)
    private Integer orden;

    @Column(length = 200)
    private String slug;

    @Column(length = 200)
    private String metaTitulo;

    @Column(length = 500)
    private String metaDescripcion;

    @Column(length = 500)
    private String palabrasClave;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre_id")
    private Categoria categoriaPadre;

    @OneToMany(mappedBy = "categoriaPadre", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Categoria> subcategorias = new ArrayList<>();

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();

    // Métodos de negocio
    public boolean esCategoriaRaiz() {
        return categoriaPadre == null;
    }

    public boolean esSubcategoria() {
        return categoriaPadre != null;
    }

    public int getNivel() {
        if (esCategoriaRaiz()) {
            return 0;
        }
        return categoriaPadre.getNivel() + 1;
    }

    public String getRutaCompleta() {
        if (esCategoriaRaiz()) {
            return nombre;
        }
        return categoriaPadre.getRutaCompleta() + " > " + nombre;
    }

    public void agregarSubcategoria(Categoria subcategoria) {
        subcategorias.add(subcategoria);
        subcategoria.setCategoriaPadre(this);
    }

    public void removerSubcategoria(Categoria subcategoria) {
        subcategorias.remove(subcategoria);
        subcategoria.setCategoriaPadre(null);
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        producto.setCategoria(this);
    }

    public void removerProducto(Producto producto) {
        productos.remove(producto);
        producto.setCategoria(null);
    }

    public int getTotalProductos() {
        return productos.size();
    }

    public int getTotalProductosActivos() {
        return (int) productos.stream()
                .filter(Producto::getActivo)
                .count();
    }

    public int getTotalSubcategorias() {
        return subcategorias.size();
    }

    public int getTotalSubcategoriasActivas() {
        return (int) subcategorias.stream()
                .filter(Categoria::getActiva)
                .count();
    }

    public List<Categoria> getSubcategoriasActivas() {
        return subcategorias.stream()
                .filter(Categoria::getActiva)
                .toList();
    }

    public List<Producto> getProductosActivos() {
        return productos.stream()
                .filter(Producto::getActivo)
                .toList();
    }

    public List<Producto> getProductosDestacados() {
        return productos.stream()
                .filter(Producto::getActivo)
                .filter(Producto::getDestacado)
                .toList();
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }

    public void marcarComoDestacada() {
        this.destacada = true;
    }

    public void quitarDestacada() {
        this.destacada = false;
    }

    public boolean tieneSubcategorias() {
        return !subcategorias.isEmpty();
    }

    public boolean tieneProductos() {
        return !productos.isEmpty();
    }

    public boolean puedeEliminarse() {
        return !tieneSubcategorias() && !tieneProductos();
    }

    public void incrementarOrden() {
        this.orden++;
    }

    public void decrementarOrden() {
        if (this.orden > 0) {
            this.orden--;
        }
    }

    public String getNombreCompleto() {
        return String.format("%s (%d productos)", nombre, getTotalProductos());
    }

    public boolean esVisible() {
        return activa;
    }

    @PrePersist
    protected void onCreate() {
        if (activa == null) activa = true;
        if (destacada == null) destacada = false;
        if (orden == null) orden = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
