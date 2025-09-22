package com.login.user.domain.repository;

import com.login.user.domain.entity.TokenRecuperacion;
import com.login.user.domain.entity.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad TokenRecuperacion.
 * Define los contratos de acceso a datos para tokens de recuperación.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
public interface TokenRecuperacionRepository {

    /**
     * Guarda un token de recuperación en el repositorio
     */
    TokenRecuperacion save(TokenRecuperacion token);

    /**
     * Busca un token por su valor
     */
    Optional<TokenRecuperacion> findByToken(String token);

    /**
     * Busca un token válido por su valor y tipo
     */
    Optional<TokenRecuperacion> findByTokenAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
            String token, TokenRecuperacion.TipoToken tipoToken, LocalDateTime fecha);

    /**
     * Busca tokens por usuario
     */
    List<TokenRecuperacion> findByUsuario(Usuario usuario);

    /**
     * Busca tokens por usuario y tipo
     */
    List<TokenRecuperacion> findByUsuarioAndTipoToken(Usuario usuario, TokenRecuperacion.TipoToken tipoToken);

    /**
     * Busca tokens válidos por usuario y tipo
     */
    List<TokenRecuperacion> findByUsuarioAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
            Usuario usuario, TokenRecuperacion.TipoToken tipoToken, LocalDateTime fecha);

    /**
     * Busca tokens expirados
     */
    List<TokenRecuperacion> findByFechaExpiracionBefore(LocalDateTime fecha);

    /**
     * Busca tokens no usados
     */
    List<TokenRecuperacion> findByUsadoFalse();

    /**
     * Elimina tokens por usuario
     */
    void deleteByUsuario(Usuario usuario);

    /**
     * Elimina tokens expirados
     */
    void deleteByFechaExpiracionBefore(LocalDateTime fecha);

    /**
     * Elimina tokens usados
     */
    void deleteByUsadoTrue();

    /**
     * Elimina un token por su ID
     */
    void deleteById(Long id);

    /**
     * Verifica si existe un token por su valor
     */
    boolean existsByToken(String token);

    /**
     * Cuenta tokens por usuario
     */
    long countByUsuario(Usuario usuario);

    /**
     * Cuenta tokens válidos por usuario y tipo
     */
    long countByUsuarioAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
            Usuario usuario, TokenRecuperacion.TipoToken tipoToken, LocalDateTime fecha);

    /**
     * Guarda una lista de tokens de recuperación
     */
    List<TokenRecuperacion> saveAll(List<TokenRecuperacion> tokens);
}
