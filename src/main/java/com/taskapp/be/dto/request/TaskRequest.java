package com.taskapp.be.dto.request;

import com.taskapp.be.util.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Priority priority;
}
