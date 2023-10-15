package com.taskapp.be.service.impl;

import com.taskapp.be.dto.request.TaskRequest;
import com.taskapp.be.model.Task;
import com.taskapp.be.repository.ProjectRepository;
import com.taskapp.be.repository.TaskRepository;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public void createNewTask(TaskRequest taskRequest, long id, String username) {
        Task task = Task.builder()
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .startDate(taskRequest.getStartDate())
                .endDate(taskRequest.getEndDate())
                .priority(taskRequest.getPriority())
                .project(projectRepository.getProjectById(id))
                .creatorUser(userRepository.findByUsername(username))
                .build();
        taskRepository.save(task);
    }
}
