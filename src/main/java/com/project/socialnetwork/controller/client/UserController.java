package com.project.socialnetwork.controller.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.enums.AccountStatus;
import com.project.socialnetwork.enums.Role;
import com.project.socialnetwork.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String DEFAULT_AVATAR = "default-avatar.png";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public UserController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "client/page/auth/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("regAccount", new Account());
        return "client/page/auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("regAccount") Account account) {
        account.setRole(Role.USER);
        account.setStatus(AccountStatus.ACTIVE);
        account.setAvatar(DEFAULT_AVATAR);
        String password = passwordEncoder.encode(account.getPassword());
        account.setPassword(password);
        accountService.saveAccount(account);
        logger.info("New user registered: {}", account.getEmail());
        return "redirect:/login";
    }
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "client/page/auth/forgot-password";
    }
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(HttpServletRequest request, @RequestBody String passwordData) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        
        try {
            JsonNode jsonNode = objectMapper.readTree(passwordData);
            String password = jsonNode.get("password").asText();
            
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password cannot be empty");
            }
            
            Account currAccount = accountService.findByEmail(usernameAttr.toString());
            if (currAccount == null) {
                return ResponseEntity.badRequest().body("Account not found");
            }
            
            String passwordEncoded = passwordEncoder.encode(password);
            currAccount.setPassword(passwordEncoded);
            accountService.saveAccount(currAccount);
            logger.info("Password changed for user: {}", currAccount.getEmail());
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            logger.error("Error changing password", e);
            return ResponseEntity.status(500).body("Failed to change password: " + e.getMessage());
        }
    }

    @GetMapping("/getAccountStatus")
    public ResponseEntity<String> getAccountStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        
        try {
            Account currAccount = accountService.findByEmail(usernameAttr.toString());
            if (currAccount == null) {
                return ResponseEntity.badRequest().body("Account not found");
            }
            return ResponseEntity.ok(currAccount.getStatus().toString());
        } catch (Exception e) {
            logger.error("Error getting account status", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

}
