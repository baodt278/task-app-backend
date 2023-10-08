package com.taskapp.be.controller;

import com.taskapp.be.dto.request.LoginRequest;
import com.taskapp.be.dto.response.LoginResponse;
import com.taskapp.be.dto.request.RegisterRequest;
import com.taskapp.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.createNewUser(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest) {

        return userService.checkLoginUser(loginRequest);

    }
}
