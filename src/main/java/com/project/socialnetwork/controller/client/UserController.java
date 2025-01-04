package com.project.socialnetwork.controller.client;

import com.project.socialnetwork.enums.AccountStatus;
import com.project.socialnetwork.enums.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
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
        String avatar = "default-avatar.png";
        account.setStatus(AccountStatus.ACTIVE); // active account
        account.setAvatar(avatar);
        String password = passwordEncoder.encode(account.getPassword());
        account.setPassword(password);
        accountService.saveAccount(account);
        return "redirect:/login";
    }
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "client/page/auth/forgot-password";
    }
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(HttpServletRequest request, @RequestBody String passwordData) {
        String password = passwordData.split(":\"")[1].split("\"")[0].split("}")[0];// {"password":"hehe"}
        HttpSession session = request.getSession();
        String username = session.getAttribute("username").toString();
        Account currAccount = accountService.findByEmail(username);
        String passwordEncoded = passwordEncoder.encode(password);
        currAccount.setPassword(passwordEncoded);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/getAccountStatus")
    public ResponseEntity<String> getAccountStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = session.getAttribute("username").toString();
        Account currAccount = accountService.findByEmail(username);
        return ResponseEntity.ok(currAccount.getStatus().toString());
    }

}
