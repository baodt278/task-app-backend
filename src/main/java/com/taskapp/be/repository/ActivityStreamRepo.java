package com.taskapp.be.repository;

import com.taskapp.be.model.ActivityStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityStreamRepo extends JpaRepository<ActivityStream, Long> {
}
