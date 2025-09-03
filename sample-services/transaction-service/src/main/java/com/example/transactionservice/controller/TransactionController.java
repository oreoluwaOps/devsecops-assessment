package com.example.transactionservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * REST controller for transaction operations.
 */
@RestController
public class TransactionController {

    @Value("${secret.token}")
    private String secretToken;

    private final List<Map<String, Object>> transactions = new ArrayList<>();

    /**
     * GET /transactions?accountId=...
     * Returns all recorded transactions for the specified account.
     */
    @GetMapping("/transactions")
    public ResponseEntity<Map<String, Object>> getTransactions(@RequestParam String accountId) {
        List<Map<String, Object>> filtered = transactions.stream()
                .filter(tx -> accountId.equals(String.valueOf(tx.get("accountId"))))
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("accountId", accountId);
        response.put("transactions", filtered);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /record
     * Records a new transaction.  The payload must include an accountId,
     * type and amount.
     */
    @PostMapping("/record")
    public ResponseEntity<Map<String, Object>> record(@RequestBody Map<String, Object> payload) {
        Map<String, Object> tx = new HashMap<>();
        tx.put("id", transactions.size() + 1);
        tx.put("accountId", String.valueOf(payload.get("accountId")));
        tx.put("type", payload.get("type"));
        tx.put("amount", payload.get("amount"));
        tx.put("timestamp", System.currentTimeMillis());
        // Insecure: attach secret token to every record
        tx.put("token", secretToken);
        transactions.add(tx);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Transaction recorded");
        response.put("transaction", tx);
        return ResponseEntity.ok(response);
    }
}