package com.project.socialnetwork.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.repository.AccountRepository;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.RoleService;
import com.project.socialnetwork.service.StatusService;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final RoleService roleService;

    public AccountController(AccountService accountService, RoleService roleService) {
        this.accountService = accountService;
        this.roleService = roleService;
    }

    @GetMapping("/admin/accounts")
    public String getAccountsPage(Model model, @RequestParam(value = "page") Optional<String> page) {
        if (!page.isPresent())
            return "redirect:/admin/accounts?page=1";
        int pageNumber = Integer.parseInt(page.get());
        long totalPages = accountService.getTotalPages();
        Pageable getPage = PageRequest.of(pageNumber - 1, 20);
        List<Account> accounts = accountService.findAll(getPage).getContent();
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalPages", totalPages);
        return "admin/page/account/accounts";
    }

    @GetMapping("/admin/delete-account")
    public String deleteAccount(@RequestParam("id") Long id) {
        // accountService.delete(id);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/admin/accounts/account-details")
    public String getAccountDetails(@RequestParam("id") Long id, Model model) {
        Account account = accountService.findById(id);
        model.addAttribute("account", account);
        return "admin/page/account/account-details";
    }

}

@RestController
class AccountAPI {

    private final AccountService accountService;
    private final RoleService roleService;
    private final StatusService statusService;

    public AccountAPI(AccountService accountService, RoleService roleService,
            AccountRepository accountRepository,
            StatusService statusService) {
        this.accountService = accountService;
        this.roleService = roleService;
        this.statusService = statusService;
    }

    @PostMapping("/admin/api-accounts")
    public ResponseEntity<List<Account>> createAccount(@RequestBody List<Account> accounts) {
        for (Account account : accounts) {
            account.setRole(roleService.getRoleById(account.getRole().getId()));
        }
        accountService.saveAll(accounts);
        return ResponseEntity.ok(accounts);
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
        account.setStatus(statusService.findById(Long.parseLong(statusId) == 1 ? 2L : 1L));
        accountService.saveAccount(account);
        return ResponseEntity.ok(account);
    }

}
