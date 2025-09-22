package com.login.user.infrastructure.repository;

import com.login.user.domain.entity.TokenRecuperacion;
import com.login.user.domain.entity.Usuario;
import com.login.user.domain.repository.TokenRecuperacionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de TokenRecuperacion usando Spring Data JPA.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Repository
public interface TokenRecuperacionRepositoryImpl extends JpaRepository<TokenRecuperacion, Long>, TokenRecuperacionRepository {

    @Override
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.token = :token")
    Optional<TokenRecuperacion> findByToken(@Param("token") String token);

    @Override
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.token = :token AND t.tipoToken = :tipoToken " +
           "AND t.usado = false AND t.fechaExpiracion > :fecha")
    Optional<TokenRecuperacion> findByTokenAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
            @Param("token") String token, 
            @Param("tipoToken") TokenRecuperacion.TipoToken tipoToken, 
            @Param("fecha") LocalDateTime fecha);

    @Override
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.usuario = :usuario ORDER BY t.createdAt DESC")
    List<TokenRecuperacion> findByUsuario(@Param("usuario") Usuario usuario);

    @Override
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.usuario = :usuario AND t.tipoToken = :tipoToken " +
           "ORDER BY t.createdAt DESC")
    List<TokenRecuperacion> findByUsuarioAndTipoToken(@Param("usuario") Usuario usuario, 
                                                      @Param("tipoToken") TokenRecuperacion.TipoToken tipoToken);

    @Override
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.usuario = :usuario AND t.tipoToken = :tipoToken " +
           "AND t.usado = false AND t.fechaExpiracion > :fecha ORDER BY t.createdAt DESC")
    List<TokenRecuperacion> findByUsuarioAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
            @Param("usuario") Usuario usuario, 
            @Param("tipoToken") TokenRecuperacion.TipoToken tipoToken, 
            @Param("fecha") LocalDateTime fecha);

    @Override
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.fechaExpiracion < :fecha")
    List<TokenRecuperacion> findByFechaExpiracionBefore(@Param("fecha") LocalDateTime fecha);

    @Override
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.usado = false")
    List<TokenRecuperacion> findByUsadoFalse();

    @Override
    @Modifying
    @Query("DELETE FROM TokenRecuperacion t WHERE t.usuario = :usuario")
    void deleteByUsuario(@Param("usuario") Usuario usuario);

    @Override
    @Modifying
    @Query("DELETE FROM TokenRecuperacion t WHERE t.fechaExpiracion < :fecha")
    void deleteByFechaExpiracionBefore(@Param("fecha") LocalDateTime fecha);

    @Override
    @Modifying
    @Query("DELETE FROM TokenRecuperacion t WHERE t.usado = true")
    void deleteByUsadoTrue();

    @Override
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TokenRecuperacion t WHERE t.token = :token")
    boolean existsByToken(@Param("token") String token);

    @Override
    @Query("SELECT COUNT(t) FROM TokenRecuperacion t WHERE t.usuario = :usuario")
    long countByUsuario(@Param("usuario") Usuario usuario);

    @Override
    @Query("SELECT COUNT(t) FROM TokenRecuperacion t WHERE t.usuario = :usuario AND t.tipoToken = :tipoToken " +
           "AND t.usado = false AND t.fechaExpiracion > :fecha")
    long countByUsuarioAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
            @Param("usuario") Usuario usuario, 
            @Param("tipoToken") TokenRecuperacion.TipoToken tipoToken, 
            @Param("fecha") LocalDateTime fecha);

    // Consultas adicionales específicas

    /**
     * Busca tokens válidos por tipo
     */
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.tipoToken = :tipoToken " +
           "AND t.usado = false AND t.fechaExpiracion > :fecha")
    List<TokenRecuperacion> findTokensValidosPorTipo(@Param("tipoToken") TokenRecuperacion.TipoToken tipoToken, 
                                                     @Param("fecha") LocalDateTime fecha);

    /**
     * Cuenta tokens por tipo
     */
    @Query("SELECT t.tipoToken, COUNT(t) FROM TokenRecuperacion t GROUP BY t.tipoToken")
    List<Object[]> countByTipoToken();

    /**
     * Busca tokens creados en un rango de fechas
     */
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.createdAt BETWEEN :fechaInicio AND :fechaFin")
    List<TokenRecuperacion> findByCreatedAtBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                   @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Busca el último token válido por usuario y tipo
     */
    @Query("SELECT t FROM TokenRecuperacion t WHERE t.usuario = :usuario AND t.tipoToken = :tipoToken " +
           "AND t.usado = false AND t.fechaExpiracion > :fecha ORDER BY t.createdAt DESC")
    Optional<TokenRecuperacion> findUltimoTokenValidoPorUsuarioYTipo(@Param("usuario") Usuario usuario, 
                                                                     @Param("tipoToken") TokenRecuperacion.TipoToken tipoToken, 
                                                                     @Param("fecha") LocalDateTime fecha);

    /**
     * Elimina tokens antiguos (limpieza automática)
     */
    @Modifying
    @Query("DELETE FROM TokenRecuperacion t WHERE t.createdAt < :fechaLimite")
    void deleteTokensAntiguos(@Param("fechaLimite") LocalDateTime fechaLimite);
}
