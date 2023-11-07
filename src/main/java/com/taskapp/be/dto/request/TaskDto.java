package com.taskapp.be.dto.request;

import com.taskapp.be.model.User;
import com.taskapp.be.util.Priority;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskDto {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Priority priority;
    private String username;
}
