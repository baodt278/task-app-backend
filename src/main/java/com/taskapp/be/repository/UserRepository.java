package com.taskapp.be.repository;

import com.taskapp.be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);

    User findByUsername(String username);
}
