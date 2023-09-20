package com.taskapp.be.dto;

import com.taskapp.be.util.RoleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String name;
    private String token;
    private String type = "Bearer ";

    public JwtResponse(String name, String accessToken) {
        this.name = name;
        this.token = accessToken;
    }
}
