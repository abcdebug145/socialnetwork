package com.project.socialnetwork.controller.client;

import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.utils.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class PasswordResetController {

    @Autowired
    private EmailService passwordResetService;
    
    private AccountService accountService;


    @PostMapping("/request-password-reset")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestParam("email") String email) {
        try {
            if(accountService.findByEmail(email)==null){
                return ResponseEntity.status(500)
                        .body("Email not found");
            }
            // Generate a simple UUID as the reset token
            String resetToken = UUID.randomUUID().toString();

            // Send the email with the reset token

            passwordResetService.sendPasswordResetEmail(email, resetToken);

            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Failed to send password reset email: " + e.getMessage());
        }
    }
    
}

