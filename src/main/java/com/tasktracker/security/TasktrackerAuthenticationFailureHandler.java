package com.tasktracker.security;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class TasktrackerAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        this.saveException(httpServletRequest, e);
        if(e.getClass().isAssignableFrom(DisabledException.class)){
            this.getRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, "/register/confirmation-required");
        } else {
            httpServletRequest.getRequestDispatcher("/login?error").forward(httpServletRequest, httpServletResponse);
        }
    }
}
