package com.tasktracker.dto;

import com.tasktracker.model.Role;
import com.tasktracker.validation.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordMatches
public class UserDto {

    @NotNull
    @UniqueUsername
    private String username;

    @NotNull
    @ValidEmail
    @UniqueEmail
    private String email;

    @NotNull
    @Size(min = 4, max = 20)
    private String password;

    @NotNull
    private String matchingPassword;

    private Role role;
}