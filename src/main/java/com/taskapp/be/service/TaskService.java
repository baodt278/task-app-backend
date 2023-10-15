package com.taskapp.be.service;

import com.taskapp.be.dto.request.TaskRequest;

public interface TaskService {
    void createNewTask(TaskRequest taskRequest, long id, String username);
}
