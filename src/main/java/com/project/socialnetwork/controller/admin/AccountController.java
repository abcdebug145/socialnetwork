package com.project.socialnetwork.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.enums.AccountStatus;
import com.project.socialnetwork.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ModelAttribute("currAccount")
    public Account account(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = session.getAttribute("username").toString();
        return accountService.findByEmail(username);
    }

    @GetMapping("/admin")
    public String getAccountsPage(Model model, @RequestParam(value = "page") Optional<String> page) {
        if (!page.isPresent())
            return "redirect:/admin?page=1";
        int pageNumber = Integer.parseInt(page.get());
        long totalPages = accountService.getTotalPages();
        Pageable getPage = PageRequest.of(pageNumber - 1, 20);
        List<Account> accounts = accountService.findAll(getPage).getContent();
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalPages", totalPages);
        return "admin/page/accounts";
    }

    @PutMapping("/admin/accounts/api-ban-account")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        accountService.saveAccount(account);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/admin/api-changeStatus-account")
    public ResponseEntity<Account> changeStatusAccount(@RequestParam("id") String accountId,
            @RequestParam("currStatus") String statusId) {
        Account account = accountService.findById(Long.parseLong(accountId));
        System.out.println(statusId);
        System.out.println(AccountStatus.BANNED + " " + AccountStatus.ACTIVE);
        account.setStatus(statusId.equalsIgnoreCase("ACTIVE") ? AccountStatus.BANNED : AccountStatus.ACTIVE);
        accountService.saveAccount(account);
        return ResponseEntity.ok(account);
    }

}
