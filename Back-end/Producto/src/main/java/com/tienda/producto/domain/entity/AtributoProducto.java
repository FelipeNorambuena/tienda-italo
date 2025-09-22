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
 * Entidad de dominio AtributoProducto.
 * Representa un atributo específico de un producto.
 * 
 * @author Tienda Italo Team
 */
@Entity
@Table(name = "atributos_producto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AtributoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String valor;

    @Column(length = 50)
    private String unidad;

    @Column(nullable = false)
    private Boolean esVisible;

    @Column(nullable = false)
    private Boolean esFiltrable;

    @Column(nullable = false)
    private Integer orden;

    @Column(length = 100)
    private String tipo;

    @Column(length = 500)
    private String opciones;

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
    public String getValorCompleto() {
        if (unidad == null || unidad.isEmpty()) {
            return valor;
        }
        return String.format("%s %s", valor, unidad);
    }

    public String getNombreCompleto() {
        return String.format("%s: %s", nombre, getValorCompleto());
    }

    public boolean esNumerico() {
        return "NUMERICO".equalsIgnoreCase(tipo);
    }

    public boolean esTexto() {
        return "TEXTO".equalsIgnoreCase(tipo);
    }

    public boolean esBooleano() {
        return "BOOLEANO".equalsIgnoreCase(tipo);
    }

    public boolean esLista() {
        return "LISTA".equalsIgnoreCase(tipo);
    }

    public String[] getOpcionesLista() {
        if (opciones == null || opciones.isEmpty()) {
            return new String[0];
        }
        return opciones.split(",");
    }

    public void setOpcionesLista(String[] opcionesArray) {
        if (opcionesArray == null || opcionesArray.length == 0) {
            this.opciones = null;
        } else {
            this.opciones = String.join(",", opcionesArray);
        }
    }

    public boolean tieneOpciones() {
        return opciones != null && !opciones.isEmpty();
    }

    public void incrementarOrden() {
        this.orden++;
    }

    public void decrementarOrden() {
        if (this.orden > 0) {
            this.orden--;
        }
    }

    public void activar() {
        this.esVisible = true;
    }

    public void desactivar() {
        this.esVisible = false;
    }

    public void habilitarFiltro() {
        this.esFiltrable = true;
    }

    public void deshabilitarFiltro() {
        this.esFiltrable = false;
    }

    public boolean puedeFiltrarse() {
        return esFiltrable && esVisible;
    }

    @PrePersist
    protected void onCreate() {
        if (esVisible == null) esVisible = true;
        if (esFiltrable == null) esFiltrable = false;
        if (orden == null) orden = 0;
        if (tipo == null) tipo = "TEXTO";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
