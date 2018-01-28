package com.tasktracker.service;

import com.tasktracker.model.User;
import com.tasktracker.model.VerificationToken;
import com.tasktracker.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Value("${app-url}")
    private String appUrl;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public VerificationToken generateVerificationTokenForUser(User user){
        VerificationToken verificationToken = new VerificationToken();
        String token = UUID.randomUUID().toString();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    public VerificationToken getVerificationTokenByToken(String token){
        return verificationTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteVerificationToken(VerificationToken token){
        verificationTokenRepository.delete(token);
    }

    public void sendVerificationTokenByEmail(VerificationToken verificationToken){
        User user = verificationToken.getUser();
        String message = "Your Tasktracker account has been registered." +
                "Please confirm registration by clicking this link: " +
                appUrl +
                "/register/confirm?token=" +
                verificationToken.getToken();
        emailService.sendMail(user.getEmail(), "Tasktracker registration", message);
    }
}
