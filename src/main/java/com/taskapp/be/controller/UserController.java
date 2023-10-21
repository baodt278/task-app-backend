package com.taskapp.be.controller;

import com.taskapp.be.dto.request.ChangePasswordRequest;
import com.taskapp.be.dto.response.InfoResponse;
import com.taskapp.be.dto.request.UpdateRequest;
import com.taskapp.be.model.User;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.service.UserService;
import com.taskapp.be.service.impl.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final MessageService messageService;
    @GetMapping("/{username}")
    public InfoResponse getUserInfo(@PathVariable String username) {
        return userService.loadUserInfo(username);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateInfo(@RequestBody UpdateRequest updateRequest, @PathVariable String username) {
        userService.updateUserInfo(updateRequest, username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password/{username}")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @PathVariable String username){
        userService.changePasswordUser(changePasswordRequest, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/message")
    public List<String> getMessage(@PathVariable String username){
        return messageService.notifyUser(username);
    }
}
