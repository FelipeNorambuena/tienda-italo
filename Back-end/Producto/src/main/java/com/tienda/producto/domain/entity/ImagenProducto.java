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
 * Entidad de dominio ImagenProducto.
 * Representa una imagen asociada a un producto.
 * 
 * @author Tienda Italo Team
 */
@Entity
@Table(name = "imagenes_producto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 200)
    private String nombreArchivo;

    @Column(length = 100)
    private String tipoMime;

    @Column(nullable = false)
    private Long tamanioBytes;

    @Column(nullable = false)
    private Integer ancho;

    @Column(nullable = false)
    private Integer alto;

    @Column(length = 200)
    private String alt;

    @Column(length = 500)
    private String titulo;

    @Column(nullable = false)
    private Boolean esPrincipal;

    @Column(nullable = false)
    private Boolean activa;

    @Column(nullable = false)
    private Integer orden;

    @Column(length = 200)
    private String miniatura;

    @Column(length = 200)
    private String versionGrande;

    @Column(length = 200)
    private String versionMediana;

    @Column(length = 200)
    private String versionPequena;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Métodos de negocio
    public String getUrlCompleta() {
        if (url.startsWith("http")) {
            return url;
        }
        // Aquí se podría agregar la URL base del servidor
        return url;
    }

    public String getNombreCompleto() {
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            return "imagen_" + id;
        }
        return nombreArchivo;
    }

    public String getTamanioFormateado() {
        if (tamanioBytes < 1024) {
            return tamanioBytes + " B";
        } else if (tamanioBytes < 1024 * 1024) {
            return String.format("%.1f KB", tamanioBytes / 1024.0);
        } else {
            return String.format("%.1f MB", tamanioBytes / (1024.0 * 1024.0));
        }
    }

    public String getDimensiones() {
        return String.format("%dx%d", ancho, alto);
    }

    public String getAspectRatio() {
        if (alto == 0) return "0:0";
        int gcd = gcd(ancho, alto);
        return String.format("%d:%d", ancho / gcd, alto / gcd);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public boolean esImagenValida() {
        return ancho > 0 && alto > 0 && tamanioBytes > 0;
    }

    public boolean esImagenGrande() {
        return ancho > 1920 || alto > 1080;
    }

    public boolean esImagenPequena() {
        return ancho < 300 || alto < 300;
    }

    public String getVersionPorTamanio(String tamanio) {
        return switch (tamanio.toUpperCase()) {
            case "GRANDE" -> versionGrande != null ? versionGrande : url;
            case "MEDIANA" -> versionMediana != null ? versionMediana : url;
            case "PEQUENA" -> versionPequena != null ? versionPequena : url;
            case "MINIATURA" -> miniatura != null ? miniatura : url;
            default -> url;
        };
    }

    public void marcarComoPrincipal() {
        this.esPrincipal = true;
    }

    public void quitarPrincipal() {
        this.esPrincipal = false;
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

    public String getAltText() {
        if (alt != null && !alt.isEmpty()) {
            return alt;
        }
        return "Imagen de " + (producto != null ? producto.getNombre() : "producto");
    }

    public String getTituloCompleto() {
        if (titulo != null && !titulo.isEmpty()) {
            return titulo;
        }
        return "Imagen de " + (producto != null ? producto.getNombre() : "producto");
    }

    public boolean esImagenCuadrada() {
        return ancho.equals(alto);
    }

    public boolean esImagenHorizontal() {
        return ancho > alto;
    }

    public boolean esImagenVertical() {
        return alto > ancho;
    }

    public String getOrientacion() {
        if (esImagenCuadrada()) return "CUADRADA";
        if (esImagenHorizontal()) return "HORIZONTAL";
        return "VERTICAL";
    }

    @PrePersist
    protected void onCreate() {
        if (esPrincipal == null) esPrincipal = false;
        if (activa == null) activa = true;
        if (orden == null) orden = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
