package com.login.user.application.service.impl;

import com.login.user.application.dto.*;
import com.login.user.application.service.UsuarioService;
import com.login.user.domain.entity.Rol;
import com.login.user.domain.entity.TokenRecuperacion;
import com.login.user.domain.entity.Usuario;
import com.login.user.domain.repository.RolRepository;
import com.login.user.domain.repository.TokenRecuperacionRepository;
import com.login.user.domain.repository.UsuarioRepository;
import com.login.user.infrastructure.security.JwtUtil;
import com.login.user.web.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de usuarios.
 * Contiene la lógica de negocio para la gestión de usuarios.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TokenRecuperacionRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO usuarioRequest) {
        log.info("Registrando nuevo usuario con email: {}", usuarioRequest.getEmail());
        
        // Normalizar datos de entrada
        usuarioRequest.normalizarDatos();
        
        // Validar que el email no esté en uso
        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // Crear entidad Usuario
        Usuario usuario = usuarioMapper.toEntity(usuarioRequest);
        usuario.setPasswordHash(passwordEncoder.encode(usuarioRequest.getPassword()));
        
        // Asignar rol por defecto (CLIENTE)
        Rol rolCliente = rolRepository.findByNombre(Rol.CLIENTE)
                .orElseThrow(() -> new IllegalStateException("Rol CLIENTE no encontrado"));
        usuario.setRoles(Set.of(rolCliente));
        
        // Guardar usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Generar token de verificación de email
        TokenRecuperacion tokenVerificacion = TokenRecuperacion.crearTokenVerificacionEmail(usuarioGuardado);
        tokenRepository.save(tokenVerificacion);
        
        log.info("Usuario registrado exitosamente: {}", usuarioGuardado.getId());
        
        // TODO: Enviar email de verificación
        
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    @Override
    public LoginResponseDTO autenticarUsuario(LoginRequestDTO loginRequest) {
        log.info("Intentando autenticar usuario: {}", loginRequest.getEmail());
        
        // Normalizar datos
        loginRequest.normalizarDatos();
        
        try {
            // Buscar usuario por email
            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
            
            // Verificar si el usuario está bloqueado
            if (usuario.estaBloqueado()) {
                throw new BadCredentialsException("Usuario bloqueado temporalmente");
            }
            
            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            
            // Generar tokens
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            
            // Actualizar información de último acceso y resetear intentos fallidos
            usuario.actualizarUltimoAcceso();
            usuario.reiniciarIntentosFallidos();
            usuarioRepository.save(usuario);
            
            // Construir respuesta
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.getAccessTokenExpiration() / 1000)
                    .expiresAt(jwtUtil.getExpirationAsLocalDateTime(accessToken))
                    .usuario(usuarioMapper.toResponseDTO(usuario))
                    .roles(usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toList()))
                    .primerLogin(usuario.getFechaUltimoAcceso() == null)
                    .build();
            
            response.construirMensajeBienvenida();
            
            log.info("Usuario autenticado exitosamente: {}", usuario.getId());
            return response;
            
        } catch (BadCredentialsException e) {
            // Incrementar intentos fallidos
            usuarioRepository.findByEmail(loginRequest.getEmail())
                    .ifPresent(usuario -> {
                        usuario.incrementarIntentosFallidos();
                        usuarioRepository.save(usuario);
                    });
            
            log.warn("Intento de autenticación fallido para: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Credenciales inválidas");
        }
    }

    @Override
    public LoginResponseDTO renovarToken(String refreshToken) {
        log.info("Renovando token de acceso");
        
        if (!jwtUtil.isValidToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token inválido");
        }
        
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        if (!usuario.isEnabled()) {
            throw new IllegalArgumentException("Usuario no habilitado");
        }
        
        // Generar nuevo access token
        String newAccessToken = jwtUtil.generateAccessToken(usuario);
        
        LoginResponseDTO response = LoginResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // Mantener el mismo refresh token
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenExpiration() / 1000)
                .expiresAt(jwtUtil.getExpirationAsLocalDateTime(newAccessToken))
                .usuario(usuarioMapper.toResponseDTO(usuario))
                .roles(usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toList()))
                .build();
        
        log.info("Token renovado exitosamente para usuario: {}", usuario.getId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email.toLowerCase().trim())
                .map(usuarioMapper::toResponseDTO);
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO usuarioRequest) {
        log.info("Actualizando usuario: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Normalizar datos
        usuarioRequest.normalizarDatos();
        
        // Verificar email único si se está cambiando
        if (!usuario.getEmail().equals(usuarioRequest.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        
        // Actualizar campos
        usuarioMapper.updateEntityFromDTO(usuarioRequest, usuario);
        
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        log.info("Usuario actualizado exitosamente: {}", id);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    
    public void cambiarPassword(Long id, String passwordActual, String passwordNueva) {
        log.info("Cambiando contraseña para usuario: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getPasswordHash())) {
            throw new BadCredentialsException("Contraseña actual incorrecta");
        }
        
        // Actualizar contraseña
        usuario.setPasswordHash(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
        
        log.info("Contraseña cambiada exitosamente para usuario: {}", id);
    }

    @Override
    public UsuarioResponseDTO cambiarEstadoUsuario(Long id, Boolean activo) {
        log.info("Cambiando estado de usuario {} a: {}", id, activo);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.setActivo(activo);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        log.info("Estado de usuario cambiado exitosamente: {}", id);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    public void verificarEmail(String token) {
        log.info("Verificando email con token");
        
        TokenRecuperacion tokenVerificacion = tokenRepository
                .findByTokenAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
                        token, TokenRecuperacion.TipoToken.EMAIL_VERIFICATION, LocalDateTime.now())
                .orElseThrow(() -> new IllegalArgumentException("Token de verificación inválido o expirado"));
        
        Usuario usuario = tokenVerificacion.getUsuario();
        usuario.setEmailVerificado(true);
        tokenVerificacion.marcarComoUsado();
        
        usuarioRepository.save(usuario);
        tokenRepository.save(tokenVerificacion);
        
        log.info("Email verificado exitosamente para usuario: {}", usuario.getId());
    }

    @Override
    public void solicitarRecuperacionPassword(String email) {
        log.info("Solicitando recuperación de contraseña para: {}", email);
        
        Usuario usuario = usuarioRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Invalidar tokens anteriores de recuperación
        List<TokenRecuperacion> tokensAnteriores = tokenRepository
                .findByUsuarioAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
                        usuario, TokenRecuperacion.TipoToken.PASSWORD_RESET, LocalDateTime.now());

        if (!tokensAnteriores.isEmpty()) {
            tokensAnteriores.forEach(TokenRecuperacion::marcarComoUsado);
            tokensAnteriores.forEach(tokenRepository::save);
        }

        // Crear nuevo token
        TokenRecuperacion nuevoToken = TokenRecuperacion.crearTokenRecuperacionPassword(usuario);
        tokenRepository.save(nuevoToken);
        
        log.info("Token de recuperación generado para usuario: {}", usuario.getId());
        
        // TODO: Enviar email con el token
    }

    @Override
    public void restablecerPassword(String token, String nuevaPassword) {
        log.info("Restableciendo contraseña con token");
        
        TokenRecuperacion tokenRecuperacion = tokenRepository
                .findByTokenAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
                        token, TokenRecuperacion.TipoToken.PASSWORD_RESET, LocalDateTime.now())
                .orElseThrow(() -> new IllegalArgumentException("Token de recuperación inválido o expirado"));
        
        Usuario usuario = tokenRecuperacion.getUsuario();
        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuario.reiniciarIntentosFallidos(); // Desbloquear si estaba bloqueado
        tokenRecuperacion.marcarComoUsado();
        
        usuarioRepository.save(usuario);
        tokenRepository.save(tokenRecuperacion);
        
        log.info("Contraseña restablecida exitosamente para usuario: {}", usuario.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> obtenerUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> buscarUsuariosPorNombre(String termino, Pageable pageable) {
        return usuarioRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                termino, termino, pageable)
                .map(usuarioMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> buscarUsuariosPorRol(String nombreRol, Pageable pageable) {
        return usuarioRepository.findByRoles_Nombre(nombreRol, pageable)
                .map(usuarioMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> obtenerUsuariosActivos(Pageable pageable) {
        return usuarioRepository.findByActivoTrue(pageable)
                .map(usuarioMapper::toResponseDTO);
    }

    @Override
    public void eliminarUsuario(Long id) {
        log.info("Eliminando (desactivando) usuario: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Soft delete - solo desactivar
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        log.info("Usuario desactivado exitosamente: {}", id);
    }

    @Override
    public UsuarioResponseDTO asignarRol(Long usuarioId, Long rolId) {
        log.info("Asignando rol {} a usuario {}", rolId, usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        
        Set<Rol> roles = new HashSet<>(usuario.getRoles());
        roles.add(rol);
        usuario.setRoles(roles);
        
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        log.info("Rol asignado exitosamente");
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    public UsuarioResponseDTO removerRol(Long usuarioId, Long rolId) {
        log.info("Removiendo rol {} de usuario {}", rolId, usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        
        Set<Rol> roles = new HashSet<>(usuario.getRoles());
        roles.remove(rol);
        
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Un usuario debe tener al menos un rol");
        }
        
        usuario.setRoles(roles);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        log.info("Rol removido exitosamente");
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    public UsuarioResponseDTO desbloquearUsuario(Long id) {
        log.info("Desbloqueando usuario: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.reiniciarIntentosFallidos();
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        log.info("Usuario desbloqueado exitosamente: {}", id);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public EstadisticasUsuarioDTO obtenerEstadisticas() {
        long totalUsuarios = usuarioRepository.count();
        long usuariosActivos = usuarioRepository.countByActivoTrue();
        long usuariosInactivos = totalUsuarios - usuariosActivos;
        
        // Obtener distribución por roles
        Map<String, Long> usuariosPorRol = new HashMap<>();
        usuariosPorRol.put("ADMIN", usuarioRepository.countByRoles_Nombre("ADMIN"));
        usuariosPorRol.put("CLIENTE", usuarioRepository.countByRoles_Nombre("CLIENTE"));
        usuariosPorRol.put("GESTOR", usuarioRepository.countByRoles_Nombre("GESTOR"));
        usuariosPorRol.put("VENDEDOR", usuarioRepository.countByRoles_Nombre("VENDEDOR"));
        
        // Estadísticas adicionales
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        long nuevosRegistros = usuarioRepository.findByCreatedAtAfter(hace30Dias).size();
        long usuariosActivosRecientes = usuarioRepository.findUsuariosActivosRecientes(hace30Dias).size();
        
        EstadisticasUsuarioDTO estadisticas = EstadisticasUsuarioDTO.builder()
                .totalUsuarios(totalUsuarios)
                .usuariosActivos(usuariosActivos)
                .usuariosInactivos(usuariosInactivos)
                .usuariosPorRol(usuariosPorRol)
                .nuevosRegistrosUltimoMes(nuevosRegistros)
                .usuariosActivosUltimos30Dias(usuariosActivosRecientes)
                .build();
        
        estadisticas.calcularPorcentajes();
        
        return estadisticas;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailDisponible(String email) {
        return !usuarioRepository.existsByEmail(email.toLowerCase().trim());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPerfilActual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO actualizarPerfilActual(UsuarioRequestDTO usuarioRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        return actualizarUsuario(usuario.getId(), usuarioRequest);
    }
}
