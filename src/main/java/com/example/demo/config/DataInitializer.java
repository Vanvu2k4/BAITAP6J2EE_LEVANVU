package com.example.demo.config;

import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountService accountService;

    @Override
    public void run(String... args) {
        accountService.getOrCreateRole("ROLE_ADMIN");
        accountService.getOrCreateRole("ROLE_USER");

        accountService.createAdminIfNotExists(
                "admin1",
                "Administrator 1",
                "admin1@demo.local",
                "Admin@123"
        );

        accountService.createAdminIfNotExists(
                "admin2",
                "Administrator 2",
                "admin2@demo.local",
                "Admin@123"
        );
    }
}

