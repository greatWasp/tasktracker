package com.tasktracker.controller;

import com.tasktracker.dto.UserDto;
import com.tasktracker.model.User;
import com.tasktracker.model.VerificationToken;
import com.tasktracker.service.UserService;
import com.tasktracker.service.VerificationTokenService;
import com.tasktracker.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserValidator());
    }

    @GetMapping
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "registration";
    }

    @PostMapping
    public ModelAndView registerNewUser(@ModelAttribute("userDto") @Valid UserDto userDto,
                                        BindingResult bindingResult,
                                        Errors errors){

        if(bindingResult.hasErrors()){
            return new ModelAndView("registration", "userDto", userDto);
        }
        userService.registerUser(userDto);
        return new ModelAndView("email-confirmation", "email", userDto.getEmail());
    }

    @GetMapping("/confirm")
    public ModelAndView activateUser(@RequestParam("token") String token){
        if (userService.activateUser(token) == null){
            return new ModelAndView("badToken");
        }
        return new ModelAndView("email-confirmed");
    }

    @RequestMapping("/confirmation-required")
    public ModelAndView showConfirmationRequiredPage(){
        UserDto userDto = new UserDto();
        return new ModelAndView("confirmation-required", "userDto", userDto);
    }

    @PostMapping("/resend-token")
    public String resendToken(UserDto userDto, Model model){
        User user = userService.getDisabledUserByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        if(user != null){
            verificationTokenService.sendVerificationTokenByEmail(user.getVerificationToken());
            return "email-confirmation";
        }
        model.addAttribute("error", "Bad credentials");
        model.addAttribute("userDto", userDto);
        return "confirmation-required";
    }
}
