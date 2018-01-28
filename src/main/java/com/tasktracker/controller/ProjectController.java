package com.tasktracker.controller;

import com.tasktracker.model.Project;
import com.tasktracker.model.Role;
import com.tasktracker.model.User;
import com.tasktracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{projectName}")
    public String getProject(@PathVariable String projectName, Model model){
        model.addAttribute("projectName", projectName);
        return "project";
    }

    @GetMapping("/{projectName}/tasks/{taskName}")
    public String getTask(@PathVariable String projectName, @PathVariable String taskName, Model model){
        model.addAttribute("projectName", projectName);
        model.addAttribute("taskName", taskName);
        return "task";
    }
}
