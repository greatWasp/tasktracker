package com.tasktracker.validation;

import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(final UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext context) {
        return !userService.checkIfUsernameExists(username);
    }
}