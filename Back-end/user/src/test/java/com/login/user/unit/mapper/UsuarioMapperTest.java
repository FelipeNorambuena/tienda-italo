package com.login.user.unit.mapper;

import com.login.user.application.dto.UsuarioRequestDTO;
import com.login.user.application.dto.UsuarioResponseDTO;
import com.login.user.domain.entity.DireccionUsuario;
import com.login.user.domain.entity.Rol;
import com.login.user.domain.entity.Usuario;
import com.login.user.web.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios para UsuarioMapper.
 * Verifican las conversiones entre entidades y DTOs.
 * 
 * @author Tienda Italo Team
 */
@DisplayName("Tests de Usuario Mapper")
class UsuarioMapperTest {

    private UsuarioMapper usuarioMapper;
    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;
    private Rol rolCliente;

    @BeforeEach
    void setUp() {
        usuarioMapper = Mappers.getMapper(UsuarioMapper.class);

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
                .telefono("+57 300 123 4567")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .activo(true)
                .emailVerificado(true)
                .intentosFallidosLogin(0)
                .bloqueadoHasta(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .roles(Set.of(rolCliente))
                .build();

        usuarioRequestDTO = UsuarioRequestDTO.builder()
                .email("test@example.com")
                .nombre("Test")
                .apellido("User")
                .password("Test123!")
                .telefono("+57 300 123 4567")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .activo(true)
                .emailVerificado(false)
                .build();
    }

    @Test
    @DisplayName("Debería convertir UsuarioRequestDTO a entidad Usuario")
    void deberiaConvertirRequestDTOAEntidad() {
        // When
        Usuario resultado = usuarioMapper.toEntity(usuarioRequestDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo("test@example.com");
        assertThat(resultado.getNombre()).isEqualTo("Test");
        assertThat(resultado.getApellido()).isEqualTo("User");
        assertThat(resultado.getTelefono()).isEqualTo("+57 300 123 4567");
        assertThat(resultado.getFechaNacimiento()).isEqualTo(LocalDate.of(1990, 5, 15));
        assertThat(resultado.getActivo()).isTrue();

        // Campos que deben ser ignorados
        assertThat(resultado.getId()).isNull();
        assertThat(resultado.getPasswordHash()).isNull();
        assertThat(resultado.getRoles()).isNull();
        assertThat(resultado.getCreatedAt()).isNull();
        assertThat(resultado.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Debería convertir entidad Usuario a UsuarioResponseDTO")
    void deberiaConvertirEntidadAResponseDTO() {
        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEmail()).isEqualTo("test@example.com");
        assertThat(resultado.getNombre()).isEqualTo("Test");
        assertThat(resultado.getApellido()).isEqualTo("User");
        assertThat(resultado.getNombreCompleto()).isEqualTo("Test User");
        assertThat(resultado.getTelefono()).isEqualTo("+57 300 123 4567");
        assertThat(resultado.getFechaNacimiento()).isEqualTo(LocalDate.of(1990, 5, 15));
        assertThat(resultado.getActivo()).isTrue();
        assertThat(resultado.getEmailVerificado()).isTrue();
        assertThat(resultado.getIntentosFallidosLogin()).isEqualTo(0);
        assertThat(resultado.getBloqueadoHasta()).isNull();
        assertThat(resultado.getCreatedAt()).isNotNull();
        assertThat(resultado.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Debería calcular campos derivados correctamente")
    void deberiaCalcularCamposDerivadosCorrectamente() {
        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getNombreCompleto()).isEqualTo("Test User");
        assertThat(resultado.getEstaBloqueado()).isFalse();
        assertThat(resultado.getCuentaHabilitada()).isTrue();
        assertThat(resultado.getEdad()).isEqualTo(LocalDate.now().getYear() - 1990);
    }

    @Test
    @DisplayName("Debería calcular usuario bloqueado correctamente")
    void deberiaCalcularUsuarioBloqueadoCorrectamente() {
        // Given
        usuario.setBloqueadoHasta(LocalDateTime.now().plusHours(1));

        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getEstaBloqueado()).isTrue();
        assertThat(resultado.getCuentaHabilitada()).isFalse();
    }

    @Test
    @DisplayName("Debería calcular cuenta deshabilitada correctamente")
    void deberiaCalcularCuentaDeshabilitadaCorrectamente() {
        // Given - usuario inactivo
        usuario.setActivo(false);

        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getCuentaHabilitada()).isFalse();

        // Given - email no verificado
        usuario.setActivo(true);
        usuario.setEmailVerificado(false);

        // When
        resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getCuentaHabilitada()).isFalse();
    }

    @Test
    @DisplayName("Debería manejar fecha de nacimiento nula")
    void deberiaManejarFechaNacimientoNula() {
        // Given
        usuario.setFechaNacimiento(null);

        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getFechaNacimiento()).isNull();
        assertThat(resultado.getEdad()).isNull();
    }

    @Test
    @DisplayName("Debería manejar nombre o apellido nulos")
    void deberiaManejarNombreOApellidoNulos() {
        // Given
        usuario.setNombre(null);

        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getNombreCompleto()).isNull();

        // Given
        usuario.setNombre("Test");
        usuario.setApellido(null);

        // When
        resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getNombreCompleto()).isNull();
    }

    @Test
    @DisplayName("Debería convertir lista de usuarios")
    void deberiaConvertirListaDeUsuarios() {
        // Given
        Usuario usuario2 = Usuario.builder()
                .id(2L)
                .email("test2@example.com")
                .nombre("Test2")
                .apellido("User2")
                .passwordHash("hashedPassword2")
                .activo(true)
                .emailVerificado(true)
                .roles(Set.of(rolCliente))
                .build();

        List<Usuario> usuarios = List.of(usuario, usuario2);

        // When
        List<UsuarioResponseDTO> resultado = usuarioMapper.toResponseDTOList(usuarios);

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
        assertThat(resultado.get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Debería actualizar entidad desde DTO")
    void deberiaActualizarEntidadDesdeDTO() {
        // Given
        UsuarioRequestDTO updateDTO = UsuarioRequestDTO.builder()
                .email("updated@example.com")
                .nombre("Updated")
                .apellido("Name")
                .telefono("+57 300 999 8888")
                .fechaNacimiento(LocalDate.of(1985, 10, 20))
                .build();

        // When
        usuarioMapper.updateEntityFromDTO(updateDTO, usuario);

        // Then
        assertThat(usuario.getEmail()).isEqualTo("updated@example.com");
        assertThat(usuario.getNombre()).isEqualTo("Updated");
        assertThat(usuario.getApellido()).isEqualTo("Name");
        assertThat(usuario.getTelefono()).isEqualTo("+57 300 999 8888");
        assertThat(usuario.getFechaNacimiento()).isEqualTo(LocalDate.of(1985, 10, 20));

        // Campos que no deben cambiar
        assertThat(usuario.getId()).isEqualTo(1L);
        assertThat(usuario.getPasswordHash()).isEqualTo("hashedPassword");
        assertThat(usuario.getRoles()).isEqualTo(Set.of(rolCliente));
    }

    @Test
    @DisplayName("Debería mapear roles correctamente")
    void deberíaMapearRolesCorrectamente() {
        // Given
        Rol rolAdmin = Rol.builder()
                .id(2L)
                .nombre("ADMIN")
                .descripcion("Administrador")
                .activo(true)
                .build();

        usuario.setRoles(Set.of(rolCliente, rolAdmin));

        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getRoles()).hasSize(2);
        assertThat(resultado.getRoles()).extracting("nombre")
                .containsExactlyInAnyOrder("CLIENTE", "ADMIN");
    }

    @Test
    @DisplayName("Debería mapear direcciones correctamente")
    void deberíaMapearDireccionesCorrectamente() {
        // Given
        DireccionUsuario direccion = DireccionUsuario.builder()
                .id(1L)
                .usuario(usuario)
                .tipoDireccion(DireccionUsuario.TipoDireccion.ENVIO)
                .nombreContacto("Test User")
                .calle("Carrera 15")
                .numero("23-45")
                .ciudad("Bogotá")
                .estadoProvincia("Cundinamarca")
                .codigoPostal("110221")
                .pais("Colombia")
                .esPrincipal(true)
                .activo(true)
                .build();

        usuario.setDirecciones(List.of(direccion));

        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuario);

        // Then
        assertThat(resultado.getDirecciones()).hasSize(1);
        assertThat(resultado.getDirecciones().get(0).getId()).isEqualTo(1L);
        assertThat(resultado.getDirecciones().get(0).getNombreContacto()).isEqualTo("Test User");
        assertThat(resultado.getDirecciones().get(0).getDireccionCompleta()).isNotNull();
    }

    @Test
    @DisplayName("Debería manejar entidad vacía")
    void deberiaManejarEntidadVacia() {
        // Given
        Usuario usuarioVacio = new Usuario();

        // When
        UsuarioResponseDTO resultado = usuarioMapper.toResponseDTO(usuarioVacio);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNull();
        assertThat(resultado.getEmail()).isNull();
        assertThat(resultado.getNombreCompleto()).isNull();
        assertThat(resultado.getEdad()).isNull();
    }

    @Test
    @DisplayName("Debería manejar DTO vacío")
    void deberiaManejarDTOVacio() {
        // Given
        UsuarioRequestDTO dtoVacio = new UsuarioRequestDTO();

        // When
        Usuario resultado = usuarioMapper.toEntity(dtoVacio);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isNull();
        assertThat(resultado.getNombre()).isNull();
        assertThat(resultado.getApellido()).isNull();
    }
}
