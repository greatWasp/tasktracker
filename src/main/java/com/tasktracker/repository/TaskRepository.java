package com.tasktracker.repository;

import com.tasktracker.model.Project;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

    List<Task> findByProject(Project project);

    List<Task> findByProjectAndDevelopers(Project project, User developer);

    Task findByNameAndProject(String name, Project project);
}
