package com.taskapp.be.service.impl;

import com.taskapp.be.model.Task;
import com.taskapp.be.repository.TaskRepository;
import com.taskapp.be.util.Status;
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
        LocalDate after3days = LocalDate.now().plusDays(3);
        List<Task> assignTask = taskRepository.findForMessage(username);
        List<String> messages = new ArrayList<>();
        for (Task task : assignTask) {
            if (task.getStatus() == Status.FAILED && after3days.isAfter(task.getEndDate())) {
                messages.add(createMessage(3, task));
            }
            if (task.getStatus() == Status.TODO || task.getStatus() == Status.IN_PROGRESS) {
                if (today.isEqual(task.getEndDate())) {
                    messages.add(createMessage(2, task));
                } else {
                    messages.add(createMessage(1, task));
                }
            }
        }
        return messages;
    }

    private String createMessage(int type, Task task) {
        return switch (type) {
            case 1 -> String.format("Assigned to you\nPlease check the description before start %s.", task.getName());
            case 2 -> String.format("Deadline\nToday is the end of %s.", task.getName());
            case 3 -> String.format("Failed\n%s is marked as FAILED.", task.getName());
            default -> "Invalid type";
        };
    }

}
