package com.taskapp.be.controller;

import com.taskapp.be.dto.JwtResponse;
import com.taskapp.be.dto.LoginDto;
import com.taskapp.be.dto.RegisterDto;
import com.taskapp.be.dto.UserDto;
import com.taskapp.be.model.User;
import com.taskapp.be.security.jwt.JwtUtils;
import com.taskapp.be.security.principal.UserDetailsImpl;
import com.taskapp.be.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping(value = "/login")
    public ResponseEntity<?> getDetailUser(@RequestBody LoginDto loginDto, HttpServletResponse response) {
//        UserDto userDto = userService.getUserDetails(loginDto);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        response.addHeader("Authorization", "Bearer " + jwt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> getRegister(@RequestBody RegisterDto registerDto) {
        userService.getUserFromRegister(registerDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
