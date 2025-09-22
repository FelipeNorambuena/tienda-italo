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
 * Entidad de dominio Marca.
 * Representa una marca de productos en el catálogo.
 * 
 * @author Tienda Italo Team
 */
@Entity
@Table(name = "marcas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 200)
    private String logo;

    @Column(length = 200)
    private String sitioWeb;

    @Column(length = 100)
    private String pais;

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
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();

    // Métodos de negocio
    public void agregarProducto(Producto producto) {
        productos.add(producto);
        producto.setMarca(this);
    }

    public void removerProducto(Producto producto) {
        productos.remove(producto);
        producto.setMarca(null);
    }

    public int getTotalProductos() {
        return productos.size();
    }

    public int getTotalProductosActivos() {
        return (int) productos.stream()
                .filter(Producto::getActivo)
                .count();
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

    public boolean tieneProductos() {
        return !productos.isEmpty();
    }

    public boolean puedeEliminarse() {
        return !tieneProductos();
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

    public String getPaisCompleto() {
        if (pais == null || pais.isEmpty()) {
            return "No especificado";
        }
        return pais;
    }

    public boolean tieneSitioWeb() {
        return sitioWeb != null && !sitioWeb.isEmpty();
    }

    public boolean tieneLogo() {
        return logo != null && !logo.isEmpty();
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
