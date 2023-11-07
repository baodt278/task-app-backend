package com.taskapp.be.repository;

import com.taskapp.be.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(long id);

    boolean existsByNameAndProjectId(String name, long id);

    Task findByNameAndId(String name, long id);

    @Query("select t from Task t " +
            "join UserProject up on t.project.id = up.project.id " +
            "where t.status = 'TODO' and t.project.id = :id")
    List<Task> findTodoTaskInProject(long id);

    @Query("select t from Task t " +
            "join UserProject up on t.project.id = up.project.id " +
            "where t.status = 'IN_PROGRESS' and t.project.id = :id")
    List<Task> findInprogressTaskInProject(long id);

    @Query("select t from Task t " +
            "join UserProject up on t.project.id = up.project.id " +
            "where t.status = 'DONE' and t.project.id = :id")
    List<Task> findDoneTaskInProject(long id);

    @Query("select t from Task t " +
            "join UserProject up on t.project.id = up.project.id " +
            "where t.status = 'FAILED' and t.project.id = :id")
    List<Task> findFailedTaskInProject(long id);

    @Query("select t from Task t " +
            "join User u on t.assigneeUser.id = u.id " +
            "where u.username = :username and t.status != 'FAILED'")
    List<Task> findByAssigneeUserUsername(String username);

    @Query("select t from Task t " +
            "join User u on t.assigneeUser.id = u.id " +
            "where u.username = :username")
    List<Task> findForMessage(String username);
    Task findById(long id);
}
