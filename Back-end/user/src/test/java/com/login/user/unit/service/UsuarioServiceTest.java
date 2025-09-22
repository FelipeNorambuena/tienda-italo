package com.login.user.unit.service;

import com.login.user.application.dto.*;
import com.login.user.application.service.impl.UsuarioServiceImpl;
import com.login.user.domain.entity.Rol;
import com.login.user.domain.entity.TokenRecuperacion;
import com.login.user.domain.entity.Usuario;
import com.login.user.domain.repository.RolRepository;
import com.login.user.domain.repository.TokenRecuperacionRepository;
import com.login.user.domain.repository.UsuarioRepository;
import com.login.user.infrastructure.security.JwtUtil;
import com.login.user.web.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UsuarioService.
 * Verifican la lógica de negocio del servicio sin dependencias externas.
 * 
 * @author Tienda Italo Team
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de Usuario Service")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private TokenRecuperacionRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioRequestDTO usuarioRequestDTO;
    private Usuario usuario;
    private Rol rolCliente;
    private UsuarioResponseDTO usuarioResponseDTO;

    @BeforeEach
    void setUp() {
        usuarioRequestDTO = UsuarioRequestDTO.builder()
                .email("test@example.com")
                .nombre("Test")
                .apellido("User")
                .password("Test123!")
                .telefono("+57 300 123 4567")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .build();

        rolCliente = Rol.builder()
                .id(1L)
                .nombre("CLIENTE")
                .descripcion("Cliente registrado")
                .activo(true)
                .build();

        usuario = Usuario.builder()
                .id(1L)
                .email("test@example.com")
                .nombre("Test")
                .apellido("User")
                .passwordHash("hashedPassword")
                .activo(true)
                .emailVerificado(false)
                .intentosFallidosLogin(0)
                .roles(Set.of(rolCliente))
                .createdAt(LocalDateTime.now())
                .build();

        usuarioResponseDTO = UsuarioResponseDTO.builder()
                .id(1L)
                .email("test@example.com")
                .nombre("Test")
                .apellido("User")
                .nombreCompleto("Test User")
                .activo(true)
                .emailVerificado(false)
                .build();
    }

    @Test
    @DisplayName("Debería registrar usuario exitosamente")
    void deberiaRegistrarUsuarioExitosamente() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioMapper.toEntity(any(UsuarioRequestDTO.class))).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(rolRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);
        when(tokenRepository.save(any(TokenRecuperacion.class))).thenReturn(new TokenRecuperacion());

        // When
        UsuarioResponseDTO resultado = usuarioService.registrarUsuario(usuarioRequestDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo("test@example.com");
        
        verify(usuarioRepository).existsByEmail("test@example.com");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(tokenRepository).save(any(TokenRecuperacion.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando email ya existe")
    void deberiaLanzarExcepcionCuandoEmailYaExiste() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> usuarioService.registrarUsuario(usuarioRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El email ya está registrado");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería autenticar usuario exitosamente")
    void deberiaAutenticarUsuarioExitosamente() {
        // Given
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("test@example.com")
                .password("Test123!")
                .build();

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateAccessToken(any(UserDetails.class))).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any(UserDetails.class))).thenReturn("refresh-token");
        when(jwtUtil.getAccessTokenExpiration()).thenReturn(86400000L);
        when(jwtUtil.getExpirationAsLocalDateTime(anyString())).thenReturn(LocalDateTime.now().plusDays(1));
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        LoginResponseDTO resultado = usuarioService.autenticarUsuario(loginRequest);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getAccessToken()).isEqualTo("access-token");
        assertThat(resultado.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(resultado.getTokenType()).isEqualTo("Bearer");

        verify(usuarioRepository).save(any(Usuario.class)); // Para actualizar último acceso
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando usuario está bloqueado")
    void deberiaLanzarExcepcionCuandoUsuarioEstaBloqueado() {
        // Given
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("test@example.com")
                .password("Test123!")
                .build();

        usuario.setBloqueadoHasta(LocalDateTime.now().plusHours(1));
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // When & Then
        assertThatThrownBy(() -> usuarioService.autenticarUsuario(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Usuario bloqueado temporalmente");

        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    @DisplayName("Debería incrementar intentos fallidos en login incorrecto")
    void deberiaIncrementarIntentosFallidosEnLoginIncorrecto() {
        // Given
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("test@example.com")
                .password("WrongPassword")
                .build();

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // When & Then
        assertThatThrownBy(() -> usuarioService.autenticarUsuario(loginRequest))
                .isInstanceOf(BadCredentialsException.class);

        verify(usuarioRepository, times(2)).save(any(Usuario.class)); // Una vez para incrementar intentos
    }

    @Test
    @DisplayName("Debería buscar usuario por ID exitosamente")
    void deberiaBuscarUsuarioPorIdExitosamente() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);

        // When
        Optional<UsuarioResponseDTO> resultado = usuarioService.buscarUsuarioPorId(1L);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Debería retornar vacío cuando usuario no existe")
    void deberiaRetornarVacioCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<UsuarioResponseDTO> resultado = usuarioService.buscarUsuarioPorId(999L);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería cambiar contraseña exitosamente")
    void deberiaCambiarPasswordExitosamente() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("currentPassword", "hashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        usuarioService.cambiarPassword(1L, "currentPassword", "newPassword");

        // Then
        verify(passwordEncoder).matches("currentPassword", "hashedPassword");
        verify(passwordEncoder).encode("newPassword");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción con contraseña actual incorrecta")
    void deberiaLanzarExcepcionConPasswordActualIncorrecta() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> usuarioService.cambiarPassword(1L, "wrongPassword", "newPassword"))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Contraseña actual incorrecta");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería obtener usuarios con paginación")
    void deberiaObtenerUsuariosConPaginacion() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> usuariosPage = new PageImpl<>(List.of(usuario), pageable, 1);
        Page<UsuarioResponseDTO> expectedPage = new PageImpl<>(List.of(usuarioResponseDTO), pageable, 1);

        when(usuarioRepository.findAll(pageable)).thenReturn(usuariosPage);
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);

        // When
        Page<UsuarioResponseDTO> resultado = usuarioService.obtenerUsuarios(pageable);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Debería activar usuario correctamente")
    void deberiaActivarUsuarioCorrectamente() {
        // Given
        usuario.setActivo(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);

        // When
        UsuarioResponseDTO resultado = usuarioService.cambiarEstadoUsuario(1L, true);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).save(argThat(u -> u.getActivo().equals(true)));
    }

    @Test
    @DisplayName("Debería verificar email exitosamente")
    void deberiaVerificarEmailExitosamente() {
        // Given
        String token = "verification-token";
        TokenRecuperacion tokenVerificacion = TokenRecuperacion.builder()
                .token(token)
                .tipoToken(TokenRecuperacion.TipoToken.EMAIL_VERIFICATION)
                .usado(false)
                .fechaExpiracion(LocalDateTime.now().plusDays(1))
                .usuario(usuario)
                .build();

        when(tokenRepository.findByTokenAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
                eq(token), eq(TokenRecuperacion.TipoToken.EMAIL_VERIFICATION), any(LocalDateTime.class)))
                .thenReturn(Optional.of(tokenVerificacion));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(tokenRepository.save(any(TokenRecuperacion.class))).thenReturn(tokenVerificacion);

        // When
        usuarioService.verificarEmail(token);

        // Then
        verify(usuarioRepository).save(argThat(u -> u.getEmailVerificado().equals(true)));
        verify(tokenRepository).save(argThat(t -> t.getUsado().equals(true)));
    }

    @Test
    @DisplayName("Debería lanzar excepción con token de verificación inválido")
    void deberiaLanzarExcepcionConTokenVerificacionInvalido() {
        // Given
        String token = "invalid-token";
        when(tokenRepository.findByTokenAndTipoTokenAndUsadoFalseAndFechaExpiracionAfter(
                eq(token), eq(TokenRecuperacion.TipoToken.EMAIL_VERIFICATION), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.verificarEmail(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token de verificación inválido o expirado");
    }

    @Test
    @DisplayName("Debería verificar disponibilidad de email")
    void deberiaVerificarDisponibilidadDeEmail() {
        // Given
        when(usuarioRepository.existsByEmail("nuevo@example.com")).thenReturn(false);
        when(usuarioRepository.existsByEmail("existente@example.com")).thenReturn(true);

        // When & Then
        assertThat(usuarioService.emailDisponible("nuevo@example.com")).isTrue();
        assertThat(usuarioService.emailDisponible("existente@example.com")).isFalse();
    }

    @Test
    @DisplayName("Debería normalizar email en verificación de disponibilidad")
    void deberiaNormalizarEmailEnVerificacionDisponibilidad() {
        // Given
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(false);

        // When
        boolean disponible = usuarioService.emailDisponible("  TEST@EXAMPLE.COM  ");

        // Then
        assertThat(disponible).isTrue();
        verify(usuarioRepository).existsByEmail("test@example.com");
    }

    @Test
    @DisplayName("Debería obtener estadísticas de usuarios")
    void deberiaObtenerEstadisticasDeUsuarios() {
        // Given
        when(usuarioRepository.count()).thenReturn(100L);
        when(usuarioRepository.countByActivoTrue()).thenReturn(90L);
        when(usuarioRepository.countByRoles_Nombre("ADMIN")).thenReturn(5L);
        when(usuarioRepository.countByRoles_Nombre("CLIENTE")).thenReturn(85L);
        when(usuarioRepository.countByRoles_Nombre("GESTOR")).thenReturn(3L);
        when(usuarioRepository.countByRoles_Nombre("VENDEDOR")).thenReturn(7L);
        when(usuarioRepository.findByCreatedAtAfter(any(LocalDateTime.class))).thenReturn(List.of());
        when(usuarioRepository.findUsuariosActivosRecientes(any(LocalDateTime.class))).thenReturn(List.of());

        // When
        EstadisticasUsuarioDTO estadisticas = usuarioService.obtenerEstadisticas();

        // Then
        assertThat(estadisticas).isNotNull();
        assertThat(estadisticas.getTotalUsuarios()).isEqualTo(100L);
        assertThat(estadisticas.getUsuariosActivos()).isEqualTo(90L);
        assertThat(estadisticas.getUsuariosInactivos()).isEqualTo(10L);
        assertThat(estadisticas.getPorcentajeActivos()).isEqualTo(90.0);
    }
}
