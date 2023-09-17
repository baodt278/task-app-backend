package com.taskapp.be.service.impl;

import com.taskapp.be.dto.LoginDto;
import com.taskapp.be.dto.RegisterDto;
import com.taskapp.be.dto.UserDto;
import com.taskapp.be.model.User;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.security.jwt.JwtUtils;
import com.taskapp.be.security.principal.UserDetailsImpl;
import com.taskapp.be.service.UserService;
import com.taskapp.be.util.LoginType;
import com.taskapp.be.util.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void getUserFromRegister(RegisterDto registerDto) {
        if (!userRepository.existsByUsername(registerDto.getUsername())) {
            User user = new User();
            BeanUtils.copyProperties(registerDto, user);
            user.setRole(RoleType.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setLoginType(LoginType.LOCAL);
            userRepository.save(user);
        }
    }

    @Override
    public UserDto getUserDetails(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> null);
        if (Objects.isNull(user)) {
            return null;
        } else {
            UserDto userDTO = new UserDto();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }
    }
}
