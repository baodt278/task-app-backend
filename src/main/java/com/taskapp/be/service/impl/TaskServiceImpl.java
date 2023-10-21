package com.taskapp.be.service.impl;

import com.taskapp.be.dto.request.StatusDto;
import com.taskapp.be.dto.request.TaskDto;
import com.taskapp.be.dto.request.TaskRequest;
import com.taskapp.be.model.Task;
import com.taskapp.be.repository.ProjectRepository;
import com.taskapp.be.repository.TaskRepository;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.service.TaskService;
import com.taskapp.be.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public void createNewTask(TaskRequest taskRequest, long id, String username) {
        boolean checkTask = taskRepository.existsByNameAndProjectId(taskRequest.getName(), id);
        if (!checkTask) {
            Task task = Task.builder()
                    .name(taskRequest.getName())
                    .description(taskRequest.getDescription())
                    .startDate(taskRequest.getStartDate())
                    .endDate(taskRequest.getEndDate())
                    .priority(taskRequest.getPriority())
                    .status(Status.BACKLOG)
                    .project(projectRepository.getProjectById(id))
                    .creatorUser(userRepository.findByUsername(username))
                    .build();
            taskRepository.save(task);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already used");
        }
    }

    @Override
    public List<Task> getBacklogTaskInProject(long id) {
        List<Task> tasks = taskRepository.findByProjectId(id);
        for (Task task : tasks) {
            if (LocalDate.now().isAfter(task.getEndDate())) {
                task.setStatus(Status.FAILED);
            }
        }
        taskRepository.saveAll(tasks);
        return taskRepository.findBacklogTaskInProject(id);
    }

    @Override
    public void updateTask(TaskDto taskDto, long taskId) {
        Task check = taskRepository.findById(taskId);
        boolean test = false;
        List<Task> tasks = taskRepository.findByProjectId(check.getProject().getId());
        for (Task task : tasks) {
            if (task.getId() != taskId && taskDto.getName().equals(task.getName())) {
                test = true;
                break;
            }
        }
        if (!test) {
            check.setName(taskDto.getName());
            check.setDescription(taskDto.getDescription());
            check.setStartDate(taskDto.getStartDate());
            check.setEndDate(taskDto.getEndDate());
            check.setPriority(taskDto.getPriority());
            taskRepository.save(check);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already used");
        }
    }

    @Override
    public void assignToMe(long taskId, String username) {
        Task task = taskRepository.findById(taskId);
        task.setAssigneeUser(userRepository.findByUsername(username));
        taskRepository.save(task);
    }

    @Override
    public void setStatusTask(StatusDto choice, long taskId) {
        Task task = taskRepository.findById(taskId);
        task.setStatus(choice.getStatus());
        taskRepository.save(task);
    }
}
