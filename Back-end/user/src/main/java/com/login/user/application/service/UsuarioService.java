package com.login.user.application.service;

import com.login.user.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de usuarios.
 * Define los casos de uso principales del dominio de usuarios.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
public interface UsuarioService {

    /**
     * Registra un nuevo usuario en el sistema
     */
    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO usuarioRequest);

    /**
     * Autentica un usuario y genera tokens de acceso
     */
    LoginResponseDTO autenticarUsuario(LoginRequestDTO loginRequest);

    /**
     * Renueva un token de acceso usando el refresh token
     */
    LoginResponseDTO renovarToken(String refreshToken);

    /**
     * Busca un usuario por su ID
     */
    Optional<UsuarioResponseDTO> buscarUsuarioPorId(Long id);

    /**
     * Busca un usuario por su email
     */
    Optional<UsuarioResponseDTO> buscarUsuarioPorEmail(String email);

    /**
     * Actualiza la información de un usuario
     */
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO usuarioRequest);

    /**
     * Cambia la contraseña de un usuario
     */
    void cambiarPassword(Long id, String passwordActual, String passwordNueva);

    /**
     * Activa o desactiva un usuario
     */
    UsuarioResponseDTO cambiarEstadoUsuario(Long id, Boolean activo);

    /**
     * Verifica el email de un usuario usando un token
     */
    void verificarEmail(String token);

    /**
     * Solicita recuperación de contraseña
     */
    void solicitarRecuperacionPassword(String email);

    /**
     * Restablece la contraseña usando un token de recuperación
     */
    void restablecerPassword(String token, String nuevaPassword);

    /**
     * Obtiene todos los usuarios con paginación
     */
    Page<UsuarioResponseDTO> obtenerUsuarios(Pageable pageable);

    /**
     * Busca usuarios por nombre o apellido
     */
    Page<UsuarioResponseDTO> buscarUsuariosPorNombre(String termino, Pageable pageable);

    /**
     * Busca usuarios por rol
     */
    Page<UsuarioResponseDTO> buscarUsuariosPorRol(String nombreRol, Pageable pageable);

    /**
     * Obtiene usuarios activos
     */
    Page<UsuarioResponseDTO> obtenerUsuariosActivos(Pageable pageable);

    /**
     * Elimina un usuario (soft delete)
     */
    void eliminarUsuario(Long id);

    /**
     * Asigna un rol a un usuario
     */
    UsuarioResponseDTO asignarRol(Long usuarioId, Long rolId);

    /**
     * Remueve un rol de un usuario
     */
    UsuarioResponseDTO removerRol(Long usuarioId, Long rolId);

    /**
     * Desbloquea un usuario manualmente
     */
    UsuarioResponseDTO desbloquearUsuario(Long id);

    /**
     * Obtiene estadísticas de usuarios
     */
    EstadisticasUsuarioDTO obtenerEstadisticas();

    /**
     * Valida si un email está disponible
     */
    boolean emailDisponible(String email);

    /**
     * Obtiene el perfil del usuario actual
     */
    UsuarioResponseDTO obtenerPerfilActual();

    /**
     * Actualiza el perfil del usuario actual
     */
    UsuarioResponseDTO actualizarPerfilActual(UsuarioRequestDTO usuarioRequest);
}
