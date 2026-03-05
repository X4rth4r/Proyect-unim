package co.project.api_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Long expiresIn; // en segundos
    private String username;
    private String roles;

    public LoginResponse(String token, Long expiresIn, String username, String roles) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
        this.roles = roles;
    }
}