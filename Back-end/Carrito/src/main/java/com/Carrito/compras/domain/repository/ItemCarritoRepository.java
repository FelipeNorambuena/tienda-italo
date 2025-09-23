package com.Carrito.compras.domain.repository;

import com.Carrito.compras.domain.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    
    /**
     * Busca todos los items de un carrito específico
     */
    List<ItemCarrito> findByCarritoId(Long carritoId);
    
    /**
     * Busca un item específico en un carrito por producto
     */
    @Query("SELECT i FROM ItemCarrito i WHERE i.carrito.id = :carritoId AND i.productoId = :productoId")
    Optional<ItemCarrito> findByCarritoIdAndProductoId(@Param("carritoId") Long carritoId, @Param("productoId") Long productoId);
    
    /**
     * Elimina todos los items de un carrito
     */
    void deleteByCarritoId(Long carritoId);
    
    /**
     * Cuenta la cantidad de items en un carrito
     */
    @Query("SELECT COUNT(i) FROM ItemCarrito i WHERE i.carrito.id = :carritoId")
    long countByCarritoId(@Param("carritoId") Long carritoId);
}
