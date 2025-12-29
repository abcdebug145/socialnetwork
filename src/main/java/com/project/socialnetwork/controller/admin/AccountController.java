package com.project.socialnetwork.controller.admin;

import java.util.List;

import com.project.socialnetwork.entity.BanRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.enums.AccountStatus;
import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.BanRequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final BanRequestService banRequestService;

    public AccountController(AccountService accountService, BanRequestService banRequestService) {
        this.accountService = accountService;
        this.banRequestService = banRequestService;
    }

    @ModelAttribute("currAccount")
    public Account account(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object usernameAttr = session.getAttribute("username");
        if (usernameAttr == null) {
            return null;
        }
        String username = usernameAttr.toString();
        return accountService.findByEmail(username);
    }

    @GetMapping("/admin")
    public String getAccountsPage(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageNumber = Math.max(page, 1);
        long totalPages = accountService.getTotalPages();
        Pageable getPage = PageRequest.of(pageNumber - 1, AccountService.PAGE_SIZE);
        List<Account> accounts = accountService.findAll(getPage).getContent();
        List<BanRequest> banRequests = banRequestService.getAllBanRequests();
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("reportList", banRequests);
        return "admin/page/accounts";
    }

    @GetMapping("/admin/ban-requests")
    public String getBanRequestPage(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageNumber = Math.max(page, 1);
        long totalPages = banRequestService.getTotalPages();
        Pageable getPage = PageRequest.of(pageNumber - 1, BanRequestService.PAGE_SIZE);
        List<BanRequest> banRequests = banRequestService.findAll(getPage).getContent();
        model.addAttribute("banRequests", banRequests);
        model.addAttribute("totalPages", totalPages);
        return "admin/page/ban-requests";
    }
    @PostMapping("/admin/ban-user")
    public String banAccount(@RequestParam("accountId") Long accountId) {
        Account account = accountService.findById(accountId);
        account.setStatus(AccountStatus.BANNED);
        accountService.saveAccount(account);
        return "redirect:/admin";
    }
    @PostMapping("/admin/delete-report")
    public String deleteReport(@RequestParam("reportId") Long reportId) {
        banRequestService.deleteBanRequestById(reportId);
        return "redirect:/admin";
    }
    
    @PutMapping("/admin/accounts/api-ban-account")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        accountService.saveAccount(account);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/admin/api-changeStatus-account")
    public ResponseEntity<Account> changeStatusAccount(@RequestParam("id") Long accountId,
                                                       @RequestParam("currStatus") String statusId) {
        Account account = accountService.findById(accountId);
        account.setStatus(statusId.equalsIgnoreCase("ACTIVE") ? AccountStatus.BANNED : AccountStatus.ACTIVE);
        accountService.saveAccount(account);
        return ResponseEntity.ok(account);
    }
}
