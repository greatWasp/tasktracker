package com.tasktracker.controller;

import com.tasktracker.dto.CommentDto;
import com.tasktracker.dto.StatusDto;
import com.tasktracker.dto.TaskDto;
import com.tasktracker.model.Comment;
import com.tasktracker.model.Project;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import com.tasktracker.service.CommentService;
import com.tasktracker.service.ProjectService;
import com.tasktracker.service.TaskService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developer")
public class DeveloperApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/profile")
    public User getProfile(Authentication authentication){
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return projectService.getAllDeveloperProjects(user);
    }

    @GetMapping("/projects/{projectName}")
    public Project getProject(@PathVariable String projectName, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return projectService.getDeveloperProject(projectName, user);
    }

    @GetMapping("/projects/{projectName}/tasks")
    public List<Task> getAllDeveloperTasks(@PathVariable String projectName,
                                           @RequestParam(value = "query", required = false) String query,
                                           Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getDeveloperProject(projectName, user);
        List<Task> result = null;
        if(query != null){
            if(query.equals("All")){
                result = taskService.getAllTasks(project);
            }
            if(query.equals("My")){
                result = taskService.getAllDeveloperTasks(project, user);
            }
        } else {
            result = taskService.getAllTasks(project);
        }
        return result;
    }

    @GetMapping("/projects/{projectName}/tasks/{taskName}")
    public Task getTask(@PathVariable String projectName,
                        @PathVariable String taskName,
                        Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getDeveloperProject(projectName, user);
        return taskService.getTask(project, taskName);
    }

    @GetMapping("/projects/{projectName}/tasks/{taskName}/comments")
    public List<Comment> getAllComments(@PathVariable String projectName,
                                        @PathVariable String taskName,
                                        Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getDeveloperProject(projectName, user);
        Task task = taskService.getTask(project, taskName);
        return commentService.getAllComments(task);
    }

    @PostMapping("/projects/{projectName}/tasks/create")
    public Task createNewTask(@PathVariable String projectName,
                              @RequestBody TaskDto taskDto,
                              Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getDeveloperProject(projectName, user);
        return taskService.createTask(project, taskDto);
    }

    @PostMapping("/projects/{projectName}/tasks/{taskName}/comments/create")
    public Comment createComment(@PathVariable String projectName,
                                 @PathVariable String taskName,
                                 @RequestBody CommentDto commentDto,
                                 Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getDeveloperProject(projectName, user);
        Task task = taskService.getTask(project, taskName);
        return commentService.createComment(user, task, commentDto);
    }

    @PutMapping("/projects/{projectName}/tasks/{taskName}/comments/edit")
    public Comment editComment(@PathVariable String projectName,
                               @PathVariable String taskName,
                               @RequestBody CommentDto commentDto){
        return commentService.saveComment(commentDto);
    }

    @PutMapping("/projects/{projectName}/tasks/{taskName}/status/edit")
    public Task editTaskStatus(@PathVariable String projectName,
                               @PathVariable String taskName,
                               @RequestBody StatusDto statusDto,
                               Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getDeveloperProject(projectName, user);
        Task task = taskService.getTask(project, taskName);
        return taskService.changeStatus(task, statusDto);
    }

    @DeleteMapping("/projects/{projectName}/tasks/{taskName}/comments/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
    }
}
