package com.taskapp.be.service.impl;

import com.taskapp.be.model.Task;
import com.taskapp.be.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
            if (today.isEqual(task.getEndDate())) {
                messages.add(createMessage(2, task));
            } else if (today.isAfter(task.getEndDate())) {
                messages.add(createMessage(3, task));
            } else {
                messages.add(createMessage(1, task));
            }

        }
        Collections.reverse(messages);
        return messages;
    }

    private String createMessage(int type, Task task) {
        return switch (type) {
            case 1 ->
                    String.format("Assigned to you\nPlease check description before start Task %1$s.", task.getName());
            case 2 ->
                    String.format("Deadline is coming\nToday is the end of Task %1$s.", task.getName());
            case 3 ->
                    String.format("Outdated\nTask %1$s is now outdated.", task.getName());
            default -> "Invalid type";
        };
    }

}
