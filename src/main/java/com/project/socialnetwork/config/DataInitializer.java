package com.project.socialnetwork.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.project.socialnetwork.domain.Role; // Use the correct package for your entity
import com.project.socialnetwork.repository.RoleRepository; // Use the correct package for your repository

@Component
public class DataInitializer {

    private final RoleRepository repository;

    public DataInitializer(RoleRepository repository) {
        this.repository = repository;
    }

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Insert default values into the table
            repository.save(new Role(1L, "Admin", "ADMIN can do anything"));
            repository.save(new Role(2L, "User",
                    "USER can post their image, view other's posts and interact such as like, download image"));
        };
    }
}