package com.project.socialnetwork.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.repository.AccountRepository;
import com.project.socialnetwork.repository.PostLikedRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PostLikedRepository postLikedRepository;

    public AccountService(AccountRepository accountRepository, PostLikedRepository postLikedRepository) {
        this.accountRepository = accountRepository;
        this.postLikedRepository = postLikedRepository;
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

    public List<Post> getPostLiked(Long accountId) {
        return postLikedRepository.findAllByAccounts_Id(accountId);
    }
}
