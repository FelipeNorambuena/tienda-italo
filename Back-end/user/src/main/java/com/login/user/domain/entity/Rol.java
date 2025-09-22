package com.login.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entidad de dominio que representa un rol del sistema.
 * Define los diferentes niveles de acceso y permisos.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuarios")
@EqualsAndHashCode(of = "id")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relaciones
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios;

    // Métodos de negocio

    /**
     * Verifica si es un rol de administrador
     */
    public boolean esAdministrador() {
        return "ADMIN".equals(this.nombre);
    }

    /**
     * Verifica si es un rol de cliente
     */
    public boolean esCliente() {
        return "CLIENTE".equals(this.nombre);
    }

    /**
     * Verifica si es un rol de gestor
     */
    public boolean esGestor() {
        return "GESTOR".equals(this.nombre);
    }

    /**
     * Verifica si es un rol de vendedor
     */
    public boolean esVendedor() {
        return "VENDEDOR".equals(this.nombre);
    }

    // Callbacks JPA
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constantes para roles del sistema
    public static final String ADMIN = "ADMIN";
    public static final String CLIENTE = "CLIENTE";
    public static final String GESTOR = "GESTOR";
    public static final String VENDEDOR = "VENDEDOR";
}
