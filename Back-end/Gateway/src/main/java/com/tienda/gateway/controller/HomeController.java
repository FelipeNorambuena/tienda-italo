package com.tienda.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para el frontend del Gateway.
 * 
 * @author Tienda Italo Team
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Tienda Italo - Inicio");
        model.addAttribute("message", "Bienvenido a Tienda Italo");
        return "index";
    }

    @GetMapping("/index.html")
    public String index(Model model) {
        return home(model);
    }
}
