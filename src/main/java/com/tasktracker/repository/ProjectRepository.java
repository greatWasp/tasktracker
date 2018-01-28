package com.tasktracker.repository;

import com.tasktracker.model.Project;
import com.tasktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

    List<Project> findByManager(User manager);

    List<Project> findByDevelopers(User developer);

    Project findByNameAndManager(String name, User manager);

    Project findByNameAndDevelopers(String name, User developer);
}
