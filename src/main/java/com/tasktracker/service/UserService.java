package com.tasktracker.service;

import com.tasktracker.dto.UserDto;
import com.tasktracker.model.*;
import com.tasktracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getDisabledUserByEmailAndPassword(String email, String password){
        User user = userRepository.findByEmailAndIsEnabled(email, false);
        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return user;
        }
        return null;
    }

    @Transactional
    public User registerUser(UserDto userDto){
        User user = new User();
        user.setName(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setIsEnabled(false);

        userRepository.save(user);
        UserRole userRole = userRoleService.assignRoleToUser(userDto.getRole(), user);
        user.setRoles(Arrays.asList(userRole));
        VerificationToken token = verificationTokenService.generateVerificationTokenForUser(user);
        verificationTokenService.sendVerificationTokenByEmail(token);
        return user;
    }

    @Transactional
    public User activateUser(String verificationToken){
        VerificationToken token = verificationTokenService.getVerificationTokenByToken(verificationToken);
        if(token == null) {
            return null;
        } else {
            User user = token.getUser();
            user.setIsEnabled(true);
            userRepository.save(user);
            verificationTokenService.deleteVerificationToken(token);
            return user;
        }
    }

    @Transactional(readOnly = true)
    public List<User> getAllAvailableDevelopers(Project project){
        return userRepository.findByProjectsNotIn(project.getId());
    }

    @Transactional(readOnly = true)
    public List<User> getAllAvailableDevelopersWithName(String name, Project project){
        return userRepository.findByNameAndProjectsNotIn(name, project.getId());
    }

    @Transactional(readOnly = true)
    public List<User> getAllAvailableDevelopersForTask(Project project, Task task){
        return userRepository.findByProjectsAndTasksNotIn(project.getId(), task.getId());
    }

    @Transactional(readOnly = true)
    public List<User> getAllAvailableDevelopersForTaskWithName(String name, Project project, Task task){
        return userRepository.findByNameAndProjectsAndTasksNotIn(name, project.getId(), task.getId());
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByIds(List<Long> userIds){
        return userRepository.findAllById(userIds);
    }

    @Transactional(readOnly = true)
    public boolean checkIfEmailExists(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkIfUsernameExists(String username){
        return userRepository.existsByName(username);
    }
}
