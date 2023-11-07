package com.taskapp.be.service.impl;

import com.taskapp.be.dto.request.StatusDto;
import com.taskapp.be.dto.request.TaskDto;
import com.taskapp.be.dto.request.TaskRequest;
import com.taskapp.be.model.ActivityStream;
import com.taskapp.be.model.Project;
import com.taskapp.be.model.Task;
import com.taskapp.be.repository.ActivityStreamRepo;
import com.taskapp.be.repository.ProjectRepository;
import com.taskapp.be.repository.TaskRepository;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.service.TaskService;
import com.taskapp.be.util.Status;
import com.taskapp.be.util.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ActivityStreamRepo activityStreamRepo;

    public String typeOfStream(Type type) {
        return switch (type) {
            case CREATE -> "%s created %s";
            case UPDATE -> "%s updated %s";
            case DELETE -> "%s deleted %s";
            case ASSIGN -> "%s changed the assignee %s on %s";
            case CHANGE -> "%s changed the status to %s on %s";
            case IN -> "%s joined the project";
            case OUT -> "%s left the project";
            case START -> "%s created the project";
            case DETAIL -> "%s updated project's detail";
        };
    }

    public String prepareStream(Type type, Object... items) {
        return String.format(typeOfStream(type), items);
    }


    @Override
    public void createNewTask(TaskRequest taskRequest, long id, String username) {
        boolean checkTask = taskRepository.existsByNameAndProjectId(taskRequest.getName(), id);
        if (!checkTask) {
            Project project = projectRepository.getProjectById(id);
            Task task = Task.builder()
                    .name(taskRequest.getName())
                    .description(taskRequest.getDescription())
                    .startDate(taskRequest.getStartDate())
                    .endDate(taskRequest.getEndDate())
                    .priority(taskRequest.getPriority())
                    .status(Status.TODO)
                    .project(project)
                    .creatorUser(userRepository.findByUsername(username))
                    .build();
            taskRepository.save(task);
            ActivityStream stream = ActivityStream.builder()
                    .message(prepareStream(Type.CREATE, username, task.getName()))
                    .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .build();
            activityStreamRepo.save(stream);
            if (project.getStreams() == null) {
                project.setStreams(new ArrayList<>());
            }
            project.getStreams().add(stream);
            projectRepository.save(project);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already used");
        }
    }

    @Override
    public List<Task> getTodoTaskInProject(long id) {
        List<Task> tasks = taskRepository.findByProjectId(id);
        for (Task task : tasks) {
            if (task.getStatus() != Status.DONE && LocalDate.now().isAfter(task.getEndDate())) {
                task.setStatus(Status.FAILED);
            }
        }
        taskRepository.saveAll(tasks);
        return taskRepository.findTodoTaskInProject(id);
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
            ActivityStream stream = ActivityStream.builder()
                    .message(prepareStream(Type.UPDATE, taskDto.getUsername(), check.getName()))
                    .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .build();
            activityStreamRepo.save(stream);
            if (check.getProject().getStreams() == null) {
                check.getProject().setStreams(new ArrayList<>());
            }
            check.getProject().getStreams().add(stream);
            projectRepository.save(check.getProject());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already used");
        }
    }

    @Override
    public void deleteTask(long taskId, String username) {
        Task task = taskRepository.findById(taskId);
        ActivityStream stream = ActivityStream.builder()
                .message(prepareStream(Type.DELETE, username.substring(0, username.length() - 1), task.getName()))
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
        activityStreamRepo.save(stream);
        if (task.getProject().getStreams() == null) {
            task.getProject().setStreams(new ArrayList<>());
        }
        task.getProject().getStreams().add(stream);
        projectRepository.save(task.getProject());
        taskRepository.deleteById(taskId);
    }

    @Override
    public void assignToMe(long taskId, String username, String name) {
        Task task = taskRepository.findById(taskId);
        task.setAssigneeUser(userRepository.findByUsername(username));
        taskRepository.save(task);
        ActivityStream stream = ActivityStream.builder()
                .message(prepareStream(Type.ASSIGN, name.substring(0, name.length() - 1), username, task.getName()))
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
        activityStreamRepo.save(stream);
        if (task.getProject().getStreams() == null) {
            task.getProject().setStreams(new ArrayList<>());
        }
        task.getProject().getStreams().add(stream);
        projectRepository.save(task.getProject());
    }

    @Override
    public void setStatusTask(StatusDto choice, long taskId) {
        Task task = taskRepository.findById(taskId);
        task.setStatus(choice.getStatus());
        taskRepository.save(task);
        ActivityStream stream = ActivityStream.builder()
                .message(prepareStream(Type.CHANGE, choice.getUsername(), task.getStatus(), task.getName()))
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
        activityStreamRepo.save(stream);
        if (task.getProject().getStreams() == null) {
            task.getProject().setStreams(new ArrayList<>());
        }
        task.getProject().getStreams().add(stream);
        projectRepository.save(task.getProject());
    }
}
