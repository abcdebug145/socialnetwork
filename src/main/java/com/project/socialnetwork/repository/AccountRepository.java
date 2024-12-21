package com.project.socialnetwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Page<Account> findAll(Pageable pageable);

    Account save(Account account);

    Account findByUsername(String username);

    Account findByEmail(String email);
}
