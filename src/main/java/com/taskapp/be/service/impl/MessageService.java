package com.taskapp.be.service.impl;

import com.taskapp.be.model.Task;
import com.taskapp.be.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final TaskRepository taskRepository;

    public List<String> notifyUser(String username) {
        LocalDate today = LocalDate.now();
        List<Task> assignTask = taskRepository.findByAssigneeUserUsername(username);
        List<String> messages = new ArrayList<>();
        for (Task task : assignTask) {
            messages.add(createMessage(1, task));
            if (task.getEndDate().isEqual(today)) {
                messages.add(createMessage(2, task));
            }
            if (task.getEndDate().isAfter(today)) {
                messages.add(createMessage(3, task));
            }
        }
        return messages;
    }

    private String createMessage(int type, Task task) {
        return switch (type) {
            case 1 ->
                    String.format("Task %1$s from Project %2$s was assigned to you. Please check information before start working.", task.getName(), task.getProject().getName());
            case 2 ->
                    String.format("Deadline is coming, %1$s is the end of Task %2$s from Project %3$s", task.getEndDate(), task.getName(), task.getProject().getName());
            case 3 ->
                    String.format("Task %1$s from Project %2$s is now outdated.", task.getName(), task.getProject().getName());
            default -> "Invalid type";
        };
    }

}
