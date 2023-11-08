package com.taskapp.be.service;

import com.taskapp.be.dto.request.StatusDto;
import com.taskapp.be.dto.request.TaskDto;
import com.taskapp.be.dto.request.TaskRequest;
import com.taskapp.be.model.Task;

import java.util.List;

public interface TaskService {
    void createNewTask(TaskRequest taskRequest, long id, String username);

    List<Task> getTodoTaskInProject(long id);

    void updateTask(TaskDto taskDto, long id);

    void deleteTask(long taskId, String username);

    void assignToMe(long taskId, String username, String name);

    void setStatusTask(StatusDto choice, long taskId);

}
