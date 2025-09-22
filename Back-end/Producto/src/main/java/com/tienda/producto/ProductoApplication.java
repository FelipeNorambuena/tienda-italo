package com.tienda.producto;

import com.tienda.producto.config.JwtConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Aplicación principal del microservicio de productos.
 * 
 * @author Tienda Italo Team
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(JwtConfigProperties.class)
public class ProductoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductoApplication.class, args);
    }
}
