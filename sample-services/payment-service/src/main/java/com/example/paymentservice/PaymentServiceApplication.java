package com.example.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;

/**
 * Entry point for the payment service.
 */
@SpringBootApplication
public class PaymentServiceApplication {

    @Value("${api.key}")
    private String apiKey;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("PaymentService started. Secrets are loaded but not displayed for security.");
    }
}