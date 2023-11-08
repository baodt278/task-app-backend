package com.taskapp.be.service.impl;

import com.taskapp.be.dto.request.ChangePasswordRequest;
import com.taskapp.be.dto.request.LoginRequest;
import com.taskapp.be.dto.request.RegisterRequest;
import com.taskapp.be.dto.request.UpdateRequest;
import com.taskapp.be.dto.response.InfoResponse;
import com.taskapp.be.dto.response.LoginResponse;
import com.taskapp.be.model.User;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.security.custom.CustomUserDetails;
import com.taskapp.be.security.jwt.JwtTokenProvider;
import com.taskapp.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public void createNewUser(RegisterRequest registerRequest) {
        boolean check = userRepository.existsByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());
        if (!check) {
            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .fullName(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .build();
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or email already used");
        }
    }

    @Override
    public LoginResponse checkLoginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new LoginResponse(loginRequest.getUsername(), jwt);
    }

    @Override
    public void updateUserInfo(UpdateRequest updateRequest, String username) {
        User existUser = userRepository.findByUsername(username);
        existUser.setFullName(updateRequest.getFullName());
        if (!Objects.equals(existUser.getEmail(), updateRequest.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used!");
            }
            existUser.setEmail(updateRequest.getEmail());
        }
        userRepository.save(existUser);
    }


    @Override
    public InfoResponse loadUserInfo(String username) {
        InfoResponse infoResponse = new InfoResponse();
        User user = userRepository.findByUsername(username);
        infoResponse.setUsername(user.getUsername());
        infoResponse.setEmail(user.getEmail());
        infoResponse.setFullName(user.getFullName());
        return infoResponse;
    }

    @Override
    public void changePasswordUser(ChangePasswordRequest changePasswordRequest, String username) {
        User existUser = userRepository.findByUsername(username);
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), existUser.getPassword())) {
            existUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(existUser);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password!");
        }
    }
}
