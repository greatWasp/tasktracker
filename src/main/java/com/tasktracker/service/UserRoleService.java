package com.tasktracker.service;

import com.tasktracker.model.Role;
import com.tasktracker.model.User;
import com.tasktracker.model.UserRole;
import com.tasktracker.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Transactional
    public UserRole assignRoleToUser(Role role, User user){
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);

        return userRoleRepository.save(userRole);
    }
}
