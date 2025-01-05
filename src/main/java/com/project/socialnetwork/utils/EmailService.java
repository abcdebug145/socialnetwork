package com.project.socialnetwork.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
    private static String apiKey;
    private static String senderEmail;
    private final String apiUrl = "https://api.sendgrid.com/v3/mail/send";
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        this.apiKey = env.getProperty("api.key");
        this.senderEmail = env.getProperty("sender.email");

        if (apiKey == null || senderEmail == null) {
            System.err.println("WARNING: Missing required environment variables: 'api.key' or 'sender.email'");
        }
    }

    public boolean sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            if (apiKey == null || apiKey.isEmpty()) {
                System.out.println("WARNING: API key is null or empty");
                return false;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

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

            HttpEntity<String> request = new HttpEntity<>(emailBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}