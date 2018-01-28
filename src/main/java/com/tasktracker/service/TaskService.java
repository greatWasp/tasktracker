package com.tasktracker.service;

import com.tasktracker.dto.StatusDto;
import com.tasktracker.dto.TaskDto;
import com.tasktracker.model.Project;
import com.tasktracker.model.Task;
import com.tasktracker.model.TaskStatus;
import com.tasktracker.model.User;
import com.tasktracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public Task createTask(Project project, TaskDto taskDto){
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setProject(project);
        task.setStatus(TaskStatus.Waiting);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks(Project project){
        return taskRepository.findByProject(project);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllDeveloperTasks(Project project, User developer){
        return taskRepository.findByProjectAndDevelopers(project, developer);
    }

    @Transactional(readOnly = true)
    public Task getTask(Project project, String taskName){
        return taskRepository.findByNameAndProject(taskName, project);
    }

    @Transactional
    public Task changeStatus(Task task, StatusDto statusDto){
        task.setStatus(statusDto.getStatus());
        return taskRepository.save(task);
    }

    @Transactional
    public Task addDevelopers(Task task, List<User> developers){
        List<User> currentDevs = task.getDevelopers();
        currentDevs.addAll(developers);
        task.setDevelopers(currentDevs);
        return taskRepository.save(task);
    }
}
