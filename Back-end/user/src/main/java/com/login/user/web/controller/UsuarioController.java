package com.login.user.web.controller;

import com.login.user.application.dto.*;
import com.login.user.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para gestión de usuarios.
 * Incluye operaciones CRUD y gestión de perfiles.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gestión de Usuarios", description = "Endpoints para gestión completa de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ===== ENDPOINTS DE PERFIL PERSONAL =====

    @Operation(
        summary = "Obtener perfil actual",
        description = "Obtiene la información del perfil del usuario autenticado"
    )
    @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente",
                content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> obtenerPerfilActual() {
        UsuarioResponseDTO perfil = usuarioService.obtenerPerfilActual();
        return ResponseEntity.ok(perfil);
    }

    @Operation(
        summary = "Actualizar perfil actual",
        description = "Actualiza la información del perfil del usuario autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> actualizarPerfilActual(
            @Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        
        UsuarioResponseDTO perfilActualizado = usuarioService.actualizarPerfilActual(usuarioRequest);
        return ResponseEntity.ok(perfilActualizado);
    }

    @Operation(
        summary = "Cambiar contraseña",
        description = "Cambia la contraseña del usuario autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta")
    })
    @PostMapping("/profile/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> cambiarPassword(
            @RequestBody Map<String, String> request) {
        
        String passwordActual = request.get("currentPassword");
        String passwordNueva = request.get("newPassword");
        
        if (passwordActual == null || passwordNueva == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Obtener ID del usuario actual desde el perfil
        UsuarioResponseDTO perfil = usuarioService.obtenerPerfilActual();
        usuarioService.cambiarPassword(perfil.getId(), passwordActual, passwordNueva);
        
        return ResponseEntity.ok(Map.of("message", "Contraseña cambiada exitosamente"));
    }

    // ===== ENDPOINTS DE ADMINISTRACIÓN =====

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Obtiene una lista paginada de todos los usuarios (solo administradores)"
    )
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioResponseDTO>> obtenerUsuarios(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<UsuarioResponseDTO> usuarios = usuarioService.obtenerUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(
        summary = "Buscar usuario por ID",
        description = "Busca un usuario específico por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') and #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        
        return usuarioService.buscarUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza la información de un usuario específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        
        UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioRequest);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @Operation(
        summary = "Activar usuario",
        description = "Activa un usuario previamente desactivado"
    )
    @ApiResponse(responseCode = "200", description = "Usuario activado exitosamente",
                content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @PostMapping("/users/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> activarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        
        UsuarioResponseDTO usuario = usuarioService.cambiarEstadoUsuario(id, true);
        return ResponseEntity.ok(usuario);
    }

    @Operation(
        summary = "Desactivar usuario",
        description = "Desactiva un usuario del sistema"
    )
    @ApiResponse(responseCode = "200", description = "Usuario desactivado exitosamente",
                content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @PostMapping("/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> desactivarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        
        UsuarioResponseDTO usuario = usuarioService.cambiarEstadoUsuario(id, false);
        return ResponseEntity.ok(usuario);
    }

    @Operation(
        summary = "Desbloquear usuario",
        description = "Desbloquea un usuario que fue bloqueado por intentos fallidos"
    )
    @ApiResponse(responseCode = "200", description = "Usuario desbloqueado exitosamente",
                content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @PostMapping("/users/{id}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> desbloquearUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        
        UsuarioResponseDTO usuario = usuarioService.desbloquearUsuario(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina (desactiva) un usuario del sistema"
    )
    @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente")
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> eliminarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado exitosamente"));
    }

    // ===== ENDPOINTS DE BÚSQUEDA =====

    @Operation(
        summary = "Buscar usuarios por nombre",
        description = "Busca usuarios por nombre o apellido"
    )
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    @GetMapping("/users/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<Page<UsuarioResponseDTO>> buscarUsuariosPorNombre(
            @Parameter(description = "Término de búsqueda", required = true)
            @RequestParam String termino,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuariosPorNombre(termino, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(
        summary = "Obtener usuarios por rol",
        description = "Obtiene usuarios filtrados por rol específico"
    )
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente")
    @GetMapping("/users/by-role/{roleName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<Page<UsuarioResponseDTO>> obtenerUsuariosPorRol(
            @Parameter(description = "Nombre del rol", required = true)
            @PathVariable String roleName,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuariosPorRol(roleName, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(
        summary = "Obtener usuarios activos",
        description = "Obtiene una lista paginada de usuarios activos"
    )
    @ApiResponse(responseCode = "200", description = "Usuarios activos obtenidos exitosamente")
    @GetMapping("/users/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<Page<UsuarioResponseDTO>> obtenerUsuariosActivos(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<UsuarioResponseDTO> usuarios = usuarioService.obtenerUsuariosActivos(pageable);
        return ResponseEntity.ok(usuarios);
    }

    // ===== ENDPOINTS DE GESTIÓN DE ROLES =====

    @Operation(
        summary = "Asignar rol a usuario",
        description = "Asigna un rol específico a un usuario"
    )
    @ApiResponse(responseCode = "200", description = "Rol asignado exitosamente",
                content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @PostMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> asignarRol(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID del rol", required = true)
            @PathVariable Long roleId) {
        
        UsuarioResponseDTO usuario = usuarioService.asignarRol(userId, roleId);
        return ResponseEntity.ok(usuario);
    }

    @Operation(
        summary = "Remover rol de usuario",
        description = "Remueve un rol específico de un usuario"
    )
    @ApiResponse(responseCode = "200", description = "Rol removido exitosamente",
                content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> removerRol(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID del rol", required = true)
            @PathVariable Long roleId) {
        
        UsuarioResponseDTO usuario = usuarioService.removerRol(userId, roleId);
        return ResponseEntity.ok(usuario);
    }

    // ===== ENDPOINTS DE ESTADÍSTICAS =====

    @Operation(
        summary = "Obtener estadísticas de usuarios",
        description = "Obtiene estadísticas generales del sistema de usuarios"
    )
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente",
                content = @Content(schema = @Schema(implementation = EstadisticasUsuarioDTO.class)))
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstadisticasUsuarioDTO> obtenerEstadisticas() {
        EstadisticasUsuarioDTO estadisticas = usuarioService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }
}
