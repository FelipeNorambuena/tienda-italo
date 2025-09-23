package com.Carrito.compras.infrastructure.repository;

import com.Carrito.compras.domain.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de ItemCarrito usando JPA.
 * Esta es la capa de infraestructura que implementa los contratos del dominio.
 */
@Repository
public interface ItemCarritoJpaRepository extends JpaRepository<ItemCarrito, Long> {
    
    /**
     * Busca items por ID de carrito
     */
    List<ItemCarrito> findByCarritoId(Long carritoId);
    
    /**
     * Busca un item específico por carrito y producto
     */
    Optional<ItemCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
}
