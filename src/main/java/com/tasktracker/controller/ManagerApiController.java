package com.tasktracker.controller;

import com.tasktracker.dto.CommentDto;
import com.tasktracker.dto.ProjectDto;
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
@RequestMapping("/api/manager")
public class ManagerApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/projects")
    public List<Project> getAllProjects(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return projectService.getAllManagerProjects(user);
    }

    @GetMapping("/projects/{projectName}/developers/available")
    public List<User> getAvailableDevelopers(@PathVariable String projectName,
                                             @RequestParam(value = "name",required = false) String name,
                                             Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
        List<User> result;
        if(name != null){
            result = userService.getAllAvailableDevelopersWithName(name, project);
        } else {
            result = userService.getAllAvailableDevelopers(project);
        }
        return result;
    }

    @GetMapping("/projects/{projectName}/tasks/{taskName}/developers/available")
    public List<User> getAvailableDevelopersForTask(@PathVariable String projectName,
                                                    @PathVariable String taskName,
                                                    @RequestParam(value = "name",required = false) String name,
                                                    Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
        Task task = taskService.getTask(project, taskName);
        List<User> result;
        if(name != null){
            result = userService.getAllAvailableDevelopersForTaskWithName(name, project, task);
        } else {
            result = userService.getAllAvailableDevelopersForTask(project, task);
        }
        return result;
    }

    @GetMapping("/projects/{projectName}")
    public Project getProject(@PathVariable String projectName, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return projectService.getManagerProject(projectName, user);
    }

    @GetMapping("/projects/{projectName}/tasks")
    public List<Task> getAllTasks(@PathVariable String projectName, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
        return taskService.getAllTasks(project);
    }

    @GetMapping("/projects/{projectName}/tasks/{taskName}")
    public Task getTask(@PathVariable String projectName,
                        @PathVariable String taskName,
                        Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
        return taskService.getTask(project, taskName);
    }

    @GetMapping("/projects/{projectName}/tasks/{taskName}/comments")
    public List<Comment> getAllComments(@PathVariable String projectName,
                                        @PathVariable String taskName,
                                        Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
        Task task = taskService.getTask(project, taskName);
        return commentService.getAllComments(task);
    }

    @PostMapping("/projects/create")
    public Project createNewProject(@RequestBody ProjectDto projectDto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return projectService.createProject(user, projectDto);
    }

    @PostMapping("/projects/{projectName}/tasks/create")
    public Task createNewTask(@PathVariable String projectName,
                              @RequestBody TaskDto taskDto,
                              Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
        return taskService.createTask(project, taskDto);
    }

    @PostMapping("/projects/{projectName}/tasks/{taskName}/comments/create")
    public Comment createComment(@PathVariable String projectName,
                                 @PathVariable String taskName,
                                 @RequestBody CommentDto commentDto,
                                 Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
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
        Project project = projectService.getManagerProject(projectName, user);
        Task task = taskService.getTask(project, taskName);
        return taskService.changeStatus(task, statusDto);
    }

    @PutMapping("/projects/{projectName}/developers/add")
    public Project addDeveloperToProject(@PathVariable String projectName,
                                         @RequestBody List<Long> userIds,
                                         Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
        List<User> developers = userService.getUsersByIds(userIds);
        return projectService.addDevelopers(project, developers);
    }

    @PutMapping("/projects/{projectName}/tasks/{taskName}/developers/add")
    public Task addDeveloperToTask(@PathVariable String projectName,
                                      @PathVariable String taskName,
                                      @RequestBody List<Long> userIds,
                                      Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Project project = projectService.getManagerProject(projectName, user);
        Task task = taskService.getTask(project, taskName);
        List<User> developers = userService.getUsersByIds(userIds);
        return taskService.addDevelopers(task, developers);
    }

    @DeleteMapping("/projects/{projectName}/tasks/{taskName}/comments/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
    }
}
