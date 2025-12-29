package com.project.socialnetwork.controller.client;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.EmailService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);
    
    private final EmailService passwordResetService;
    private final AccountService accountService;

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestParam("email") String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email cannot be empty");
        }
        
        try {
            Account account = accountService.findByEmail(email.trim());
            if (account == null) {
                logger.warn("Password reset requested for non-existent email: {}", email);
                return ResponseEntity.status(404).body("Email not found");
            }
            
            String resetToken = UUID.randomUUID().toString();
            passwordResetService.sendPasswordResetEmail(email.trim(), resetToken);
            logger.info("Password reset email sent to: {}", email);
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (Exception e) {
            logger.error("Error sending password reset email to: {}", email, e);
            return ResponseEntity.status(500)
                    .body("Failed to send password reset email: " + e.getMessage());
        }
    }
}

