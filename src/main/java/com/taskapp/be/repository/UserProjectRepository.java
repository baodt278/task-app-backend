package com.taskapp.be.repository;

import com.taskapp.be.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    List<UserProject> findByProjectId(long id);

    UserProject findByProjectIdAndUserUsername(long id, String username);
}
