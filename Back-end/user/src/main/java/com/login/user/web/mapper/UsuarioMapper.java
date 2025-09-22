package com.login.user.web.mapper;

import com.login.user.application.dto.DireccionUsuarioResponseDTO;
import com.login.user.application.dto.RolResponseDTO;
import com.login.user.application.dto.UsuarioRequestDTO;
import com.login.user.application.dto.UsuarioResponseDTO;
import com.login.user.domain.entity.DireccionUsuario;
import com.login.user.domain.entity.Rol;
import com.login.user.domain.entity.Usuario;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

/**
 * Mapper para convertir entre entidades de Usuario y DTOs.
 * Utiliza MapStruct para generar automáticamente las implementaciones.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UsuarioMapper {

    /**
     * Convierte un UsuarioRequestDTO a entidad Usuario
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "direcciones", ignore = true)
    @Mapping(target = "tokensRecuperacion", ignore = true)
    @Mapping(target = "fechaUltimoAcceso", ignore = true)
    @Mapping(target = "intentosFallidosLogin", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    /**
     * Convierte una entidad Usuario a UsuarioResponseDTO
     */
    @Mapping(target = "nombreCompleto", source = ".", qualifiedByName = "calculateNombreCompleto")
    @Mapping(target = "estaBloqueado", source = ".", qualifiedByName = "calculateEstaBloqueado")
    @Mapping(target = "cuentaHabilitada", source = ".", qualifiedByName = "calculateCuentaHabilitada")
    @Mapping(target = "edad", source = ".", qualifiedByName = "calculateEdad")
    UsuarioResponseDTO toResponseDTO(Usuario entity);

    /**
     * Convierte una lista de usuarios a lista de DTOs
     */
    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> entities);

    /**
     * Actualiza una entidad Usuario con datos de un DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "direcciones", ignore = true)
    @Mapping(target = "tokensRecuperacion", ignore = true)
    @Mapping(target = "fechaUltimoAcceso", ignore = true)
    @Mapping(target = "intentosFallidosLogin", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UsuarioRequestDTO dto, @MappingTarget Usuario entity);

    /**
     * Convierte un Set de roles a lista de DTOs
     */
    List<RolResponseDTO> rolesToResponseDTOList(Set<Rol> roles);

    /**
     * Convierte un rol a DTO
     */
    RolResponseDTO rolToResponseDTO(Rol rol);

    /**
     * Convierte una lista de direcciones a lista de DTOs
     */
    List<DireccionUsuarioResponseDTO> direccionesToResponseDTOList(List<DireccionUsuario> direcciones);

    /**
     * Convierte una dirección a DTO
     */
    @Mapping(target = "direccionCompleta", source = ".", qualifiedByName = "calculateDireccionCompleta")
    DireccionUsuarioResponseDTO direccionToResponseDTO(DireccionUsuario direccion);

    // Métodos auxiliares para campos calculados

    @Named("calculateNombreCompleto")
    default String calculateNombreCompleto(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getApellido() == null) {
            return null;
        }
        return usuario.getNombre() + " " + usuario.getApellido();
    }

    @Named("calculateEstaBloqueado")
    default Boolean calculateEstaBloqueado(Usuario usuario) {
        return usuario.estaBloqueado();
    }

    @Named("calculateCuentaHabilitada")
    default Boolean calculateCuentaHabilitada(Usuario usuario) {
        return usuario.isEnabled();
    }

    @Named("calculateEdad")
    default Integer calculateEdad(Usuario usuario) {
        if (usuario.getFechaNacimiento() == null) {
            return null;
        }
        return java.time.LocalDate.now().getYear() - usuario.getFechaNacimiento().getYear();
    }

    @Named("calculateDireccionCompleta")
    default String calculateDireccionCompleta(DireccionUsuario direccion) {
        return direccion.getDireccionCompleta();
    }
}
