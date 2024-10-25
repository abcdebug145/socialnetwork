package com.project.socialnetwork.controller.client;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.RoleService;
import com.project.socialnetwork.service.StatusService;
import com.project.socialnetwork.service.UploadService;

import jakarta.servlet.annotation.MultipartConfig;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class UserController {
    private final AccountService accountService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final StatusService statusService;

    public UserController(AccountService accountService, RoleService roleService, PasswordEncoder passwordEncoder,
            StatusService statusService) {
        this.accountService = accountService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.statusService = statusService;
    }

    // Login

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        // model.addAttribute("loginAccount", new Account());
        return "client/page/auth/login";
    }

    // Register

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("regAccount", new Account());
        return "client/page/auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("regAccount") Account account) {
        account.setRole(roleService.getRoleByName("USER"));
        String avatar = "default-avatar.png";
        account.setStatus(statusService.findById(1L)); // active account
        account.setAvatar(avatar);
        String password = passwordEncoder.encode(account.getPassword());
        account.setPassword(password);
        accountService.saveAccount(account);
        return "redirect:/login";
    }
    
}
