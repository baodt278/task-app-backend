package com.taskapp.be.controller;

import com.taskapp.be.dto.JwtResponse;
import com.taskapp.be.dto.LoginDto;
import com.taskapp.be.dto.RegisterDto;
import com.taskapp.be.dto.UserDto;
import com.taskapp.be.security.jwt.JwtUtils;
import com.taskapp.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping(value = "/login")
    public ResponseEntity<?> getDetailUser(@RequestBody LoginDto loginDto) {
        UserDto userDto = userService.getUserDetails(loginDto);
        String jwt = jwtUtils.generateJwtToken(userDto.getUsername(), String.valueOf(userDto.getRoleType()));
        return ResponseEntity
                .ok(new JwtResponse(userDto.getUsername(), jwt));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> getRegister(@RequestBody RegisterDto registerDto) {
        userService.getUserFromRegister(registerDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
