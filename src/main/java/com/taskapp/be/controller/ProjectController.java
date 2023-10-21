package com.taskapp.be.controller;

import com.taskapp.be.dto.request.ProjectRequest;
import com.taskapp.be.dto.response.ProjectResponse;
import com.taskapp.be.model.Project;
import com.taskapp.be.model.User;
import com.taskapp.be.model.UserProject;
import com.taskapp.be.repository.ProjectRepository;
import com.taskapp.be.repository.UserRepository;
import com.taskapp.be.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {
    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @PostMapping("/{username}")
    public ResponseEntity<?> createProjectByUser(@RequestBody ProjectRequest projectRequest, @PathVariable String username) {
        projectService.createNewProject(projectRequest, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all/{username}")
    public List<Project> getProjectsByUser(@PathVariable String username) {
        return projectService.getAllProjectsForUser(username);
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProjectInfo(@RequestBody ProjectRequest projectRequest, @PathVariable long id) {
        projectService.updateProject(projectRequest, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}/{username}")
    public ResponseEntity<?> deleteProject(@PathVariable long id, @PathVariable String username) {
        projectService.deleteProject(id, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/members")
    public List<User> getMembersInProject(@PathVariable long id) {
        return userRepository.findMembersInProject(id);
    }

    @GetMapping("/{id}/managers")
    public List<User> getManagersInProject(@PathVariable long id) {
        return userRepository.findManagersInProject(id);
    }

    @GetMapping("/{id}/users")
    public List<User> getMembersNotInProject(@PathVariable long id) {
        return userRepository.getUsersNotInProject(id);
    }

    @PostMapping("{id}/add/{username}")
    public ResponseEntity<?> addMemberToProject(@PathVariable long id, @PathVariable String username) {
        projectService.addUserToProject(username, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/all")
    public List<User> getAllMemberInProject(@PathVariable long projectId){
        return userRepository.findAllMember(projectId);
    }

    @GetMapping("/all/{username}")
    public List<Project> getProjectsUserNotIN(@PathVariable String username){
        return projectRepository.getProjectUserNotIN(username);
    }
}
