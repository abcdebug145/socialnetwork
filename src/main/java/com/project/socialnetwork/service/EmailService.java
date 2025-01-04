package com.project.socialnetwork.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class EmailService {
    
    @Value("${gridmail.api.key}")
    private String apiKey;
    
    @Value("${gridmail.sender.email}")
    private String senderEmail;

    private final String apiUrl = "https://api.sendgrid.com/v3/mail/send";

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            // Debug: Check if API key is loaded
            if (apiKey == null || apiKey.isEmpty()) {
                System.out.println("WARNING: API key is null or empty");
                return false;
            }

            // Debug: Print first few characters of API key (safely)
            System.out.println("API Key prefix: " + (apiKey.length() > 4 ? apiKey.substring(0, 4) + "..." : "INVALID"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Debug: Print full header value (remove in production)
            String authHeader = "Bearer " + apiKey;
            System.out.println("Full Authorization header: " + authHeader);
            headers.set("Authorization", authHeader);

            String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;

            String emailBody = String.format("""
                {
                    "personalizations": [{
                        "to": [{"email": "%s"}],
                        "subject": "Password Reset Request"
                    }],
                    "from": {"email": "%s"},
                    "content": [{
                        "type": "text/html",
                        "value": "<p>Click the following link to reset your password:</p><a href='%s'>Reset Password</a>"
                    }]
                }""", toEmail, senderEmail, resetLink);

            // Debug: Print request details
            System.out.println("Request URL: " + apiUrl);
            System.out.println("Request Body: " + emailBody);

            HttpEntity<String> request = new HttpEntity<>(emailBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    apiUrl,
                    request,
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            System.out.println("Detailed error information:");
            System.out.println("Error type: " + e.getClass().getName());
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
