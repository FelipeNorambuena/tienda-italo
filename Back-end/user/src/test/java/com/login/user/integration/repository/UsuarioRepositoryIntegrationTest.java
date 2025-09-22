package com.login.user.integration.repository;

import com.login.user.domain.entity.Rol;
import com.login.user.domain.entity.Usuario;
import com.login.user.infrastructure.repository.UsuarioRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de integración para UsuarioRepository.
 * Usa base de datos H2 en memoria para tests rápidos.
 * 
 * @author Tienda Italo Team
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Tests de Integración - Usuario Repository")
class UsuarioRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepositoryImpl usuarioRepository;

    private Rol rolCliente;
    private Rol rolAdmin;
    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        // Crear roles
        rolCliente = Rol.builder()
                .nombre("CLIENTE")
                .descripcion("Cliente registrado")
                .activo(true)
                .build();
        rolCliente = entityManager.persistAndFlush(rolCliente);

        rolAdmin = Rol.builder()
                .nombre("ADMIN")
                .descripcion("Administrador")
                .activo(true)
                .build();
        rolAdmin = entityManager.persistAndFlush(rolAdmin);

        // Crear usuarios
        usuario1 = Usuario.builder()
                .email("user1@example.com")
                .nombre("User")
                .apellido("One")
                .passwordHash("hashedPassword1")
                .activo(true)
                .emailVerificado(true)
                .intentosFallidosLogin(0)
                .roles(Set.of(rolCliente))
                .build();
        usuario1 = entityManager.persistAndFlush(usuario1);

        usuario2 = Usuario.builder()
                .email("user2@example.com")
                .nombre("User")
                .apellido("Two")
                .passwordHash("hashedPassword2")
                .activo(false)
                .emailVerificado(false)
                .intentosFallidosLogin(2)
                .roles(Set.of(rolAdmin))
                .build();
        usuario2 = entityManager.persistAndFlush(usuario2);

        entityManager.clear();
    }

    @Test
    @DisplayName("Debería encontrar usuario por email")
    void deberiaEncontrarUsuarioPorEmail() {
        // When
        Optional<Usuario> resultado = usuarioRepository.findByEmail("user1@example.com");

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("user1@example.com");
        assertThat(resultado.get().getNombre()).isEqualTo("User");
        assertThat(resultado.get().getApellido()).isEqualTo("One");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando email no existe")
    void deberiaRetornarVacioCuandoEmailNoExiste() {
        // When
        Optional<Usuario> resultado = usuarioRepository.findByEmail("noexiste@example.com");

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería verificar si email existe")
    void deberiaVerificarSiEmailExiste() {
        // When & Then
        assertThat(usuarioRepository.existsByEmail("user1@example.com")).isTrue();
        assertThat(usuarioRepository.existsByEmail("noexiste@example.com")).isFalse();
    }

    @Test
    @DisplayName("Debería encontrar usuarios activos")
    void deberiaEncontrarUsuariosActivos() {
        // When
        Page<Usuario> resultado = usuarioRepository.findByActivoTrue(PageRequest.of(0, 10));

        // Then
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(resultado.getContent().get(0).getActivo()).isTrue();
    }

    @Test
    @DisplayName("Debería buscar usuarios por nombre")
    void deberiaBuscarUsuariosPorNombre() {
        // When
        Page<Usuario> resultado = usuarioRepository
                .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                        "user", "user", PageRequest.of(0, 10));

        // Then
        assertThat(resultado.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Debería buscar usuarios por apellido")
    void deberiaBuscarUsuariosPorApellido() {
        // When
        Page<Usuario> resultado = usuarioRepository
                .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                        "One", "One", PageRequest.of(0, 10));

        // Then
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getApellido()).isEqualTo("One");
    }

    @Test
    @DisplayName("Debería encontrar usuarios no verificados")
    void deberiaEncontrarUsuariosNoVerificados() {
        // When
        List<Usuario> resultado = usuarioRepository.findByEmailVerificadoFalse();

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("user2@example.com");
        assertThat(resultado.get(0).getEmailVerificado()).isFalse();
    }

    @Test
    @DisplayName("Debería encontrar usuarios por rol")
    void deberiaEncontrarUsuariosPorRol() {
        // When
        Page<Usuario> clientesResult = usuarioRepository.findByRoles_Nombre("CLIENTE", PageRequest.of(0, 10));
        Page<Usuario> adminsResult = usuarioRepository.findByRoles_Nombre("ADMIN", PageRequest.of(0, 10));

        // Then
        assertThat(clientesResult.getContent()).hasSize(1);
        assertThat(clientesResult.getContent().get(0).getEmail()).isEqualTo("user1@example.com");

        assertThat(adminsResult.getContent()).hasSize(1);
        assertThat(adminsResult.getContent().get(0).getEmail()).isEqualTo("user2@example.com");
    }

    @Test
    @DisplayName("Debería encontrar usuarios con intentos fallidos")
    void deberiaEncontrarUsuariosConIntentosFallidos() {
        // When
        List<Usuario> resultado = usuarioRepository.findByIntentosFallidosLoginGreaterThanEqual(1);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("user2@example.com");
        assertThat(resultado.get(0).getIntentosFallidosLogin()).isEqualTo(2);
    }

    @Test
    @DisplayName("Debería encontrar usuarios bloqueados")
    void deberiaEncontrarUsuariosBloqueados() {
        // Given - agregar usuario bloqueado
        Usuario usuarioBloqueado = Usuario.builder()
                .email("bloqueado@example.com")
                .nombre("Blocked")
                .apellido("User")
                .passwordHash("hashedPassword")
                .activo(true)
                .emailVerificado(true)
                .intentosFallidosLogin(5)
                .bloqueadoHasta(LocalDateTime.now().plusHours(1))
                .roles(Set.of(rolCliente))
                .build();
        entityManager.persistAndFlush(usuarioBloqueado);

        // When
        List<Usuario> resultado = usuarioRepository.findByBloqueadoHastaAfter(LocalDateTime.now());

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("bloqueado@example.com");
    }

    @Test
    @DisplayName("Debería contar usuarios activos")
    void deberiaContarUsuariosActivos() {
        // When
        long count = usuarioRepository.countByActivoTrue();

        // Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debería contar usuarios por rol")
    void deberiaContarUsuariosPorRol() {
        // When
        long countClientes = usuarioRepository.countByRoles_Nombre("CLIENTE");
        long countAdmins = usuarioRepository.countByRoles_Nombre("ADMIN");

        // Then
        assertThat(countClientes).isEqualTo(1);
        assertThat(countAdmins).isEqualTo(1);
    }

    @Test
    @DisplayName("Debería encontrar usuarios creados después de fecha específica")
    void deberiaEncontrarUsuariosCreadosDespuesDeFecha() {
        // Given
        LocalDateTime fechaCorte = LocalDateTime.now().minusHours(1);

        // When
        List<Usuario> resultado = usuarioRepository.findByCreatedAtAfter(fechaCorte);

        // Then
        assertThat(resultado).hasSize(2); // Ambos usuarios fueron creados recientemente
    }

    @Test
    @DisplayName("Debería guardar y recuperar usuario con relaciones")
    void deberiaGuardarYRecuperarUsuarioConRelaciones() {
        // Given
        Usuario nuevoUsuario = Usuario.builder()
                .email("nuevo@example.com")
                .nombre("Nuevo")
                .apellido("Usuario")
                .passwordHash("hashedPassword")
                .activo(true)
                .emailVerificado(false)
                .intentosFallidosLogin(0)
                .roles(Set.of(rolCliente, rolAdmin))
                .build();

        // When
        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        entityManager.flush();
        entityManager.clear();

        Optional<Usuario> recuperado = usuarioRepository.findById(guardado.getId());

        // Then
        assertThat(recuperado).isPresent();
        assertThat(recuperado.get().getEmail()).isEqualTo("nuevo@example.com");
        assertThat(recuperado.get().getRoles()).hasSize(2);
        assertThat(recuperado.get().getCreatedAt()).isNotNull();
        assertThat(recuperado.get().getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Debería actualizar usuario existente")
    void deberiaActualizarUsuarioExistente() {
        // Given
        Optional<Usuario> usuarioOriginal = usuarioRepository.findByEmail("user1@example.com");
        assertThat(usuarioOriginal).isPresent();

        Usuario usuario = usuarioOriginal.get();
        String nombreOriginal = usuario.getNombre();
        LocalDateTime updatedAtOriginal = usuario.getUpdatedAt();

        // When
        usuario.setNombre("Nombre Actualizado");
        Usuario actualizado = usuarioRepository.save(usuario);
        entityManager.flush();

        // Then
        assertThat(actualizado.getNombre()).isEqualTo("Nombre Actualizado");
        assertThat(actualizado.getNombre()).isNotEqualTo(nombreOriginal);
        assertThat(actualizado.getUpdatedAt()).isAfter(updatedAtOriginal);
    }

    @Test
    @DisplayName("Debería eliminar usuario por ID")
    void deberiaEliminarUsuarioPorId() {
        // Given
        Long userId = usuario1.getId();
        assertThat(usuarioRepository.existsById(userId)).isTrue();

        // When
        usuarioRepository.deleteById(userId);
        entityManager.flush();

        // Then
        assertThat(usuarioRepository.existsById(userId)).isFalse();
        assertThat(usuarioRepository.findById(userId)).isEmpty();
    }

    @Test
    @DisplayName("Debería manejar transacciones correctamente")
    void deberiaManejarTransaccionesCorrectamente() {
        // Given
        long countInicial = usuarioRepository.count();

        Usuario nuevoUsuario = Usuario.builder()
                .email("transaccion@example.com")
                .nombre("Trans")
                .apellido("Action")
                .passwordHash("hashedPassword")
                .activo(true)
                .emailVerificado(true)
                .roles(Set.of(rolCliente))
                .build();

        // When
        usuarioRepository.save(nuevoUsuario);
        long countDespuesGuardar = usuarioRepository.count();

        // Then
        assertThat(countDespuesGuardar).isEqualTo(countInicial + 1);
    }

    @Test
    @DisplayName("Debería manejar consultas con paginación")
    void deberiaManejarConsultasConPaginacion() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 1);

        // When
        Page<Usuario> firstPage = usuarioRepository.findAll(pageRequest);

        // Then
        assertThat(firstPage.getContent()).hasSize(1);
        assertThat(firstPage.getTotalElements()).isEqualTo(2);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
        assertThat(firstPage.hasNext()).isTrue();
        assertThat(firstPage.hasPrevious()).isFalse();

        // When - segunda página
        PageRequest secondPageRequest = PageRequest.of(1, 1);
        Page<Usuario> secondPage = usuarioRepository.findAll(secondPageRequest);

        // Then
        assertThat(secondPage.getContent()).hasSize(1);
        assertThat(secondPage.hasNext()).isFalse();
        assertThat(secondPage.hasPrevious()).isTrue();
    }
}
