package com.tasktracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component
public class TasktrackerPostAuthenticationChecks implements UserDetailsChecker {

    @Override
    public void check(UserDetails userDetails) {
        if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("Account is locked");
        } else if (!userDetails.isEnabled()) {
            throw new DisabledException("Account is disabled");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("Account expired");
        }
    }
}
