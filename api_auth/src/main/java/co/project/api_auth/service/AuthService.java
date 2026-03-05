package co.project.api_auth.service;

import co.project.api_auth.dto.LoginRequest;
import co.project.api_auth.dto.LoginResponse;
import co.project.api_auth.entity.Usuario;
import co.project.api_auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRoles());

        return new LoginResponse(
                token,
                jwtExpiration / 60, // convertir a segundos
                usuario.getUsername(),
                usuario.getRoles()
        );
    }
}