package com.tasktracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component
public class TasktrackerPreAuthenticationChecks implements UserDetailsChecker {

    @Override
    public void check(UserDetails userDetails) {
        if (!userDetails.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Credentials expired");
        }
    }
}
