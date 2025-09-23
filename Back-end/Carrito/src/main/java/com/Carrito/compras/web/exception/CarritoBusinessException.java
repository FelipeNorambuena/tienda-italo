package com.Carrito.compras.web.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción personalizada para errores de negocio del carrito.
 * Permite especificar el código HTTP apropiado para cada tipo de error.
 */
public class CarritoBusinessException extends RuntimeException {
    
    private final HttpStatus httpStatus;
    
    public CarritoBusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    
    public CarritoBusinessException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
