package com.tasktracker.validation;

import com.tasktracker.dto.UserDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return UserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.empty_username", "Username is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.empty_email", "Email is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.empty_password", "Password is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "matchingPassword", "error.empty_matching_password", "Matching password is required");
    }

}
