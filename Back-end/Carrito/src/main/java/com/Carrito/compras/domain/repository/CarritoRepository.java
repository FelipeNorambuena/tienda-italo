package com.Carrito.compras.domain.repository;

import com.Carrito.compras.domain.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    
    /**
     * Busca el carrito activo de un usuario
     */
    @Query("SELECT c FROM Carrito c WHERE c.usuarioId = :usuarioId AND c.activo = true")
    Optional<Carrito> findByUsuarioIdAndActivoTrue(@Param("usuarioId") Long usuarioId);
    
    /**
     * Verifica si un usuario tiene un carrito activo
     */
    @Query("SELECT COUNT(c) > 0 FROM Carrito c WHERE c.usuarioId = :usuarioId AND c.activo = true")
    boolean existsByUsuarioIdAndActivoTrue(@Param("usuarioId") Long usuarioId);
    
    /**
     * Desactiva todos los carritos de un usuario
     */
    @Query("UPDATE Carrito c SET c.activo = false WHERE c.usuarioId = :usuarioId")
    void desactivarCarritosByUsuarioId(@Param("usuarioId") Long usuarioId);
}
