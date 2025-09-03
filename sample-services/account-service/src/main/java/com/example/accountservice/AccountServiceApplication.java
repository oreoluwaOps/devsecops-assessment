package com.example.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;

/**
 * Entry point for the account service.
 */
@SpringBootApplication
public class AccountServiceApplication {

    @Value("${secret.key}")
    private String secretKey;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("AccountService started. Secrets are loaded but not displayed for security.");
    }
}