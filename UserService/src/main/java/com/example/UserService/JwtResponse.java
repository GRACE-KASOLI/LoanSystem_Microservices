package com.example.UserService;

public class JwtResponse {
    private String token;
    private String Role;

    public JwtResponse(String token, String role) {
        this.token = token;
        this.Role = role;
    }

    // Getters and setters
}
