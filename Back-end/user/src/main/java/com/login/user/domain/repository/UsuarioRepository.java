package com.login.user.domain.repository;

import com.login.user.domain.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Usuario.
 * Define los contratos de acceso a datos sin depender de la implementación específica.
 * Sigue los principios de Clean Architecture.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
public interface UsuarioRepository {

    /**
     * Guarda un usuario en el repositorio
     */
    Usuario save(Usuario usuario);

    /**
     * Busca un usuario por su ID
     */
    Optional<Usuario> findById(Long id);

    /**
     * Busca un usuario por su email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios activos
     */
    Page<Usuario> findByActivoTrue(Pageable pageable);

    /**
     * Busca usuarios por nombre o apellido (búsqueda parcial)
     */
    Page<Usuario> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido, Pageable pageable);

    /**
     * Busca usuarios que no han verificado su email
     */
    List<Usuario> findByEmailVerificadoFalse();

    /**
     * Busca usuarios por rol
     */
    Page<Usuario> findByRoles_Nombre(String nombreRol, Pageable pageable);

    /**
     * Busca usuarios con intentos fallidos mayor o igual al especificado
     */
    List<Usuario> findByIntentosFallidosLoginGreaterThanEqual(Integer intentos);

    /**
     * Busca usuarios bloqueados
     */
    List<Usuario> findByBloqueadoHastaAfter(LocalDateTime fecha);

    /**
     * Busca usuarios creados después de una fecha específica
     */
    List<Usuario> findByCreatedAtAfter(LocalDateTime fecha);

    /**
     * Busca usuarios que no han accedido desde una fecha específica
     */
    List<Usuario> findByFechaUltimoAccesoBeforeOrFechaUltimoAccesoIsNull(LocalDateTime fecha);

    /**
     * Elimina un usuario por su ID
     */
    void deleteById(Long id);

    /**
     * Verifica si un usuario existe por su ID
     */
    boolean existsById(Long id);

    /**
     * Cuenta el total de usuarios
     */
    long count();

    /**
     * Cuenta usuarios activos
     */
    long countByActivoTrue();

    /**
     * Cuenta usuarios por rol
     */
    long countByRoles_Nombre(String nombreRol);

    /**
     * Obtiene todos los usuarios (con paginación)
     */
    Page<Usuario> findAll(Pageable pageable);

    /**
     * Obtiene todos los usuarios
     */
    List<Usuario> findAll();

    /**
     * Busca usuarios activos con último acceso reciente
     */
    List<Usuario> findUsuariosActivosRecientes(LocalDateTime fecha);
}
