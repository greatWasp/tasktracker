package com.tasktracker.userdetails;

import com.tasktracker.model.User;
import com.tasktracker.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TasktrackerUserDetails extends User implements UserDetails{

    public TasktrackerUserDetails(User user){
        this.setId(user.getId());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setIsEnabled(user.getIsEnabled());
        this.setRoles(user.getRoles());
        this.setVerificationToken(user.getVerificationToken());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(UserRole item : this.getRoles()){
            authorities.add(new SimpleGrantedAuthority(item.getRole().toString()));
        }
        return authorities;
    }

//    @Override
//    public String getPassword() {
//        return null;
//    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getIsEnabled();
    }
}
