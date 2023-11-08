package com.taskapp.be.repository;

import com.taskapp.be.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByName(String name);

    Project getProjectById(Long id);

    @Query("select p from Project p " +
            "join UserProject u " +
            "on p.id = u.project.id " +
            "where u.user.username = :username")
    List<Project> getProjectsForUser(@Param("username") String username);

    @Query("SELECT p FROM Project p " +
            "WHERE p.id NOT IN (" +
            "SELECT u.project.id FROM UserProject u " +
            "WHERE u.user.username = :username)")
    List<Project> getProjectUserNotIN(@Param("username") String username);

}
