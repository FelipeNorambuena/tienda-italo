package com.Carrito.compras.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ItemCarrito> items = new ArrayList<>();
    
    @Column(name = "total", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;
    
    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Métodos de negocio
    public void agregarItem(ItemCarrito item) {
        item.setCarrito(this);
        this.items.add(item);
        calcularTotal();
    }
    
    public void removerItem(ItemCarrito item) {
        this.items.remove(item);
        item.setCarrito(null);
        calcularTotal();
    }
    
    public void calcularTotal() {
        this.total = items.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void limpiar() {
        this.items.clear();
        this.total = BigDecimal.ZERO;
    }
    
    public boolean estaVacio() {
        return items.isEmpty();
    }
    
    public int getCantidadTotalItems() {
        return items.stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }
}
