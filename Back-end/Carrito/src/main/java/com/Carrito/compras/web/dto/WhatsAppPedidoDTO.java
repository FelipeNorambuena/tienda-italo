package com.Carrito.compras.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppPedidoDTO {
    
    private String numeroWhatsApp;
    private String mensaje;
    private List<ProductoPedidoDTO> productos;
    private BigDecimal total;
    private String urlWhatsApp;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoPedidoDTO {
        private String nombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
