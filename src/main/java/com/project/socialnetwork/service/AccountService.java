package com.project.socialnetwork.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.entity.PostLiked;
import com.project.socialnetwork.repository.AccountRepository;
import com.project.socialnetwork.repository.PostLikedRepository;

import jakarta.servlet.ServletContext;

@Service
public class AccountService {
    public static final int PAGE_SIZE = 20;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final PostLikedRepository postLikedRepository;
    private final ImageService uploadService;
    private final ServletContext servletContext;

    public AccountService(AccountRepository accountRepository, PostLikedRepository postLikedRepository,
                          ImageService imageService, ServletContext servletContext) {
        this.accountRepository = accountRepository;
        this.postLikedRepository = postLikedRepository;
        this.uploadService = imageService;
        this.servletContext = servletContext;
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> saveAll(List<Account> accounts) {
        return accountRepository.saveAll(accounts);
    }

    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Account delete(Long id) {
        Account account = accountRepository.findById(id).orElseThrow();
        accountRepository.delete(account);
        return account;
    }

    public long getTotalPages() {
        long totalItems = accountRepository.count();
        return (long) Math.ceil(totalItems / (double) PAGE_SIZE);
    }

    public List<PostLiked> getPostsLiked(Long accountId) {
        return postLikedRepository.findByAccount_Id(accountId);
    }

    public String newAvatar(String oldAvatar, MultipartFile newAvatar) {
        if (!"default-avatar.png".equals(oldAvatar)) {
            String avatarPath = this.servletContext.getRealPath("/resources/images/avatar/" + oldAvatar);
            try {
                File file = new File(avatarPath);
                if (file.exists() && !file.delete()) {
                    logger.warn("Failed to delete old avatar file at path: {}", avatarPath);
                }
            } catch (Exception e) {
                logger.error("Error deleting old avatar file at path: {}", avatarPath, e);
            }
        }
        return uploadService.saveUploadFile(newAvatar, "avatar");
    }

}
