package com.taskapp.be.service;

import com.taskapp.be.dto.LoginDto;
import com.taskapp.be.dto.RegisterDto;
import com.taskapp.be.dto.UserDto;

public interface UserService {
    void getUserFromRegister(RegisterDto registerDto);

    UserDto getUserDetails(LoginDto loginDto);
}
