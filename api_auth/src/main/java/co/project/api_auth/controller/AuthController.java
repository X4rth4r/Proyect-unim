package co.project.api_auth.controller;

import co.project.api_auth.dto.LoginRequest;
import co.project.api_auth.dto.LoginResponse;
import co.project.api_auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;  // ← AGREGAR ESTA LÍNEA

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));  // ← aquí usaba Map sin import
        }
    }

    @GetMapping("/health")
    public String health() {
        return "Auth Service - OK";
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
    return ResponseEntity.ok(Map.of(
        "message", "Perfil del usuario",
        "user", "admin"
    ));
}

    @GetMapping("/admin/dashboard")
    public ResponseEntity<?> adminDashboard() {
    return ResponseEntity.ok(Map.of(
        "message", "Panel de administración",
        "data", "Solo admins pueden ver esto"
    ));
    }
}   
