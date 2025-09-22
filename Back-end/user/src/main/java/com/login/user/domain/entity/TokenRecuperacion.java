package com.login.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad de dominio que representa un token de recuperación.
 * Se usa para procesos de recuperación de contraseña y verificación de email.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Entity
@Table(name = "tokens_recuperacion",
       indexes = {
           @Index(name = "idx_token", columnList = "token"),
           @Index(name = "idx_usuario_id", columnList = "usuario_id"),
           @Index(name = "idx_fecha_expiracion", columnList = "fecha_expiracion")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"usuario", "token"})
@EqualsAndHashCode(of = "id")
public class TokenRecuperacion {

    /**
     * Enumeración para los tipos de token
     */
    public enum TipoToken {
        PASSWORD_RESET("Recuperación de Contraseña"),
        EMAIL_VERIFICATION("Verificación de Email");

        private final String descripcion;

        TipoToken(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_token", nullable = false)
    private TipoToken tipoToken;

    @Builder.Default
    @Column(name = "usado", nullable = false)
    private Boolean usado = false;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Métodos de negocio

    /**
     * Genera un nuevo token único
     */
    public static String generarToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Verifica si el token ha expirado
     */
    public boolean haExpirado() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }

    /**
     * Verifica si el token es válido para usar
     */
    public boolean esValido() {
        return !usado && !haExpirado();
    }

    /**
     * Marca el token como usado
     */
    public void marcarComoUsado() {
        this.usado = true;
    }

    /**
     * Calcula los minutos restantes antes de la expiración
     */
    public long minutosParaExpiracion() {
        if (haExpirado()) {
            return 0;
        }
        return java.time.Duration.between(LocalDateTime.now(), fechaExpiracion).toMinutes();
    }

    /**
     * Crea un token de recuperación de contraseña
     */
    public static TokenRecuperacion crearTokenRecuperacionPassword(Usuario usuario) {
        return TokenRecuperacion.builder()
                .usuario(usuario)
                .token(generarToken())
                .tipoToken(TipoToken.PASSWORD_RESET)
                .fechaExpiracion(LocalDateTime.now().plusHours(2)) // Expira en 2 horas
                .build();
    }

    /**
     * Crea un token de verificación de email
     */
    public static TokenRecuperacion crearTokenVerificacionEmail(Usuario usuario) {
        return TokenRecuperacion.builder()
                .usuario(usuario)
                .token(generarToken())
                .tipoToken(TipoToken.EMAIL_VERIFICATION)
                .fechaExpiracion(LocalDateTime.now().plusDays(7)) // Expira en 7 días
                .build();
    }

    // Callbacks JPA
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (token == null || token.trim().isEmpty()) {
            token = generarToken();
        }
    }
}
