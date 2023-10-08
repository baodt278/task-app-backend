package com.taskapp.be.service.impl;

import com.taskapp.be.dto.request.ProjectRequest;
import com.taskapp.be.model.Project;
import com.taskapp.be.model.UserProject;
import com.taskapp.be.repository.ProjectRepository;
import com.taskapp.be.repository.UserProjectRepository;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.service.ProjectService;
import com.taskapp.be.util.ProjectRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;

    @Override
    public void createNewProject(ProjectRequest projectRequest, String username) {
        if (!projectRepository.existsByName(projectRequest.getName())) {
            Project project = Project.builder()
                    .name(projectRequest.getName())
                    .description(projectRequest.getDescription())
                    .build();
            projectRepository.save(project);
            UserProject userProject = UserProject.builder()
                    .user(userRepository.findByUsername(username))
                    .project(project)
                    .projectRole(ProjectRole.MANAGER)
                    .build();
            userProjectRepository.save(userProject);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project name already used");
        }
    }
    @Override
    public Project getProjectById(long id){
        return projectRepository.getProjectById(id);
    }
    @Override
    public List<Project> getAllProjectsForUser(String username) {
        return projectRepository.getProjectsForUser(username);
    }

    @Override
    public void updateProject(ProjectRequest projectRequest, long id) {
        Project project = projectRepository.getProjectById(id);
        project.setDescription(projectRequest.getDescription());
        if (!Objects.equals(project.getName(), projectRequest.getDescription())) {
            if (projectRepository.existsByName(projectRequest.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project name already used!");
            }
            project.setName(projectRequest.getName());
        }
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
