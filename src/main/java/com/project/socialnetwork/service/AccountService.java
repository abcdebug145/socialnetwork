package com.project.socialnetwork.service;

import java.io.File;
import java.util.List;

import com.project.socialnetwork.utils.ImageService;
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
        for (Account account : accounts) {
            accountRepository.save(account);
        }
        return null;
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
        return (long) Math.ceil(accountRepository.count() / 20f);
    }

    public List<PostLiked> getPostsLiked(Long accountId) {
        return postLikedRepository.findByAccount_Id(accountId);
    }

    public String newAvatar(String oldAvatar, MultipartFile newAvatar) {
        if (!oldAvatar.equals("default-avatar.png")) {
            String avatarPath = this.servletContext.getRealPath("/resources/images/avatar/" + oldAvatar);
            try {
                File file = new File(avatarPath);
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uploadService.saveUploadFile(newAvatar, "avatar");
    }

}
