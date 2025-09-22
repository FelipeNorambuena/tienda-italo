package com.login.user.unit.domain;

import com.login.user.domain.entity.Rol;
import com.login.user.domain.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios para la entidad Usuario.
 * Verifican la lógica de negocio implementada en la entidad.
 * 
 * @author Tienda Italo Team
 */
@DisplayName("Tests de Entidad Usuario")
class UsuarioTest {

    private Usuario usuario;
    private Rol rolCliente;
    private Rol rolAdmin;

    @BeforeEach
    void setUp() {
        rolCliente = Rol.builder()
                .id(1L)
                .nombre("CLIENTE")
                .descripcion("Cliente registrado")
                .activo(true)
                .build();

        rolAdmin = Rol.builder()
                .id(2L)
                .nombre("ADMIN")
                .descripcion("Administrador")
                .activo(true)
                .build();

        usuario = Usuario.builder()
                .id(1L)
                .email("test@example.com")
                .nombre("Test")
                .apellido("User")
                .passwordHash("hashedPassword")
                .activo(true)
                .emailVerificado(true)
                .intentosFallidosLogin(0)
                .roles(Set.of(rolCliente))
                .build();
    }

    @Test
    @DisplayName("Debería obtener nombre completo correctamente")
    void deberiaObtenerNombreCompleto() {
        // When
        String nombreCompleto = usuario.getNombreCompleto();

        // Then
        assertThat(nombreCompleto).isEqualTo("Test User");
    }

    @Test
    @DisplayName("Debería detectar usuario no bloqueado")
    void deberiaDetectarUsuarioNoBloqueado() {
        // Given
        usuario.setBloqueadoHasta(null);

        // When
        boolean estaBloqueado = usuario.estaBloqueado();

        // Then
        assertThat(estaBloqueado).isFalse();
    }

    @Test
    @DisplayName("Debería detectar usuario bloqueado")
    void deberiaDetectarUsuarioBloqueado() {
        // Given
        usuario.setBloqueadoHasta(LocalDateTime.now().plusHours(1));

        // When
        boolean estaBloqueado = usuario.estaBloqueado();

        // Then
        assertThat(estaBloqueado).isTrue();
    }

    @Test
    @DisplayName("Debería detectar bloqueo expirado")
    void deberiaDetectarBloqueoExpirado() {
        // Given
        usuario.setBloqueadoHasta(LocalDateTime.now().minusHours(1));

        // When
        boolean estaBloqueado = usuario.estaBloqueado();

        // Then
        assertThat(estaBloqueado).isFalse();
    }

    @Test
    @DisplayName("Debería incrementar intentos fallidos")
    void deberiaIncrementarIntentosFallidos() {
        // Given
        assertThat(usuario.getIntentosFallidosLogin()).isEqualTo(0);

        // When
        usuario.incrementarIntentosFallidos();

        // Then
        assertThat(usuario.getIntentosFallidosLogin()).isEqualTo(1);
        assertThat(usuario.getBloqueadoHasta()).isNull();
    }

    @Test
    @DisplayName("Debería bloquear usuario después de 5 intentos fallidos")
    void deberiaBloquerUsuarioDespuesDe5Intentos() {
        // Given
        usuario.setIntentosFallidosLogin(4);

        // When
        usuario.incrementarIntentosFallidos();

        // Then
        assertThat(usuario.getIntentosFallidosLogin()).isEqualTo(5);
        assertThat(usuario.getBloqueadoHasta()).isNotNull();
        assertThat(usuario.getBloqueadoHasta()).isAfter(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debería reiniciar intentos fallidos")
    void deberiaReiniciarIntentosFallidos() {
        // Given
        usuario.setIntentosFallidosLogin(3);
        usuario.setBloqueadoHasta(LocalDateTime.now().plusHours(1));

        // When
        usuario.reiniciarIntentosFallidos();

        // Then
        assertThat(usuario.getIntentosFallidosLogin()).isEqualTo(0);
        assertThat(usuario.getBloqueadoHasta()).isNull();
    }

    @Test
    @DisplayName("Debería actualizar último acceso")
    void deberiaActualizarUltimoAcceso() {
        // Given
        LocalDateTime antes = LocalDateTime.now();
        usuario.setFechaUltimoAcceso(null);

        // When
        usuario.actualizarUltimoAcceso();

        // Then
        assertThat(usuario.getFechaUltimoAcceso()).isNotNull();
        assertThat(usuario.getFechaUltimoAcceso()).isAfter(antes);
    }

    @Test
    @DisplayName("Debería verificar si tiene rol específico")
    void deberiaVerificarSiTieneRolEspecifico() {
        // When & Then
        assertThat(usuario.tieneRol("CLIENTE")).isTrue();
        assertThat(usuario.tieneRol("ADMIN")).isFalse();
        assertThat(usuario.tieneRol("INEXISTENTE")).isFalse();
    }

    @Test
    @DisplayName("Debería verificar múltiples roles")
    void deberiaVerificarMultiplesRoles() {
        // Given
        usuario.setRoles(Set.of(rolCliente, rolAdmin));

        // When & Then
        assertThat(usuario.tieneRol("CLIENTE")).isTrue();
        assertThat(usuario.tieneRol("ADMIN")).isTrue();
        assertThat(usuario.tieneRol("GESTOR")).isFalse();
    }

    @Test
    @DisplayName("Debería implementar UserDetails correctamente - username")
    void deberiaImplementarUserDetailsUsername() {
        // When
        String username = usuario.getUsername();

        // Then
        assertThat(username).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Debería implementar UserDetails correctamente - password")
    void deberiaImplementarUserDetailsPassword() {
        // When
        String password = usuario.getPassword();

        // Then
        assertThat(password).isEqualTo("hashedPassword");
    }

    @Test
    @DisplayName("Debería implementar UserDetails correctamente - authorities")
    void deberiaImplementarUserDetailsAuthorities() {
        // When
        var authorities = usuario.getAuthorities();

        // Then
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_CLIENTE");
    }

    @Test
    @DisplayName("Debería implementar UserDetails - account non expired")
    void deberiaImplementarUserDetailsAccountNonExpired() {
        // When & Then
        assertThat(usuario.isAccountNonExpired()).isTrue();

        // Given usuario inactivo
        usuario.setActivo(false);

        // When & Then
        assertThat(usuario.isAccountNonExpired()).isFalse();
    }

    @Test
    @DisplayName("Debería implementar UserDetails - account non locked")
    void deberiaImplementarUserDetailsAccountNonLocked() {
        // When & Then
        assertThat(usuario.isAccountNonLocked()).isTrue();

        // Given usuario bloqueado
        usuario.setBloqueadoHasta(LocalDateTime.now().plusHours(1));

        // When & Then
        assertThat(usuario.isAccountNonLocked()).isFalse();
    }

    @Test
    @DisplayName("Debería implementar UserDetails - credentials non expired")
    void deberiaImplementarUserDetailsCredentialsNonExpired() {
        // When & Then
        assertThat(usuario.isCredentialsNonExpired()).isTrue();

        // Given usuario inactivo
        usuario.setActivo(false);

        // When & Then
        assertThat(usuario.isCredentialsNonExpired()).isFalse();
    }

    @Test
    @DisplayName("Debería implementar UserDetails - enabled")
    void deberiaImplementarUserDetailsEnabled() {
        // When & Then
        assertThat(usuario.isEnabled()).isTrue();

        // Given email no verificado
        usuario.setEmailVerificado(false);

        // When & Then
        assertThat(usuario.isEnabled()).isFalse();

        // Given usuario inactivo
        usuario.setEmailVerificado(true);
        usuario.setActivo(false);

        // When & Then
        assertThat(usuario.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Debería manejar roles vacíos")
    void deberiaManejarRolesVacios() {
        // Given
        usuario.setRoles(Set.of());

        // When
        var authorities = usuario.getAuthorities();

        // Then
        assertThat(authorities).isEmpty();
        assertThat(usuario.tieneRol("CUALQUIERA")).isFalse();
    }
}
