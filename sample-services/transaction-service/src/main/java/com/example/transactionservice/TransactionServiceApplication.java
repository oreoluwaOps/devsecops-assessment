package com.example.transactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;

/**
 * Entry point for the transaction service.
 */
@SpringBootApplication
public class TransactionServiceApplication {

    @Value("${secret.token}")
    private String secretToken;

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("TransactionService started. Secrets are loaded but not displayed for security.");
    }
}