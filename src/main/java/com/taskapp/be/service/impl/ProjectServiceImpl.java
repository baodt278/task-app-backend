package com.taskapp.be.service.impl;

import com.taskapp.be.dto.request.ProjectRequest;
import com.taskapp.be.model.ActivityStream;
import com.taskapp.be.model.Project;
import com.taskapp.be.model.User;
import com.taskapp.be.model.UserProject;
import com.taskapp.be.repository.ActivityStreamRepo;
import com.taskapp.be.repository.ProjectRepository;
import com.taskapp.be.repository.UserProjectRepository;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.service.ProjectService;
import com.taskapp.be.util.ProjectRole;
import com.taskapp.be.util.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;
    private final TaskServiceImpl taskService;
    private final ActivityStreamRepo activityStreamRepo;

    @Override
    public void createNewProject(ProjectRequest projectRequest, String username) {
        if (!projectRepository.existsByName(projectRequest.getName())) {
            ActivityStream stream = ActivityStream.builder()
                    .message(taskService.prepareStream(Type.START, username))
                    .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .build();
            activityStreamRepo.save(stream);
            Project project = Project.builder()
                    .name(projectRequest.getName())
                    .description(projectRequest.getDescription())
                    .build();
            project.setStreams(new ArrayList<>());
            project.getStreams().add(stream);
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
        ActivityStream stream = ActivityStream.builder()
                .message(taskService.prepareStream(Type.DETAIL, projectRequest.getUsername()))
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
        activityStreamRepo.save(stream);
        if (project.getStreams() == null) {
            project.setStreams(new ArrayList<>());
        }
        project.getStreams().add(stream);
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(long id, String username) {
        List<User> managers = userRepository.findManagersInProject(id);
        for (User user: managers){
            if (username.equals(user.getUsername())){
                List<UserProject> userProjects = userProjectRepository.findByProjectId(id);
                userProjectRepository.deleteAll(userProjects);
                projectRepository.deleteById(id);
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member cannot delete project!");
            }
        }
    }


    @Override
    public void outProject(long id, String username){
        UserProject userProject = userProjectRepository.findByProjectIdAndUserUsername(id, username);
        userProjectRepository.delete(userProject);
        ActivityStream stream = ActivityStream.builder()
                .message(taskService.prepareStream(Type.OUT, username))
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
        activityStreamRepo.save(stream);
        Project project= projectRepository.getProjectById(id);
        if (project.getStreams() == null) {
            project.setStreams(new ArrayList<>());
        }
        project.getStreams().add(stream);
        projectRepository.save(project);
    }
    @Override
    public void addUserToProject(String username, long id){
        Project project= projectRepository.getProjectById(id);
        UserProject userProject = UserProject.builder()
                .user(userRepository.findByUsername(username))
                .project(project)
                .projectRole(ProjectRole.MEMBER)
                .build();
        userProjectRepository.save(userProject);
        ActivityStream stream = ActivityStream.builder()
                .message(taskService.prepareStream(Type.IN, username))
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
        activityStreamRepo.save(stream);
        if (project.getStreams() == null) {
            project.setStreams(new ArrayList<>());
        }
        project.getStreams().add(stream);
        projectRepository.save(project);
    }

}
