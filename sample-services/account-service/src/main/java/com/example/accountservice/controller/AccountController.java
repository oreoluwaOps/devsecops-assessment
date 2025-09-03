package com.example.accountservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * REST controller for account operations.
 */
@RestController
public class AccountController {

    @Value("${secret.key}")
    private String secretKey;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    private final Map<String, Double> accounts = new ConcurrentHashMap<>();

    public AccountController() {
        accounts.put("1", 1000.0);
        accounts.put("2", 2000.0);
    }

    /**
     * GET /balance?accountId=...
     * Returns the current balance for the given account.  If the account
     * does not exist, a 404 response is returned.
     */
    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@RequestParam String accountId) {
        Double bal = accounts.get(accountId);
        if (bal == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("accountId", accountId);
        response.put("balance", bal);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /credit
     * Credits the specified account with the given amount.
     */
    @PostMapping("/credit")
    public ResponseEntity<Map<String, Object>> credit(@RequestBody Map<String, Object> payload) {
        String accountId = String.valueOf(payload.get("accountId"));
        double amount = Double.parseDouble(String.valueOf(payload.get("amount")));
        accounts.putIfAbsent(accountId, 0.0);
        double newBalance = accounts.get(accountId) + amount;
        accounts.put(accountId, newBalance);
        Map<String, Object> response = new HashMap<>();
        response.put("accountId", accountId);
        response.put("balance", newBalance);
        return ResponseEntity.ok(response);
    }
}