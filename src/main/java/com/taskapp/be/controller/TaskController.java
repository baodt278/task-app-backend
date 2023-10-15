package com.taskapp.be.controller;

import com.taskapp.be.dto.request.TaskRequest;
import com.taskapp.be.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/{id}/{username}")
    public ResponseEntity<?> createNewTask(@RequestBody TaskRequest taskRequest,@PathVariable long id, @PathVariable String username){
        taskService.createNewTask(taskRequest, id, username);
        return ResponseEntity.ok().build();
    }
}
