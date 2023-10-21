package com.taskapp.be.service;

import com.taskapp.be.dto.request.StatusDto;
import com.taskapp.be.dto.request.TaskDto;
import com.taskapp.be.dto.request.TaskRequest;
import com.taskapp.be.model.Task;
import com.taskapp.be.util.Status;

import java.util.List;

public interface TaskService {
    void createNewTask(TaskRequest taskRequest, long id, String username);

    List<Task> getBacklogTaskInProject(long id);

    void updateTask(TaskDto taskDto, long id);

    void assignToMe(long taskId, String username);

    void setStatusTask(StatusDto choice, long taskId);

}
