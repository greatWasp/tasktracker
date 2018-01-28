package com.tasktracker.service;

import com.tasktracker.dto.ProjectDto;
import com.tasktracker.model.Project;
import com.tasktracker.model.User;
import com.tasktracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<Project> getAllManagerProjects(User manager){
        return projectRepository.findByManager(manager);
    }

    @Transactional(readOnly = true)
    public List<Project> getAllDeveloperProjects(User developer){
        return projectRepository.findByDevelopers(developer);
    }

    @Transactional(readOnly = true)
    public Project getManagerProject(String name, User manager){
        return projectRepository.findByNameAndManager(name, manager);
    }

    @Transactional(readOnly = true)
    public Project getDeveloperProject(String name, User developer){
        return projectRepository.findByNameAndDevelopers(name, developer);
    }

    @Transactional
    public Project createProject(User manager, ProjectDto projectDto){
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setManager(manager);
        return projectRepository.save(project);
    }

    @Transactional
    public Project addDevelopers(Project project, List<User> developers){
        List<User> currentDevs = project.getDevelopers();
        currentDevs.addAll(developers);
        project.setDevelopers(currentDevs);
        return projectRepository.save(project);
    }
}
