package co.project.api_auth.security;

import co.project.api_auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String username = jwtService.extractUsername(token);
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtService.validateToken(token, username)) {
                        
                        // Extraer roles del token
                        String rolesStr = jwtService.extractClaims(token).get("roles", String.class);
                        List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesStr.split(","))
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                                .collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Token inválido
                System.err.println("Error validando token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}