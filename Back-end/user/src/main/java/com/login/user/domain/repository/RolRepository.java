package com.login.user.domain.repository;

import com.login.user.domain.entity.Rol;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Rol.
 * Define los contratos de acceso a datos para roles del sistema.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
public interface RolRepository {

    /**
     * Guarda un rol en el repositorio
     */
    Rol save(Rol rol);

    /**
     * Busca un rol por su ID
     */
    Optional<Rol> findById(Long id);

    /**
     * Busca un rol por su nombre
     */
    Optional<Rol> findByNombre(String nombre);

    /**
     * Verifica si existe un rol con el nombre dado
     */
    boolean existsByNombre(String nombre);

    /**
     * Busca todos los roles activos
     */
    List<Rol> findByActivoTrue();

    /**
     * Busca roles por nombre (búsqueda parcial)
     */
    List<Rol> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Obtiene todos los roles
     */
    List<Rol> findAll();

    /**
     * Elimina un rol por su ID
     */
    void deleteById(Long id);

    /**
     * Verifica si un rol existe por su ID
     */
    boolean existsById(Long id);

    /**
     * Cuenta el total de roles
     */
    long count();

    /**
     * Cuenta roles activos
     */
    long countByActivoTrue();
}
