package com.taskapp.be.service;

import com.taskapp.be.dto.request.ChangePasswordRequest;
import com.taskapp.be.dto.request.LoginRequest;
import com.taskapp.be.dto.request.RegisterRequest;
import com.taskapp.be.dto.request.UpdateRequest;
import com.taskapp.be.dto.response.InfoResponse;
import com.taskapp.be.dto.response.LoginResponse;

public interface UserService {
    void createNewUser(RegisterRequest registerRequest);

    LoginResponse checkLoginUser(LoginRequest loginRequest);

    void updateUserInfo(UpdateRequest updateRequest, String username);

    InfoResponse loadUserInfo(String username);

    void changePasswordUser(ChangePasswordRequest changePasswordRequest, String username);
}
