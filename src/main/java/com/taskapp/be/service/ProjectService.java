package com.taskapp.be.service;

import com.taskapp.be.dto.request.ProjectRequest;
import com.taskapp.be.model.Project;

import java.util.List;

public interface ProjectService {
    void createNewProject(ProjectRequest projectRequest, String username);

    Project getProjectById(long id);

    List<Project> getAllProjectsForUser(String username);

    void updateProject(ProjectRequest projectRequest, long id);


    void deleteProject(long id, String username);

    void addUserToProject(String username, long id);
}
