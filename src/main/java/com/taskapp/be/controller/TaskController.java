package com.taskapp.be.controller;

import com.taskapp.be.dto.request.StatusDto;
import com.taskapp.be.dto.request.TaskDto;
import com.taskapp.be.dto.request.TaskRequest;
import com.taskapp.be.model.Task;
import com.taskapp.be.repository.TaskRepository;
import com.taskapp.be.service.TaskService;
import com.taskapp.be.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable long taskId){
        return taskRepository.findById(taskId);
    }


    @PostMapping("/{id}/project/{username}/creator")
    public ResponseEntity<?> createNewTask(@RequestBody TaskRequest taskRequest, @PathVariable long id, @PathVariable String username) {
        taskService.createNewTask(taskRequest, id, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/backlog/{id}/project")
    public List<Task> getBacklogTasksByProjectId(@PathVariable long id) {
        return taskService.getBacklogTaskInProject(id);
    }

    @GetMapping("/todo/{id}/project")
    public List<Task> getTodoTasksByProjectId(@PathVariable long id) {
        return taskRepository.findTodoTaskInProject(id);
    }

    @GetMapping("/in-progress/{id}/project")
    public List<Task> getInprogressTasksByProjectId(@PathVariable long id) {
        return taskRepository.findInprogressTaskInProject(id);
    }

    @GetMapping("/done/{id}/project")
    public List<Task> getDoneTasksByProjectId(@PathVariable long id) {
        return taskRepository.findDoneTaskInProject(id);
    }

    @GetMapping("/failed/{id}/project")
    public List<Task> getTasksByProjectId(@PathVariable long id) {
        return taskRepository.findFailedTaskInProject(id);
    }

    @PostMapping("/{taskId}/assignee/{username}")
    public ResponseEntity<?> assigneeMemberToTask(@PathVariable long taskId, @PathVariable String username) {
        taskService.assignToMe(taskId, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/status")
    public ResponseEntity<?> changeStatusTask(@RequestBody StatusDto choice, @PathVariable long taskId) {
        taskService.setStatusTask(choice, taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/update")
    public ResponseEntity<?> updateTaskInfo(@RequestBody TaskDto taskDto, @PathVariable long taskId) {
        taskService.updateTask(taskDto, taskId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}/delete")
    public ResponseEntity<?> deleteTask(@PathVariable long taskId) {
        taskRepository.deleteById(taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/assignee")
    public List<Task> getTasksForUser(@PathVariable String username){
        return taskRepository.findByAssigneeUserUsername(username);
    }
}
