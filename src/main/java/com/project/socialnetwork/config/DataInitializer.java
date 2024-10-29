package com.project.socialnetwork.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.project.socialnetwork.domain.AccountStatus;
import com.project.socialnetwork.domain.Role; // Use the correct package for your entity
import com.project.socialnetwork.repository.RoleRepository; // Use the correct package for your repository
import com.project.socialnetwork.repository.StatusRepository;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;

    public DataInitializer(RoleRepository rolRepository, StatusRepository statusRepository) {
        this.roleRepository = rolRepository;
        this.statusRepository = statusRepository;
    }

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Insert default values into the table
            roleRepository.save(new Role(1L, "Admin", "ADMIN can do anything"));
            roleRepository.save(new Role(2L, "User",
                    "USER can post their image, view other's posts and interact such as like, download image"));

            statusRepository.save(new AccountStatus(1L, "ACTIVE"));
            statusRepository.save(new AccountStatus(2L, "BANNED"));
        };
    }
}