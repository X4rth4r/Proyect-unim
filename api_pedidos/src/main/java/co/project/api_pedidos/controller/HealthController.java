package co.project.api_pedidos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/pedidos")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "API Pedidos - OK";
    }
}