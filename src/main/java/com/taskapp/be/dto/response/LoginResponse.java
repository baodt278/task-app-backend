package com.taskapp.be.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String username;
    private String tokenType = "Bearer";
    private String accessToken;

    public LoginResponse(String username, String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
    }
}