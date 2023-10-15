package com.taskapp.be.repository;

import com.taskapp.be.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    UserProject findByProjectId(long id);
}
