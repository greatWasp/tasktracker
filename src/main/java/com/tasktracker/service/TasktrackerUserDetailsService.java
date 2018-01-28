package com.tasktracker.service;

import com.tasktracker.model.User;
import com.tasktracker.userdetails.TasktrackerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TasktrackerUserDetailsService implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return new TasktrackerUserDetails(user);
    }
}
