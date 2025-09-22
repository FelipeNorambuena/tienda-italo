package com.login.user.infrastructure.repository;

import com.login.user.domain.entity.Usuario;
import com.login.user.domain.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de Usuario usando Spring Data JPA.
 * Esta implementación maneja la persistencia de datos en la base de datos.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Repository
public interface UsuarioRepositoryImpl extends JpaRepository<Usuario, Long>, UsuarioRepository {

    @Override
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> findByEmail(@Param("email") String email);

    @Override
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Override
    @Query("SELECT u FROM Usuario u WHERE u.activo = true")
    Page<Usuario> findByActivoTrue(Pageable pageable);

    @Override
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    Page<Usuario> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            @Param("nombre") String nombre, 
            @Param("apellido") String apellido, 
            Pageable pageable);

    @Override
    @Query("SELECT u FROM Usuario u WHERE u.emailVerificado = false")
    List<Usuario> findByEmailVerificadoFalse();

    @Override
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :nombreRol")
    Page<Usuario> findByRoles_Nombre(@Param("nombreRol") String nombreRol, Pageable pageable);

    @Override
    @Query("SELECT u FROM Usuario u WHERE u.intentosFallidosLogin >= :intentos")
    List<Usuario> findByIntentosFallidosLoginGreaterThanEqual(@Param("intentos") Integer intentos);

    @Override
    @Query("SELECT u FROM Usuario u WHERE u.bloqueadoHasta > :fecha")
    List<Usuario> findByBloqueadoHastaAfter(@Param("fecha") LocalDateTime fecha);

    @Override
    @Query("SELECT u FROM Usuario u WHERE u.createdAt > :fecha")
    List<Usuario> findByCreatedAtAfter(@Param("fecha") LocalDateTime fecha);

    @Override
    @Query("SELECT u FROM Usuario u WHERE u.fechaUltimoAcceso < :fecha OR u.fechaUltimoAcceso IS NULL")
    List<Usuario> findByFechaUltimoAccesoBeforeOrFechaUltimoAccesoIsNull(@Param("fecha") LocalDateTime fecha);

    @Override
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countByActivoTrue();

    @Override
    @Query("SELECT COUNT(u) FROM Usuario u JOIN u.roles r WHERE r.nombre = :nombreRol")
    long countByRoles_Nombre(@Param("nombreRol") String nombreRol);

    // Consultas adicionales específicas para la implementación

    /**
     * Busca usuarios por email que contenga el término especificado
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Usuario> findByEmailContainingIgnoreCase(@Param("email") String email, Pageable pageable);

    /**
     * Busca usuarios creados en un rango de fechas
     */
    @Query("SELECT u FROM Usuario u WHERE u.createdAt BETWEEN :fechaInicio AND :fechaFin")
    List<Usuario> findByCreatedAtBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                        @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Busca usuarios que requieren verificación de email
     */
    @Query("SELECT u FROM Usuario u WHERE u.emailVerificado = false AND u.activo = true")
    List<Usuario> findUsuariosParaVerificarEmail();

    /**
     * Busca usuarios con múltiples roles
     */
    @Query("SELECT u FROM Usuario u WHERE SIZE(u.roles) > 1")
    List<Usuario> findUsuariosConMultiplesRoles();

    /**
     * Obtiene estadísticas de usuarios por mes
     */
    @Query("SELECT YEAR(u.createdAt) as año, MONTH(u.createdAt) as mes, COUNT(u) as total " +
           "FROM Usuario u WHERE u.createdAt >= :fechaInicio " +
           "GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
           "ORDER BY YEAR(u.createdAt) DESC, MONTH(u.createdAt) DESC")
    List<Object[]> findEstadisticasPorMes(@Param("fechaInicio") LocalDateTime fechaInicio);

    /**
     * Busca usuarios activos con último acceso reciente
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = true AND u.fechaUltimoAcceso > :fecha")
    List<Usuario> findUsuariosActivosRecientes(@Param("fecha") LocalDateTime fecha);

    /**
     * Cuenta usuarios por estado de verificación
     */
    @Query("SELECT u.emailVerificado, COUNT(u) FROM Usuario u GROUP BY u.emailVerificado")
    List<Object[]> countByEmailVerificado();

    /**
     * Busca usuarios sin direcciones
     */
    @Query("SELECT u FROM Usuario u WHERE SIZE(u.direcciones) = 0")
    List<Usuario> findUsuariosSinDirecciones();

    /**
     * Busca usuarios con direcciones principales
     */
    @Query("SELECT u FROM Usuario u WHERE EXISTS " +
           "(SELECT d FROM DireccionUsuario d WHERE d.usuario = u AND d.esPrincipal = true)")
    List<Usuario> findUsuariosConDireccionPrincipal();
}
