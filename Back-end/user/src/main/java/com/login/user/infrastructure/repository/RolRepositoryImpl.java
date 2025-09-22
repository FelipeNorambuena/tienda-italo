package com.login.user.infrastructure.repository;

import com.login.user.domain.entity.Rol;
import com.login.user.domain.repository.RolRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de Rol usando Spring Data JPA.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Repository
public interface RolRepositoryImpl extends JpaRepository<Rol, Long>, RolRepository {

    @Override
    @Query("SELECT r FROM Rol r WHERE r.nombre = :nombre")
    Optional<Rol> findByNombre(@Param("nombre") String nombre);

    @Override
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rol r WHERE r.nombre = :nombre")
    boolean existsByNombre(@Param("nombre") String nombre);

    @Override
    @Query("SELECT r FROM Rol r WHERE r.activo = true ORDER BY r.nombre")
    List<Rol> findByActivoTrue();

    @Override
    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Rol> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    @Override
    @Query("SELECT COUNT(r) FROM Rol r WHERE r.activo = true")
    long countByActivoTrue();

    // Consultas adicionales específicas

    /**
     * Busca roles ordenados por nombre
     */
    @Query("SELECT r FROM Rol r ORDER BY r.nombre ASC")
    List<Rol> findAllOrderByNombre();

    /**
     * Busca roles por descripción
     */
    @Query("SELECT r FROM Rol r WHERE LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))")
    List<Rol> findByDescripcionContainingIgnoreCase(@Param("descripcion") String descripcion);

    /**
     * Obtiene roles del sistema (roles básicos)
     */
    @Query("SELECT r FROM Rol r WHERE r.nombre IN ('ADMIN', 'CLIENTE', 'GESTOR', 'VENDEDOR')")
    List<Rol> findRolesSistema();

    /**
     * Busca roles que no están asignados a ningún usuario
     */
    @Query("SELECT r FROM Rol r WHERE SIZE(r.usuarios) = 0")
    List<Rol> findRolesSinUsuarios();

    /**
     * Cuenta usuarios por cada rol
     */
    @Query("SELECT r.nombre, COUNT(u) FROM Rol r LEFT JOIN r.usuarios u GROUP BY r.id, r.nombre")
    List<Object[]> countUsuariosPorRol();
}
