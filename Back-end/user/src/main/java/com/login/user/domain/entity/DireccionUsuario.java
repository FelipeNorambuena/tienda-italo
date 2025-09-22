package com.login.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa una dirección de usuario.
 * Puede ser de facturación, envío o ambas.
 * 
 * @author Tienda Italo Team
 * @since 1.0
 */
@Entity
@Table(name = "direcciones_usuario",
       indexes = {
           @Index(name = "idx_usuario_id", columnList = "usuario_id"),
           @Index(name = "idx_es_principal", columnList = "es_principal")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuario")
@EqualsAndHashCode(of = "id")
public class DireccionUsuario {

    /**
     * Enumeración para los tipos de dirección
     */
    public enum TipoDireccion {
        FACTURACION("Facturación"),
        ENVIO("Envío"),
        AMBAS("Facturación y Envío");

        private final String descripcion;

        TipoDireccion(String descripcion) {
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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_direccion", nullable = false)
    @Builder.Default
    private TipoDireccion tipoDireccion = TipoDireccion.ENVIO;

    @Column(name = "nombre_contacto", nullable = false, length = 100)
    private String nombreContacto;

    @Column(name = "calle", nullable = false, length = 255)
    private String calle;

    @Column(name = "numero", length = 10)
    private String numero;

    @Column(name = "piso", length = 10)
    private String piso;

    @Column(name = "departamento", length = 10)
    private String departamento;

    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad;

    @Column(name = "estado_provincia", nullable = false, length = 100)
    private String estadoProvincia;

    @Column(name = "codigo_postal", nullable = false, length = 20)
    private String codigoPostal;

    @Column(name = "pais", nullable = false, length = 100)
    @Builder.Default
    private String pais = "Colombia";

    @Builder.Default
    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Métodos de negocio

    /**
     * Obtiene la dirección completa formateada
     */
    public String getDireccionCompleta() {
        StringBuilder direccion = new StringBuilder();
        direccion.append(calle);
        
        if (numero != null && !numero.trim().isEmpty()) {
            direccion.append(" ").append(numero);
        }
        
        if (piso != null && !piso.trim().isEmpty()) {
            direccion.append(", Piso ").append(piso);
        }
        
        if (departamento != null && !departamento.trim().isEmpty()) {
            direccion.append(", Apto ").append(departamento);
        }
        
        direccion.append(", ").append(ciudad);
        direccion.append(", ").append(estadoProvincia);
        direccion.append(", ").append(codigoPostal);
        direccion.append(", ").append(pais);
        
        return direccion.toString();
    }

    /**
     * Verifica si la dirección puede ser usada para facturación
     */
    public boolean puedeUsarseParaFacturacion() {
        return tipoDireccion == TipoDireccion.FACTURACION || tipoDireccion == TipoDireccion.AMBAS;
    }

    /**
     * Verifica si la dirección puede ser usada para envío
     */
    public boolean puedeUsarseParaEnvio() {
        return tipoDireccion == TipoDireccion.ENVIO || tipoDireccion == TipoDireccion.AMBAS;
    }

    /**
     * Valida que la dirección tenga todos los campos obligatorios
     */
    public boolean esValida() {
        return nombreContacto != null && !nombreContacto.trim().isEmpty() &&
               calle != null && !calle.trim().isEmpty() &&
               ciudad != null && !ciudad.trim().isEmpty() &&
               estadoProvincia != null && !estadoProvincia.trim().isEmpty() &&
               codigoPostal != null && !codigoPostal.trim().isEmpty() &&
               pais != null && !pais.trim().isEmpty();
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
}
