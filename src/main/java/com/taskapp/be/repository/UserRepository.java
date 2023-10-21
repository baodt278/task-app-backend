package com.taskapp.be.repository;

import com.taskapp.be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT up.user.id FROM UserProject up WHERE up.project.id = :projectId)")
    List<User> getUsersNotInProject(long projectId);

    @Query("select u from User u " +
            "join UserProject up on u.id = up.user.id " +
            "where up.project.id = :projectId and up.projectRole = 'MANAGER'")
    List<User> findManagersInProject(long projectId);

    @Query("select u from User u " +
            "join UserProject up on u.id = up.user.id " +
            "where up.project.id = :projectId and up.projectRole = 'MEMBER'")
    List<User> findMembersInProject(long projectId);

    @Query("select u from User u " +
            "join UserProject up on u.id = up.user.id " +
            "where up.project.id = :projectId")
    List<User> findAllMember(long projectId);
}
