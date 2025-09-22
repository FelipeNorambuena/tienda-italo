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

/**
 * Entidad de dominio ProductoRelacionado.
 * Representa una relación entre productos (productos relacionados).
 * 
 * @author Tienda Italo Team
 */
@Entity
@Table(name = "productos_relacionados")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductoRelacionado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String tipoRelacion;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activa;

    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false)
    private Double peso;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_origen_id", nullable = false)
    private Producto productoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_relacionado_id", nullable = false)
    private Producto productoRelacionado;

    // Métodos de negocio
    public String getTipoRelacionCompleto() {
        if (tipoRelacion == null || tipoRelacion.isEmpty()) {
            return "RELACIONADO";
        }
        return tipoRelacion;
    }

    public String getDescripcionCompleta() {
        if (descripcion != null && !descripcion.isEmpty()) {
            return descripcion;
        }
        return String.format("Producto relacionado: %s", 
                productoRelacionado != null ? productoRelacionado.getNombre() : "N/A");
    }

    public boolean esRelacionSimetrica() {
        return "SIMILAR".equalsIgnoreCase(tipoRelacion) || 
               "COMPLEMENTARIO".equalsIgnoreCase(tipoRelacion);
    }

    public boolean esRelacionAsimetrica() {
        return "ACCESORIO".equalsIgnoreCase(tipoRelacion) || 
               "UPSELL".equalsIgnoreCase(tipoRelacion) ||
               "CROSSSELL".equalsIgnoreCase(tipoRelacion);
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }

    public void incrementarOrden() {
        this.orden++;
    }

    public void decrementarOrden() {
        if (this.orden > 0) {
            this.orden--;
        }
    }

    public void incrementarPeso() {
        this.peso += 0.1;
    }

    public void decrementarPeso() {
        if (this.peso > 0.1) {
            this.peso -= 0.1;
        }
    }

    public boolean esRelacionFuerte() {
        return peso >= 0.8;
    }

    public boolean esRelacionMedia() {
        return peso >= 0.5 && peso < 0.8;
    }

    public boolean esRelacionDebil() {
        return peso < 0.5;
    }

    public String getNivelRelacion() {
        if (esRelacionFuerte()) return "FUERTE";
        if (esRelacionMedia()) return "MEDIA";
        return "DEBIL";
    }

    public boolean esRelacionValida() {
        return productoOrigen != null && 
               productoRelacionado != null && 
               !productoOrigen.equals(productoRelacionado);
    }

    public String getNombreRelacion() {
        return String.format("%s -> %s", 
                productoOrigen != null ? productoOrigen.getNombre() : "N/A",
                productoRelacionado != null ? productoRelacionado.getNombre() : "N/A");
    }

    public boolean esRelacionBidireccional() {
        return "SIMILAR".equalsIgnoreCase(tipoRelacion) || 
               "COMPLEMENTARIO".equalsIgnoreCase(tipoRelacion);
    }

    public boolean esRelacionUnidireccional() {
        return "ACCESORIO".equalsIgnoreCase(tipoRelacion) || 
               "UPSELL".equalsIgnoreCase(tipoRelacion) ||
               "CROSSSELL".equalsIgnoreCase(tipoRelacion);
    }

    public String getTipoRelacionDescripcion() {
        return switch (tipoRelacion != null ? tipoRelacion.toUpperCase() : "") {
            case "SIMILAR" -> "Producto similar";
            case "COMPLEMENTARIO" -> "Producto complementario";
            case "ACCESORIO" -> "Accesorio para el producto";
            case "UPSELL" -> "Producto de mayor valor";
            case "CROSSSELL" -> "Producto relacionado";
            default -> "Producto relacionado";
        };
    }

    @PrePersist
    protected void onCreate() {
        if (activa == null) activa = true;
        if (orden == null) orden = 0;
        if (peso == null) peso = 0.5;
        if (tipoRelacion == null) tipoRelacion = "RELACIONADO";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
