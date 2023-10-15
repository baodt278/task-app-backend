package com.taskapp.be.model;

import com.taskapp.be.util.Priority;
import com.taskapp.be.util.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User creatorUser;
    @ManyToOne
    @JoinColumn(name = "assignee_user_id")
    private User assigneeUser;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
